package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

/**
 * Circular floating action button with an icon and an optional caption beneath
 * it (the coral "Add Pet" FAB on the Home dashboard).
 */
@Composable
fun PetFinderFab(
    modifier: Modifier = Modifier,
    icon: ImageVector = AppIcons.AddFilled,
    label: String? = null,
    size: Dp = Dimensions.fabSize,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .softShadow()
            .background(containerColor)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(Dimensions.iconSizeLarge),
        )
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                modifier = Modifier.padding(top = Spacing.xxs),
            )
        }
    }
}
