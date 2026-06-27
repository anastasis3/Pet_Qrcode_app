package com.petfinder.qr.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.petfinder.qr.database.dao.PetDao
import com.petfinder.qr.database.dao.ScanHistoryDao
import com.petfinder.qr.database.dao.UserDao
import com.petfinder.qr.database.entity.PetEntity
import com.petfinder.qr.database.entity.ScanHistoryEntity
import com.petfinder.qr.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PetEntity::class,
        ScanHistoryEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class PetFinderDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun userDao(): UserDao
    abstract fun scanHistoryDao(): ScanHistoryDao
}
