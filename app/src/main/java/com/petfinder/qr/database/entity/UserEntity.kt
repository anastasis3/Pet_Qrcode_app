package com.petfinder.qr.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The locally-registered owner. A single row (id = 1) represents the current
 * user — no remote auth yet, purely offline.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val password: String,
    val city: String = "",
)
