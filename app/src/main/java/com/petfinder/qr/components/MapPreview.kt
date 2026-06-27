package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.MapPreviewBlue
import com.petfinder.qr.theme.MapPreviewGreen
import com.petfinder.qr.theme.MapPreviewSand
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

/**
 * Stylised map placeholder (no real map SDK / network). Renders a soft gradient
 * "map", an optional centre pin and an optional corner chip such as "VIEW MAP".
 */
@Composable
fun MapPreview(
    modifier: Modifier = Modifier,
    height: Dp = Dimensions.mapPreviewHeightCompact,
    showPin: Boolean = true,
    chipText: String? = null,
    chipIcon: ImageVector? = null,
    chipAlignment: Alignment = Alignment.BottomEnd,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(ComponentShapes.mapPreview)
            .background(
                Brush.linearGradient(
                    colors = listOf(MapPreviewBlue, MapPreviewGreen, MapPreviewSand),
                ),
            ),
    ) {
        if (showPin) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(Dimensions.iconContainerSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AppIcons.LocationFilled,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimensions.iconSizeDefault),
                )
            }
        }

        if (chipText != null) {
            Row(
                modifier = Modifier
                    .align(chipAlignment)
                    .padding(Spacing.sm)
                    .clip(ComponentShapes.pill)
                    .softShadow()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (chipIcon != null) {
                    Icon(
                        imageVector = chipIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimensions.iconSizeSmall),
                    )
                    Spacer(modifier = Modifier.size(Spacing.xxs))
                }
                Text(
                    text = chipText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
