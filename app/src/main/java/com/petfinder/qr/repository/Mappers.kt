package com.petfinder.qr.repository

import com.petfinder.qr.database.entity.PetEntity
import com.petfinder.qr.database.entity.ScanHistoryEntity
import com.petfinder.qr.model.ContactInfo
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.network.dto.PetDto
import com.petfinder.qr.network.dto.PetRequest
import com.petfinder.qr.network.dto.ScanDto
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

internal fun Long.formatAsDate(): String = toLocalDate().format(dateFormatter)

// ── Pet ───────────────────────────────────────────────────────────────────────

fun PetEntity.toUiModel(): PetUiModel = PetUiModel(
    id = id,
    name = name,
    breed = breed,
    species = species,
    ageText = ageText,
    status = status,
    imageUrl = imageUrl,
    description = description,
    lastUpdate = lastUpdated.formatAsDate(),
    contact = ContactInfo(phone = ownerPhone, email = ownerEmail, city = ownerCity),
)

fun PetEntity.toLostUiModel(): LostPetUiModel = LostPetUiModel(
    id = id,
    name = name,
    location = ownerCity,
    lostSince = "Lost since ${lastUpdated.formatAsDate()}",
    imageUrl = imageUrl,
)

fun PetEntity.toFormData(): PetFormData = PetFormData(
    name = name,
    species = species,
    breed = breed,
    age = ageText,
    description = description,
    phone = ownerPhone,
    email = ownerEmail,
    city = ownerCity,
    imageUrl = imageUrl,
)

fun PetFormData.toEntity(
    id: String,
    status: PetStatus,
    createdAt: Long,
    lastUpdated: Long,
): PetEntity = PetEntity(
    id = id,
    name = name.ifBlank { "Unnamed" },
    species = species,
    breed = breed,
    ageText = age,
    status = status,
    imageUrl = imageUrl,
    description = description,
    ownerPhone = phone,
    ownerEmail = email,
    ownerCity = city,
    lastUpdated = lastUpdated,
    createdAt = createdAt,
)

// ── Pet: network DTO ↔ entity ─────────────────────────────────────────────────

fun PetDto.toEntity(): PetEntity = PetEntity(
    id = id,
    name = name,
    species = species,
    breed = breed,
    ageText = ageText,
    status = runCatching { PetStatus.valueOf(status.uppercase()) }.getOrDefault(PetStatus.SAFE),
    imageUrl = imageUrl,
    description = description,
    ownerPhone = ownerPhone,
    ownerEmail = ownerEmail,
    ownerCity = ownerCity,
    lastUpdated = lastUpdated,
    createdAt = createdAt,
)

fun PetFormData.toRequest(): PetRequest = PetRequest(
    name = name.ifBlank { "Unnamed" },
    species = species,
    breed = breed,
    ageText = age,
    description = description,
    ownerPhone = phone,
    ownerEmail = email,
    ownerCity = city,
    imageUrl = imageUrl,
)

// ── Scan history ──────────────────────────────────────────────────────────────

fun ScanDto.toEntity(): ScanHistoryEntity = ScanHistoryEntity(
    id = id,
    petId = petId,
    place = place,
    detail = detail,
    scannedBy = scannedBy,
    note = note,
    showMap = showMap,
    latitude = latitude,
    longitude = longitude,
    timestamp = timestamp,
    synced = true,
)


fun ScanHistoryEntity.toScanEvent(isMostRecent: Boolean): ScanEvent {
    val zoned = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
    val date = zoned.toLocalDate()
    val today = LocalDate.now()
    val label = when (date) {
        today -> "TODAY"
        today.minusDays(1) -> "YESTERDAY"
        else -> date.format(dateFormatter).uppercase()
    }
    return ScanEvent(
        id = id.toString(),
        dateLabel = label,
        place = place,
        timeDetail = "${zoned.format(timeFormatter)} • $detail",
        isNew = isMostRecent,
        showMap = showMap,
        scannedBy = scannedBy,
        note = note,
        isActive = isMostRecent,
        latitude = latitude,
        longitude = longitude,
    )
}
