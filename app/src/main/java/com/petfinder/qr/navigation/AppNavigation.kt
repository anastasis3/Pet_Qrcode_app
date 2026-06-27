package com.petfinder.qr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.screens.addpet.AddPetScreen
import com.petfinder.qr.screens.home.HomeScreen
import com.petfinder.qr.screens.login.LoginScreen
import com.petfinder.qr.screens.lostpets.LostPetsScreen
import com.petfinder.qr.screens.petprofile.PetProfileScreen
import com.petfinder.qr.screens.publicprofile.PublicPetProfileScreen
import com.petfinder.qr.screens.qrcode.QrCodeScreen
import com.petfinder.qr.screens.register.RegisterScreen
import com.petfinder.qr.screens.scanhistory.ScanHistoryScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onSignIn = { navController.navigate(Screen.Home.route) },
                onCreateAccount = { navController.navigate(Screen.Register.route) },
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onCreateAccount = { navController.navigate(Screen.Home.route) },
                onSignIn = { navController.popBackStack() },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onViewProfile = { navController.navigate(Screen.PetProfile.createRoute(it.id)) },
                onAddPet = { navController.navigate(Screen.AddPet.route) },
                onViewAll = { navController.navigate(Screen.LostPets.route) },
                onNavigate = { navController.navigateToTab(it) },
            )
        }

        composable(Screen.AddPet.route) {
            AddPetScreen(
                onSave = { navController.navigate(Screen.QrCode.createRoute("1")) },
                onNavigate = { navController.navigateToTab(it) },
            )
        }

        composable(Screen.PetProfile.route) {
            PetProfileScreen(
                onBack = { navController.popBackStack() },
                onViewQr = { navController.navigate(Screen.QrCode.createRoute("1")) },
                onEditInfo = { navController.navigate(Screen.AddPet.route) },
                onNavigate = { navController.navigateToTab(it) },
            )
        }

        composable(Screen.QrCode.route) {
            QrCodeScreen(
                onNavigate = { navController.navigateToTab(it) },
            )
        }

        composable(Screen.LostPets.route) {
            LostPetsScreen(
                onViewDetails = { navController.navigate(Screen.PublicPetProfile.createRoute(it.id)) },
                onAddPet = { navController.navigate(Screen.AddPet.route) },
                onNavigate = { navController.navigateToTab(it) },
            )
        }

        composable(Screen.PublicPetProfile.route) {
            PublicPetProfileScreen()
        }

        composable(Screen.ScanHistory.route) {
            ScanHistoryScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigateToTab(it) },
            )
        }
    }
}

/** Maps a bottom-navigation tab to its route and navigates without stacking duplicates. */
private fun NavHostController.navigateToTab(destination: BottomNavDestination) {
    val route = when (destination) {
        BottomNavDestination.Home -> Screen.Home.route
        BottomNavDestination.AddPet -> Screen.AddPet.route
        BottomNavDestination.LostPets -> Screen.LostPets.route
    }
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(Screen.Home.route) { saveState = true }
    }
}
