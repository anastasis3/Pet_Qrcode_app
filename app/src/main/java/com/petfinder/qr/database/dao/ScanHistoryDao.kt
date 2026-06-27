package com.petfinder.qr.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.petfinder.qr.database.entity.ScanHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history WHERE petId = :petId ORDER BY timestamp DESC")
    fun observeForPet(petId: String): Flow<List<ScanHistoryEntity>>

    @Query("SELECT * FROM scan_history WHERE petId = :petId ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestForPet(petId: String): Flow<ScanHistoryEntity?>

    @Query("SELECT COUNT(*) FROM scan_history")
    fun observeCount(): Flow<Int>

    // ── Backend-sync support (currently unused; ready for ScanSyncService) ──────
    @Query("SELECT * FROM scan_history WHERE synced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsynced(): List<ScanHistoryEntity>

    @Query("UPDATE scan_history SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)

    @Insert
    suspend fun insert(event: ScanHistoryEntity)

    @Insert
    suspend fun insertAll(events: List<ScanHistoryEntity>)
}
