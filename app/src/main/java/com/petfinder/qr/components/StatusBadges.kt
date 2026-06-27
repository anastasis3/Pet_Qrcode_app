package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.petFinderExtraColors

@Composable
fun StatusChip(
    status: PetStatus,
    modifier: Modifier = Modifier,
) {
    when (status) {
        PetStatus.SAFE -> SafeBadge(modifier = modifier, label = "Safe")
        PetStatus.LOST -> LostBadge(modifier = modifier, label = "Lost")
    }
}

@Composable
fun SafeBadge(
    modifier: Modifier = Modifier,
    label: String = "Safe at Home",
    showIcon: Boolean = true,
) {
    Box(
        modifier = modifier
            .clip(ComponentShapes.chip)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = Spacing.md, vertical = Spacing.xxs),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showIcon) {
                Icon(
                    imageVector = AppIcons.SafeFilled,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Box(modifier = Modifier.size(Spacing.xxs))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun LostBadge(
    modifier: Modifier = Modifier,
    label: String = "LOST",
    showPulseDot: Boolean = true,
    icon: ImageVector? = null,
) {
    Box(
        modifier = modifier
            .clip(ComponentShapes.chip)
            .background(petFinderExtraColors().statusLost)
            .padding(horizontal = Spacing.sm, vertical = Spacing.xxs),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(Dimensions.iconSizeSmall),
                    tint = MaterialTheme.colorScheme.error,
                )
                Box(modifier = Modifier.size(Spacing.xxs))
            } else if (showPulseDot) {
                Box(
                    modifier = Modifier
                        .size(Dimensions.statusDotSize)
                        .clip(ComponentShapes.chip)
                        .background(MaterialTheme.colorScheme.error),
                )
                Box(modifier = Modifier.size(Spacing.xs))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun StatusChipIcon(
    icon: ImageVector,
    label: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(ComponentShapes.chip)
            .background(containerColor)
            .padding(horizontal = Spacing.md, vertical = Spacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(Dimensions.iconSizeMedium),
                tint = contentColor,
            )
            Box(modifier = Modifier.size(Spacing.xxs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
            )
        }
    }
}
