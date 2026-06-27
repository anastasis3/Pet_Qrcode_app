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

    @Query("SELECT COUNT(*) FROM scan_history")
    fun observeCount(): Flow<Int>

    @Insert
    suspend fun insert(event: ScanHistoryEntity)

    @Insert
    suspend fun insertAll(events: List<ScanHistoryEntity>)
}
