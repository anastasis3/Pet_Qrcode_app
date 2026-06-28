package com.petfinder.qr.sync

import com.petfinder.qr.database.dao.ScanHistoryDao
import com.petfinder.qr.model.ScanReport
import com.petfinder.qr.network.PetApiService
import com.petfinder.qr.network.dto.LogScanRequest
import com.petfinder.qr.network.retryIO
import javax.inject.Inject

/**
 * The seam for future backend synchronization of scan locations. Today scans are
 * Room-only; this interface is where Spring Boot push/pull will plug in.
 *
 * The schema is already sync-ready: `ScanHistoryEntity.synced` marks rows that
 * still need pushing, and `ScanHistoryDao.getUnsynced()/markSynced()` drive it.
 */
interface ScanSyncService {
    /** Best-effort push of a single freshly-recorded scan. */
    suspend fun reportScan(report: ScanReport): Result<Unit>

    /** Pushes every locally-recorded scan that hasn't reached the backend yet. */
    suspend fun syncPendingScans(): Result<Unit>
}

/** Current behaviour: offline only. Local Room rows are the source of truth. */
class LocalScanSyncService @Inject constructor() : ScanSyncService {
    override suspend fun reportScan(report: ScanReport): Result<Unit> = Result.success(Unit)
    override suspend fun syncPendingScans(): Result<Unit> = Result.success(Unit)
}

/**
 * Spring Boot implementation: posts scans/locations to the backend and clears the
 * `synced` flag on success. Active when [com.petfinder.qr.utils.Constants.USE_REMOTE_BACKEND] is on.
 */
class RemoteScanSyncService @Inject constructor(
    private val petApi: PetApiService,
    private val scanHistoryDao: ScanHistoryDao,
) : ScanSyncService {

    override suspend fun reportScan(report: ScanReport): Result<Unit> = try {
        retryIO {
            petApi.logScan(
                report.petId,
                LogScanRequest(
                    place = report.place,
                    detail = "Tag scan",
                    latitude = report.latitude,
                    longitude = report.longitude,
                    timestamp = report.timestamp,
                ),
            )
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun syncPendingScans(): Result<Unit> = try {
        val pending = scanHistoryDao.getUnsynced()
        pending.forEach { scan ->
            retryIO {
                petApi.logScan(
                    scan.petId,
                    LogScanRequest(
                        place = scan.place,
                        detail = scan.detail,
                        latitude = scan.latitude,
                        longitude = scan.longitude,
                        timestamp = scan.timestamp,
                    ),
                )
            }
        }
        if (pending.isNotEmpty()) scanHistoryDao.markSynced(pending.map { it.id })
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
