package com.petfinder.qr.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Fixed dimensions for components — keeps UI consistent across all screens.
 */
object Dimensions {
    // Corner radii
    val pillRadius: Dp = 9999.dp
    val buttonRadius: Dp = 16.dp
    val cardRadius: Dp = 16.dp
    val inputRadius: Dp = 12.dp
    val searchBarRadius: Dp = 12.dp
    val badgeRadius: Dp = 9999.dp
    val chipRadius: Dp = 9999.dp

    // Button heights
    val buttonHeightSmall: Dp = 48.dp
    val buttonHeightMedium: Dp = 56.dp
    val buttonHeightLarge: Dp = 64.dp

    // Icons
    val iconSizeSmall: Dp = 16.dp
    val iconSizeMedium: Dp = 20.dp
    val iconSizeDefault: Dp = 24.dp
    val iconSizeLarge: Dp = 32.dp
    val iconSizeXLarge: Dp = 48.dp
    val iconContainerSize: Dp = 48.dp

    // Top bar
    val topBarHeight: Dp = 64.dp
    val topBarIconButtonSize: Dp = 40.dp

    // Bottom navigation
    val bottomNavHeight: Dp = 72.dp
    val bottomNavIconSize: Dp = 24.dp

    // Cards
    val petCardImageHeight: Dp = 256.dp
    val petCardImageHeightCompact: Dp = 160.dp

    // FAB
    val fabSize: Dp = 96.dp
    val fabSizeSmall: Dp = 64.dp

    // Input fields
    val inputHeight: Dp = 56.dp
    val inputHeightCompact: Dp = 48.dp
    val searchBarHeight: Dp = 56.dp

    // Map preview
    val mapPreviewHeight: Dp = 256.dp
    val mapPreviewHeightCompact: Dp = 160.dp

    // QR code display
    val qrCodeSize: Dp = 256.dp

    // Avatar
    val avatarSizeSmall: Dp = 40.dp
    val avatarSizeMedium: Dp = 48.dp

    // Badge dot (lost indicator pulse)
    val statusDotSize: Dp = 8.dp

    // Elevation / shadow (used with Modifier.softShadow)
    val softShadowElevation: Dp = 4.dp

    // Border
    val borderThin: Dp = 1.dp
    val borderMedium: Dp = 2.dp

    // Loading indicator
    val loadingIndicatorSize: Dp = 48.dp

    // Empty state icon container
    val emptyStateIconContainer: Dp = 48.dp
}
