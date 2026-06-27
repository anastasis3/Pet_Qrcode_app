package com.petfinder.qr.repository

import com.petfinder.qr.database.dao.PetDao
import com.petfinder.qr.database.dao.ScanHistoryDao
import com.petfinder.qr.database.dao.UserDao
import com.petfinder.qr.database.entity.PetEntity
import com.petfinder.qr.database.entity.ScanHistoryEntity
import com.petfinder.qr.database.entity.UserEntity
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for pet data. Backed entirely by Room — works fully
 * offline. ViewModels talk only to this class, never to the DAOs directly.
 */
@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao,
    private val scanHistoryDao: ScanHistoryDao,
    private val userDao: UserDao,
) {
    val allPets: Flow<List<PetUiModel>> =
        petDao.observeAll().map { list -> list.map { it.toUiModel() } }

    val lostPets: Flow<List<LostPetUiModel>> =
        petDao.observeLost().map { list -> list.map { it.toLostUiModel() } }

    val scanCount: Flow<Int> = scanHistoryDao.observeCount()

    val currentUser: Flow<UserEntity?> = userDao.observeCurrent()

    /** Stores the registered owner locally (no remote auth). */
    suspend fun saveUser(name: String, email: String, password: String, city: String = "") =
        userDao.upsert(UserEntity(name = name, email = email, password = password, city = city))

    fun pet(id: String): Flow<PetUiModel?> =
        petDao.observeById(id).map { it?.toUiModel() }

    fun searchPets(query: String): Flow<List<PetUiModel>> =
        if (query.isBlank()) {
            allPets
        } else {
            petDao.search(query).map { list -> list.map { it.toUiModel() } }
        }

    fun scanHistory(petId: String): Flow<List<ScanEvent>> =
        scanHistoryDao.observeForPet(petId).map { list ->
            list.mapIndexed { index, entity -> entity.toScanEvent(isMostRecent = index == 0) }
        }

    suspend fun getForm(id: String): PetFormData? = petDao.getById(id)?.toFormData()

    /** Inserts a new pet (starts as SAFE) and returns its generated id. */
    suspend fun addPet(form: PetFormData): String {
        val now = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        petDao.upsert(form.toEntity(id, PetStatus.SAFE, createdAt = now, lastUpdated = now))
        return id
    }

    /** Updates editable fields, preserving the pet's status and creation time. */
    suspend fun updatePet(id: String, form: PetFormData) {
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

    suspend fun deletePet(id: String) = petDao.deleteById(id)

    suspend fun markLost(id: String) =
        petDao.updateStatus(id, PetStatus.LOST, System.currentTimeMillis())

    suspend fun markSafe(id: String) =
        petDao.updateStatus(id, PetStatus.SAFE, System.currentTimeMillis())

    suspend fun setStatus(id: String, status: PetStatus) =
        petDao.updateStatus(id, status, System.currentTimeMillis())

    /** Populates demo data the first time the app runs so the UI isn't empty offline. */
    suspend fun seedIfEmpty() {
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
                    timestamp = hoursAgo(2),
                ),
                ScanHistoryEntity(
                    petId = luna.id,
                    place = "Brooklyn Bridge Park",
                    detail = "Pier 6",
                    scannedBy = null,
                    note = "Scan completed successfully. Your contact details were viewed.",
                    showMap = true,
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
