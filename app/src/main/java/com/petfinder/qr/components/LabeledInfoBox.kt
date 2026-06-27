package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Spacing

/**
 * Rounded box showing a small grey label above a larger emphasised value.
 * Used for the Breed / Age / City facts on the public recovery profile.
 */
@Composable
fun LabeledInfoBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    valueColor: Color = MaterialTheme.colorScheme.primary,
) {
    Column(
        modifier = modifier
            .clip(ComponentShapes.card)
            .background(containerColor)
            .padding(Spacing.md),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = valueColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.xxs),
        )
    }
}
