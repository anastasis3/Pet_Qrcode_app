package com.petfinder.qr.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val PetFinderShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

object ComponentShapes {
    // Generic radii (mirror PetFinderShapes for use outside MaterialTheme.shapes).
    val extraSmall: Shape = RoundedCornerShape(8.dp)
    val small: Shape = RoundedCornerShape(12.dp)
    val medium: Shape = RoundedCornerShape(16.dp)
    val large: Shape = RoundedCornerShape(24.dp)

    val pill: Shape = RoundedCornerShape(Dimensions.pillRadius)
    val button: Shape = RoundedCornerShape(Dimensions.buttonRadius)
    val card: Shape = RoundedCornerShape(Dimensions.cardRadius)
    val input: Shape = RoundedCornerShape(Dimensions.inputRadius)
    val searchBar: Shape = RoundedCornerShape(Dimensions.searchBarRadius)
    val bottomNav: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val bottomNavItem: Shape = RoundedCornerShape(12.dp)
    val badge: Shape = RoundedCornerShape(Dimensions.badgeRadius)
    val iconContainer: Shape = RoundedCornerShape(12.dp)
    val fab: Shape = CircleShape
    val chip: Shape = RoundedCornerShape(Dimensions.chipRadius)
    val qrCard: Shape = RoundedCornerShape(24.dp)
    val mapPreview: Shape = RoundedCornerShape(Dimensions.cardRadius)
}
