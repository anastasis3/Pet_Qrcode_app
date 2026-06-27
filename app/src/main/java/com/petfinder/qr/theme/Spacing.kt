package com.petfinder.qr.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing scale derived from Stitch design tokens (8px base unit).
 */
object Spacing {
    val none: Dp = 0.dp
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 20.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val xxxl: Dp = 40.dp

    /** Base unit — 8px */
    val unit: Dp = 8.dp

    /** Horizontal gutter between columns — 16px */
    val gutter: Dp = 16.dp

    /** Vertical gap between stacked sections — 20px */
    val stackGap: Dp = 20.dp

    /** Screen horizontal padding — 24px */
    val containerPadding: Dp = 24.dp

    /** Large section separation — 40px */
    val sectionMargin: Dp = 40.dp

    /** Standard screen content padding */
    val screenPadding = PaddingValues(
        horizontal = containerPadding,
        vertical = unit,
    )

    /** Card internal padding */
    val cardPadding = PaddingValues(all = containerPadding)

    /** Bottom nav clearance above system bar */
    val bottomNavClearance: Dp = 110.dp
}
