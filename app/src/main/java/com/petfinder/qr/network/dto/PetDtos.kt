package com.petfinder.qr.network.dto

import com.google.gson.annotations.SerializedName

/** Spring Boot pet representation. */
data class PetDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("species") val species: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("ageText") val ageText: String,
    @SerializedName("status") val status: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("description") val description: String,
    @SerializedName("ownerPhone") val ownerPhone: String,
    @SerializedName("ownerEmail") val ownerEmail: String,
    @SerializedName("ownerCity") val ownerCity: String,
    @SerializedName("lastUpdated") val lastUpdated: Long,
    @SerializedName("createdAt") val createdAt: Long,
)

/** Body for create/update — server assigns id/timestamps. */
data class PetRequest(
    @SerializedName("name") val name: String,
    @SerializedName("species") val species: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("ageText") val ageText: String,
    @SerializedName("description") val description: String,
    @SerializedName("ownerPhone") val ownerPhone: String,
    @SerializedName("ownerEmail") val ownerEmail: String,
    @SerializedName("ownerCity") val ownerCity: String,
    @SerializedName("imageUrl") val imageUrl: String?,
)

data class UpdateStatusRequest(
    @SerializedName("status") val status: String,
)

data class ScanDto(
    @SerializedName("id") val id: Long,
    @SerializedName("petId") val petId: String,
    @SerializedName("place") val place: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("scannedBy") val scannedBy: String?,
    @SerializedName("note") val note: String?,
    @SerializedName("showMap") val showMap: Boolean,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("timestamp") val timestamp: Long,
)

/** Body for logging a scan / uploading a location. */
data class LogScanRequest(
    @SerializedName("place") val place: String,
    @SerializedName("detail") val detail: String,
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("timestamp") val timestamp: Long,
)

data class ImageUploadResponse(
    @SerializedName("url") val url: String,
)
