package com.petfinder.qr.repository

import com.petfinder.qr.database.dao.PetDao
import com.petfinder.qr.database.dao.ScanHistoryDao
import com.petfinder.qr.database.dao.UserDao
import com.petfinder.qr.database.entity.PetEntity
import com.petfinder.qr.database.entity.ScanHistoryEntity
import com.petfinder.qr.database.entity.UserEntity
import com.petfinder.qr.di.ApplicationScope
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.model.ScanReport
import com.petfinder.qr.network.NetworkMonitor
import com.petfinder.qr.network.PetApiService
import com.petfinder.qr.network.dto.UpdateStatusRequest
import com.petfinder.qr.network.retryIO
import com.petfinder.qr.sync.ScanSyncService
import com.petfinder.qr.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for pet data, following the offline-first repository
 * pattern: the observable flows always read the Room **cache**, and (when
 * [Constants.USE_REMOTE_BACKEND] is on) the repository refreshes from the Spring
 * Boot API in the background and writes results back to Room. Mutations hit the
 * network then update the cache.
 *
 * The public API is unchanged from the local-only version, so ViewModels and the
 * UI need no changes regardless of which mode is active.
 */
@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val scanHistoryDao: ScanHistoryDao,
    private val userDao: UserDao,
    private val scanSyncService: ScanSyncService,
    private val petApi: PetApiService,
    private val networkMonitor: NetworkMonitor,
    @ApplicationScope private val externalScope: CoroutineScope,
) {
    private val remote: Boolean get() = Constants.USE_REMOTE_BACKEND

    val allPets: Flow<List<PetUiModel>> =
        petDao.observeAll()
            .map { list -> list.map { it.toUiModel() } }
            .onStart { refreshInBackground { refreshAllPets() } }

    val lostPets: Flow<List<LostPetUiModel>> =
        petDao.observeLost()
            .map { list -> list.map { it.toLostUiModel() } }
            .onStart { refreshInBackground { refreshLostPets() } }

    val scanCount: Flow<Int> = scanHistoryDao.observeCount()

    val currentUser: Flow<UserEntity?> = userDao.observeCurrent()

    suspend fun saveUser(name: String, email: String, password: String, city: String = "") =
        userDao.upsert(UserEntity(name = name, email = email, password = password, city = city))

    fun pet(id: String): Flow<PetUiModel?> =
        petDao.observeById(id)
            .map { it?.toUiModel() }
            .onStart { refreshInBackground { refreshPet(id) } }

    fun searchPets(query: String): Flow<List<PetUiModel>> =
        if (query.isBlank()) {
            allPets
        } else {
            // Searches the local cache; the list flows keep it fresh from the network.
            petDao.search(query).map { list -> list.map { it.toUiModel() } }
        }

    fun scanHistory(petId: String): Flow<List<ScanEvent>> =
        scanHistoryDao.observeForPet(petId)
            .map { list -> list.mapIndexed { index, e -> e.toScanEvent(isMostRecent = index == 0) } }
            .onStart { refreshInBackground { refreshScans(petId) } }

    /** The most recent scan for a pet — drives the "last scan location" map. */
    fun lastScan(petId: String): Flow<ScanEvent?> =
        scanHistoryDao.observeLatestForPet(petId)
            .map { it?.toScanEvent(isMostRecent = true) }
            .onStart { refreshInBackground { refreshScans(petId) } }

    /** Records a new scan locally (unsynced) and best-effort reports it upstream. */
    suspend fun logScan(
        petId: String,
        place: String,
        detail: String,
        latitude: Double?,
        longitude: Double?,
    ) {
        val timestamp = System.currentTimeMillis()
        scanHistoryDao.insert(
            ScanHistoryEntity(
                petId = petId,
                place = place,
                detail = detail,
                latitude = latitude,
                longitude = longitude,
                timestamp = timestamp,
                synced = false,
            ),
        )
        scanSyncService.reportScan(ScanReport(petId, place, latitude, longitude, timestamp))
    }

    /** Pushes any locally-recorded scans the backend hasn't seen (no-op offline). */
    suspend fun syncPendingScans(): Result<Unit> = scanSyncService.syncPendingScans()

    suspend fun getForm(id: String): PetFormData? = petDao.getById(id)?.toFormData()

    /** Inserts a new pet (starts as SAFE) and returns its id (server id when remote). */
    suspend fun addPet(form: PetFormData): String {
        if (remote) {
            val dto = runCatching { retryIO { petApi.createPet(form.toRequest()) } }.getOrNull()
            if (dto != null) {
                petDao.upsert(dto.toEntity())
                return dto.id
            }
            // Network unavailable: fall through to an optimistic local insert.
        }
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        petDao.upsert(form.toEntity(id, PetStatus.SAFE, createdAt = now, lastUpdated = now))
        return id
    }

    /** Updates editable fields, preserving the pet's status and creation time. */
    suspend fun updatePet(id: String, form: PetFormData) {
        if (remote) {
            val dto = runCatching { retryIO { petApi.updatePet(id, form.toRequest()) } }.getOrNull()
            if (dto != null) {
                petDao.upsert(dto.toEntity())
                return
            }
            // Network unavailable: fall through to an optimistic local update.
        }
        val existing = petDao.getById(id) ?: return
        petDao.upsert(
            form.toEntity(
                id = id,
                status = existing.status,
                createdAt = existing.createdAt,
                lastUpdated = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun deletePet(id: String) {
        if (remote) {
            runCatching { retryIO { petApi.deletePet(id) } }
        }
        petDao.deleteById(id)
    }

    suspend fun markLost(id: String) = setStatus(id, PetStatus.LOST)

    suspend fun markSafe(id: String) = setStatus(id, PetStatus.SAFE)

    suspend fun setStatus(id: String, status: PetStatus) {
        if (remote) {
            val dto = runCatching {
                retryIO { petApi.updateStatus(id, UpdateStatusRequest(status.name)) }
            }.getOrNull()
            if (dto != null) {
                petDao.upsert(dto.toEntity())
                return
            }
            // Network unavailable: fall through to an optimistic local status change.
        }
        petDao.updateStatus(id, status, System.currentTimeMillis())
    }

    // ── Network → cache refreshers ────────────────────────────────────────────

    private fun refreshInBackground(block: suspend () -> Unit) {
        if (!remote || !networkMonitor.isOnline()) return
        externalScope.launch { runCatching { block() } }
    }

    private suspend fun refreshAllPets() {
        val pets = retryIO { petApi.getPets() }
        petDao.insertAll(pets.map { it.toEntity() })
    }

    private suspend fun refreshLostPets() {
        val pets = retryIO { petApi.getLostPets() }
        petDao.insertAll(pets.map { it.toEntity() })
    }

    private suspend fun refreshPet(id: String) {
        val dto = runCatching { retryIO { petApi.getPet(id) } }.getOrNull()
            ?: runCatching { retryIO { petApi.getPublicPet(id) } }.getOrNull()
        if (dto != null) petDao.upsert(dto.toEntity())
    }

    private suspend fun refreshScans(petId: String) {
        val scans = retryIO { petApi.getScans(petId) }
        scanHistoryDao.deleteSyncedForPet(petId)
        scanHistoryDao.insertAll(scans.map { it.toEntity() })
    }

    /** Populates demo data on first run — local mode only; remote mode starts from the server. */
    suspend fun seedIfEmpty() {
        if (remote) return
        if (petDao.count() > 0) return

        val now = System.currentTimeMillis()
        fun daysAgo(d: Long) = now - TimeUnit.DAYS.toMillis(d)
        fun hoursAgo(h: Long) = now - TimeUnit.HOURS.toMillis(h)

        val dog = "https://images.unsplash.com/photo-1552053831-71594a27632d?w=800"
        val cat = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=800"
        val aussie = "https://images.unsplash.com/photo-1503256207526-0d5d80fa2f47?w=800"

        val luna = PetEntity(
            id = UUID.randomUUID().toString(),
            name = "Luna",
            species = "Dog",
            breed = "Golden Retriever",
            ageText = "3 Years Old",
            status = PetStatus.SAFE,
            imageUrl = dog,
            description = "Luna is an energetic and friendly golden retriever who loves belly " +
                "rubs and long walks in the park. She is fully vaccinated and microchipped.",
            ownerPhone = "+1 (555) 123-4567",
            ownerEmail = "owner@example.com",
            ownerCity = "San Francisco, CA",
            lastUpdated = daysAgo(2),
            createdAt = daysAgo(40),
        )
        val oliver = PetEntity(
            id = UUID.randomUUID().toString(),
            name = "Oliver",
            species = "Cat",
            breed = "Siamese Cat",
            ageText = "2 Years Old",
            status = PetStatus.LOST,
            imageUrl = cat,
            description = "Oliver is a curious Siamese cat with striking blue eyes. Shy around " +
                "strangers but warms up quickly with a few treats.",
            ownerPhone = "+1 (555) 987-6543",
            ownerEmail = "oliver.owner@example.com",
            ownerCity = "Seattle, WA",
            lastUpdated = daysAgo(13),
            createdAt = daysAgo(30),
        )
        val bluey = oliver.copy(
            id = UUID.randomUUID().toString(),
            name = "Bluey",
            species = "Dog",
            breed = "Australian Shepherd",
            ageText = "4 Years Old",
            imageUrl = aussie,
            ownerCity = "Portland, OR",
            lastUpdated = daysAgo(16),
        )
        val coco = oliver.copy(
            id = UUID.randomUUID().toString(),
            name = "Coco",
            species = "Dog",
            breed = "Pomeranian",
            ageText = "1 Year Old",
            imageUrl = dog,
            ownerCity = "San Francisco, CA",
            lastUpdated = daysAgo(12),
        )

        petDao.insertAll(listOf(luna, oliver, bluey, coco))

        scanHistoryDao.insertAll(
            listOf(
                ScanHistoryEntity(
                    petId = luna.id,
                    place = "Central Park, NY",
                    detail = "North Entrance",
                    scannedBy = "Good Samaritan",
                    note = "Contact shared via private chat",
                    showMap = true,
                    latitude = 40.7829,
                    longitude = -73.9654,
                    timestamp = hoursAgo(2),
                ),
                ScanHistoryEntity(
                    petId = luna.id,
                    place = "Brooklyn Bridge Park",
                    detail = "Pier 6",
                    scannedBy = null,
                    note = "Scan completed successfully. Your contact details were viewed.",
                    showMap = true,
                    latitude = 40.7024,
                    longitude = -73.9967,
                    timestamp = daysAgo(1),
                ),
                ScanHistoryEntity(
                    petId = luna.id,
                    place = "Home Terminal",
                    detail = "Setup Scan",
                    scannedBy = null,
                    note = null,
                    showMap = false,
                    timestamp = daysAgo(3),
                ),
            ),
        )
    }
}
