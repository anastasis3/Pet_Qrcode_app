package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.petFinderExtraColors

/**
 * Read-only field that mimics a dropdown — a value plus a trailing chevron.
 * (No menu logic; selection handled by the host screen.)
 */
@Composable
fun DropdownField(
    value: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    backgroundColor: Color? = null,
    onClick: () -> Unit = {},
) {
    val fill = backgroundColor ?: petFinderExtraColors().inputBackground

    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = Spacing.xxs, bottom = Spacing.xs),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.inputHeight)
                .clip(ComponentShapes.pill)
                .background(fill)
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = AppIcons.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
