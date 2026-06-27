package com.petfinder.qr.sync

import com.petfinder.qr.model.ScanReport
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
 * Future Cloudflare/Spring Boot implementation. To enable:
 *  - `reportScan` -> `POST /api/pets/{petId}/scans`, then mark the row synced.
 *  - `syncPendingScans` -> read `ScanHistoryDao.getUnsynced()`, push them, then
 *    `ScanHistoryDao.markSynced(ids)`.
 * Bind this in [com.petfinder.qr.di.SyncModule] instead of [LocalScanSyncService].
 */
class RemoteScanSyncService @Inject constructor(
    // e.g. api: PetFinderApiService, scanHistoryDao: ScanHistoryDao
) : ScanSyncService {
    override suspend fun reportScan(report: ScanReport): Result<Unit> =
        Result.failure(NotImplementedError("Remote scan sync not wired yet"))

    override suspend fun syncPendingScans(): Result<Unit> =
        Result.failure(NotImplementedError("Remote scan sync not wired yet"))
}
