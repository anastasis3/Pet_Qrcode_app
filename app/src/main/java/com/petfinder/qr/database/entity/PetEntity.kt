package com.petfinder.qr.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.petfinder.qr.model.PetStatus

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val species: String,
    val breed: String,
    val ageText: String,
    val status: PetStatus,
    val imageUrl: String?,
    val description: String,
    val ownerPhone: String,
    val ownerEmail: String,
    val ownerCity: String,
    val lastUpdated: Long,
    val createdAt: Long,
)
