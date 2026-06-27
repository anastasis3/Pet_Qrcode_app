package com.petfinder.qr.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.petfinder.qr.database.entity.PetEntity
import com.petfinder.qr.model.PetStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE status = 'LOST' ORDER BY lastUpdated DESC")
    fun observeLost(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun observeById(id: String): Flow<PetEntity?>

    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getById(id: String): PetEntity?

    @Query(
        """
        SELECT * FROM pets
        WHERE name LIKE '%' || :query || '%'
           OR breed LIKE '%' || :query || '%'
           OR ownerCity LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
        """,
    )
    fun search(query: String): Flow<List<PetEntity>>

    @Query("SELECT COUNT(*) FROM pets")
    suspend fun count(): Int

    @Upsert
    suspend fun upsert(pet: PetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pets: List<PetEntity>)

    @Query("UPDATE pets SET status = :status, lastUpdated = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: String, status: PetStatus, updatedAt: Long)

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun deleteById(id: String)
}
