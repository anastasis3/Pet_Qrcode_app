package com.petfinder.qr.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petfinder.qr.database.dao.PlaceholderDao
import com.petfinder.qr.database.entity.PlaceholderEntity

@Database(
    entities = [PlaceholderEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PetFinderDatabase : RoomDatabase() {
    abstract fun placeholderDao(): PlaceholderDao
}
