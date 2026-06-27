package com.petfinder.qr.model

/**
 * A scan event ready to be reported to the backend. This is the payload the
 * future [com.petfinder.qr.sync.ScanSyncService] will push to Spring Boot.
 */
data class ScanReport(
    val petId: String,
    val place: String,
    val latitude: Double?,
    val longitude: Double?,
    val timestamp: Long,
)
