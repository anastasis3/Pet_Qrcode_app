package com.petfinder.qr.model

/**
 * Editable form values for adding / editing a pet. Kept separate from the
 * persistence entity so the UI never touches Room types directly.
 */
data class PetFormData(
    val name: String = "",
    val species: String = "Dog",
    val breed: String = "",
    val age: String = "Puppy / Kitten",
    val description: String = "",
    val phone: String = "",
    val email: String = "",
    val city: String = "",
    val imageUrl: String? = null,
)
