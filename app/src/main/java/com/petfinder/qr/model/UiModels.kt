package com.petfinder.qr.model

/**
 * Lightweight, display-only models used to feed the UI and @Preview functions.
 * These intentionally contain no business logic — they only hold formatted,
 * presentation-ready values.
 */

data class ContactInfo(
    val phone: String,
    val email: String,
    val city: String,
)

data class PetUiModel(
    val id: String,
    val name: String,
    val breed: String,
    val species: String,
    val ageText: String,
    val status: PetStatus,
    val imageUrl: String?,
    val description: String,
    val lastUpdate: String,
    val contact: ContactInfo,
)

data class LostPetUiModel(
    val id: String,
    val name: String,
    val location: String,
    val lostSince: String,
    val imageUrl: String?,
    val isFavorite: Boolean = false,
)

data class ScanEvent(
    val id: String,
    val dateLabel: String,
    val place: String,
    val timeDetail: String,
    val isNew: Boolean = false,
    val showMap: Boolean = true,
    val scannedBy: String? = null,
    val note: String? = null,
    val isActive: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
) {
    val hasLocation: Boolean get() = latitude != null && longitude != null
}
