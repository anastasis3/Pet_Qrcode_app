package com.petfinder.qr.preview

import com.petfinder.qr.model.ContactInfo
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent

/**
 * Fake, hard-coded data used only to render the UI and Compose @Preview functions.
 * No persistence, no network — purely for visual layout.
 */
object SampleData {

    private const val DOG_IMAGE =
        "https://images.unsplash.com/photo-1552053831-71594a27632d?w=800"
    private const val CAT_IMAGE =
        "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=800"

    val luna = PetUiModel(
        id = "1",
        name = "Luna",
        breed = "Golden Retriever",
        species = "Dog",
        ageText = "3 Years Old",
        status = PetStatus.SAFE,
        imageUrl = DOG_IMAGE,
        description = "Buddy is an energetic and friendly golden retriever who loves belly " +
            "rubs and long walks in the park. He is fully vaccinated and microchipped. " +
            "Very friendly with children and other pets.",
        lastUpdate = "Oct 24, 2023",
        contact = ContactInfo(
            phone = "+1 (555) 123-4567",
            email = "owner@example.com",
            city = "San Francisco, CA",
        ),
    )

    val oliver = PetUiModel(
        id = "2",
        name = "Oliver",
        breed = "Siamese Cat",
        species = "Cat",
        ageText = "2 Years Old",
        status = PetStatus.LOST,
        imageUrl = CAT_IMAGE,
        description = "Oliver is a curious Siamese cat with striking blue eyes. He is shy " +
            "around strangers but warms up quickly with a few treats.",
        lastUpdate = "Oct 14, 2023",
        contact = ContactInfo(
            phone = "+1 (555) 987-6543",
            email = "oliver.owner@example.com",
            city = "Seattle, WA",
        ),
    )

    val myPets: List<PetUiModel> = listOf(luna, oliver)

    val buddyProfile = luna.copy(name = "Buddy")

    val lostPets: List<LostPetUiModel> = listOf(
        LostPetUiModel("1", "Luna", "Austin, TX", "Lost since Oct 12, 2023", DOG_IMAGE),
        LostPetUiModel("2", "Oliver", "Seattle, WA", "Lost since Oct 14, 2023", CAT_IMAGE),
        LostPetUiModel("3", "Bluey", "Portland, OR", "Lost since Oct 11, 2023", DOG_IMAGE),
        LostPetUiModel("4", "Coco", "San Francisco, CA", "Lost since Oct 15, 2023", DOG_IMAGE),
        LostPetUiModel("5", "Mochi", "Miami, FL", "Lost since Oct 09, 2023", CAT_IMAGE),
        LostPetUiModel("6", "Buddy", "Denver, CO", "Lost since Oct 10, 2023", DOG_IMAGE),
    )

    val scanHistory: List<ScanEvent> = listOf(
        ScanEvent(
            id = "1",
            dateLabel = "TODAY",
            place = "Central Park, NY",
            timeDetail = "02:45 PM • North Entrance",
            isNew = true,
            showMap = true,
            scannedBy = "Good Samaritan",
            note = "Contact shared via private chat",
            isActive = true,
        ),
        ScanEvent(
            id = "2",
            dateLabel = "YESTERDAY",
            place = "Brooklyn Bridge Park",
            timeDetail = "09:12 AM • Pier 6",
            showMap = true,
            note = "Scan completed successfully. Your contact details were viewed.",
        ),
        ScanEvent(
            id = "3",
            dateLabel = "OCT 24, 2023",
            place = "Home Terminal",
            timeDetail = "08:00 PM • Setup Scan",
            showMap = false,
        ),
    )
}
