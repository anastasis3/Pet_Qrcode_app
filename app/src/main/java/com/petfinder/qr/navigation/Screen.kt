package com.petfinder.qr.navigation

/**
 * Central route definitions for Navigation Compose.
 * Add new destinations here as screens are implemented.
 */
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object AddPet : Screen("add_pet")
    data object PetProfile : Screen("pet_profile/{petId}") {
        fun createRoute(petId: String) = "pet_profile/$petId"
    }
    data object QrCode : Screen("qr_code/{petId}") {
        fun createRoute(petId: String) = "qr_code/$petId"
    }
    data object LostPets : Screen("lost_pets")
    data object PublicPetProfile : Screen("public_pet/{petId}") {
        fun createRoute(petId: String) = "public_pet/$petId"
    }
    data object ScanHistory : Screen("scan_history/{petId}") {
        fun createRoute(petId: String) = "scan_history/$petId"
    }
    data object QrScanner : Screen("qr_scanner")
}
