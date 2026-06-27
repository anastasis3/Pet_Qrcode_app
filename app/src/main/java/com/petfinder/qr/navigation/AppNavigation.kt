package com.petfinder.qr.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
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
import com.petfinder.qr.screens.splash.SplashScreen

private const val ANIM_MS = 350

// ── Shared screen transitions ────────────────────────────────────────────────
private val slideEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_MS)) +
        fadeIn(tween(ANIM_MS))
}
private val slideExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_MS)) +
        fadeOut(tween(ANIM_MS))
}
private val slidePopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_MS)) +
        fadeIn(tween(ANIM_MS))
}
private val slidePopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_MS)) +
        fadeOut(tween(ANIM_MS))
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = slideEnter,
        exitTransition = slideExit,
        popEnterTransition = slidePopEnter,
        popExitTransition = slidePopExit,
    ) {
        // ── Splash ──────────────────────────────────────────────────────────
        composable(
            route = Screen.Splash.route,
            exitTransition = { fadeOut(tween(500)) },
        ) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
            )
        }

        // ── Auth ────────────────────────────────────────────────────────────
        composable(
            route = Screen.Login.route,
            enterTransition = { fadeIn(tween(500)) },
        ) {
            LoginScreen(
                onSignIn = { navController.navigateClearingAuth(Screen.Home.route) },
                onCreateAccount = { navController.navigate(Screen.Register.route) },
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onCreateAccount = { navController.navigateClearingAuth(Screen.Home.route) },
                onSignIn = { navController.popBackStack() },
            )
        }

        // ── Authenticated (bottom navigation visible) ───────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                onViewProfile = { navController.navigate(Screen.PetProfile.createRoute(it.id)) },
                onAddPet = { navController.navigate(Screen.AddPet.route) },
                onViewAll = { navController.navigate(Screen.LostPets.route) },
                onNavigate = navController::navigateToTab,
            )
        }

        composable(Screen.AddPet.route) {
            AddPetScreen(
                onSave = { navController.navigate(Screen.QrCode.createRoute("1")) },
                onNavigate = navController::navigateToTab,
            )
        }

        composable(Screen.PetProfile.route) {
            PetProfileScreen(
                onBack = { navController.popBackStack() },
                onViewQr = { navController.navigate(Screen.QrCode.createRoute("1")) },
                onEditInfo = { navController.navigate(Screen.AddPet.route) },
                onViewScanHistory = { navController.navigate(Screen.ScanHistory.createRoute("1")) },
                onNavigate = navController::navigateToTab,
            )
        }

        composable(Screen.QrCode.route) {
            QrCodeScreen(onNavigate = navController::navigateToTab)
        }

        composable(Screen.LostPets.route) {
            LostPetsScreen(
                onViewDetails = { navController.navigate(Screen.PublicPetProfile.createRoute(it.id)) },
                onAddPet = { navController.navigate(Screen.AddPet.route) },
                onNavigate = navController::navigateToTab,
            )
        }

        composable(Screen.ScanHistory.route) {
            ScanHistoryScreen(
                onBack = { navController.popBackStack() },
                onNavigate = navController::navigateToTab,
            )
        }

        // ── Public (no bottom navigation — reached by scanning a tag) ────────
        composable(Screen.PublicPetProfile.route) {
            PublicPetProfileScreen(onShare = {})
        }
    }
}

/** Navigates to a post-login destination and clears the auth screens from the back stack. */
private fun NavHostController.navigateClearingAuth(route: String) {
    navigate(route) {
        popUpTo(Screen.Login.route) { inclusive = true }
        launchSingleTop = true
    }
}

/** Bottom-navigation tab switch: keeps a single Home anchor and restores tab state. */
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
