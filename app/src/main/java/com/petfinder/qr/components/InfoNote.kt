package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Small informational callout — an icon followed by a paragraph of helper text.
 */
@Composable
fun InfoNote(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = AppIcons.QrCode,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .background(containerColor)
            .padding(Spacing.md),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(Dimensions.iconSizeMedium),
        )
        Spacer(modifier = Modifier.size(Spacing.sm))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
        )
    }
}
