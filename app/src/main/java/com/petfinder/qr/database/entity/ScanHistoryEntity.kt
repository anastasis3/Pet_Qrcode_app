package com.petfinder.qr.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scan_history",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("petId")],
)
data class ScanHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val petId: String,
    val place: String,
    val detail: String,
    val scannedBy: String? = null,
    val note: String? = null,
    val showMap: Boolean = true,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timestamp: Long,
    /** False until this scan has been pushed to the backend (future sync). */
    val synced: Boolean = true,
)
