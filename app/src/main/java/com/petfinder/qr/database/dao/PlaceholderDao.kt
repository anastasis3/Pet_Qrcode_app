package com.petfinder.qr.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.petfinder.qr.database.entity.PlaceholderEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO interfaces will be defined here.
 * This placeholder will be replaced when real entities are added.
 */
@Dao
interface PlaceholderDao {

    @Query("SELECT * FROM placeholder LIMIT 1")
    fun observePlaceholder(): Flow<PlaceholderEntity?>
}
