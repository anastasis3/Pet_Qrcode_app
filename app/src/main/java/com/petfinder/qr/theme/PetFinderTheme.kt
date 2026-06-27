package com.petfinder.qr.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceVariant = SurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    inversePrimary = InversePrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryFixedDim,
    onPrimary = OnPrimaryFixed,
    primaryContainer = Primary,
    onPrimaryContainer = PrimaryFixed,
    secondary = SecondaryFixedDim,
    onSecondary = OnSecondaryFixed,
    secondaryContainer = Secondary,
    onSecondaryContainer = SecondaryFixed,
    tertiary = TertiaryFixedDim,
    tertiaryContainer = Tertiary,
    onTertiaryContainer = TertiaryFixed,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = InverseSurface,
    onBackground = InverseOnSurface,
    surface = InverseSurface,
    onSurface = InverseOnSurface,
    onSurfaceVariant = OutlineVariant,
    surfaceContainer = InverseSurface,
    surfaceContainerHigh = SurfaceContainerHighest,
    surfaceContainerHighest = SurfaceContainerHigh,
    surfaceContainerLow = SurfaceContainer,
    surfaceContainerLowest = SurfaceDim,
    surfaceVariant = SurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = Surface,
    inverseOnSurface = OnSurface,
    inversePrimary = Primary,
)

@Immutable
data class PetFinderExtraColors(
    val statusLost: androidx.compose.ui.graphics.Color = StatusLost,
    val inputBackground: androidx.compose.ui.graphics.Color = InputBackground,
    val cardBorder: androidx.compose.ui.graphics.Color = CardBorder,
    val searchBackground: androidx.compose.ui.graphics.Color = SearchBackground,
    val primaryGradientStart: androidx.compose.ui.graphics.Color = PrimaryButtonGradientStart,
    val primaryGradientEnd: androidx.compose.ui.graphics.Color = PrimaryButtonGradientEnd,
    val cardActionGradientStart: androidx.compose.ui.graphics.Color = CardActionGradientStart,
    val cardActionGradientEnd: androidx.compose.ui.graphics.Color = CardActionGradientEnd,
)

val LocalPetFinderExtraColors = staticCompositionLocalOf { PetFinderExtraColors() }

@Composable
fun petFinderExtraColors(): PetFinderExtraColors = LocalPetFinderExtraColors.current

@Composable
fun PetFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalPetFinderExtraColors provides PetFinderExtraColors()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PetFinderTypography,
            shapes = PetFinderShapes,
            content = content,
        )
    }
}
