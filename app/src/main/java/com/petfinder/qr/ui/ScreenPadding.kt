package com.petfinder.qr.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.petfinder.qr.theme.Spacing

/** Standard horizontal screen padding used on every screen. */
fun Modifier.screenHorizontalPadding(): Modifier = padding(horizontal = Spacing.containerPadding)

/** Standard screen content padding (horizontal + top). */
fun Modifier.screenContentPadding(): Modifier = padding(Spacing.screenPadding)

/** Padding that clears the bottom navigation bar. */
fun Modifier.bottomNavPadding(): Modifier = padding(bottom = Spacing.bottomNavClearance)

/** Combined screen padding with bottom nav clearance. */
fun Modifier.mainScreenPadding(): Modifier = this
    .screenContentPadding()
    .bottomNavPadding()

val ScreenContentPadding: PaddingValues = Spacing.screenPadding
