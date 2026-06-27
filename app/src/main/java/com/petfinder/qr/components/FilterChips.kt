package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Horizontally scrollable single-select filter chips (e.g. All Pets / Dogs /
 * Cats / Birds on the Lost Pets community screen).
 */
@Composable
fun FilterChipRow(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
    ) {
        options.forEachIndexed { index, label ->
            FilterChip(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                modifier = Modifier.padding(end = Spacing.xs),
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val container = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceContainerLowest
    }
    val content = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val borderColor = if (selected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant

    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = content,
        modifier = modifier
            .clip(ComponentShapes.chip)
            .background(container)
            .border(Dimensions.borderThin, borderColor, ComponentShapes.chip)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.md, vertical = Spacing.xs),
    )
}
