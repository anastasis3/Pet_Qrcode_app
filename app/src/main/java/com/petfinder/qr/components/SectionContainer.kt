package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Rounded grouping container with an optional icon + title header.
 * Used for "Owner Contact" (Add Pet) and "Pet Status" (Pet Profile).
 */
@Composable
fun SectionContainer(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleIcon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .background(containerColor)
            .padding(Spacing.lg),
    ) {
        if (title != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (titleIcon != null) {
                    Icon(
                        imageVector = titleIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimensions.iconSizeDefault),
                    )
                    Spacer(modifier = Modifier.size(Spacing.xs))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = Modifier.size(Spacing.md))
        }
        content()
    }
}
