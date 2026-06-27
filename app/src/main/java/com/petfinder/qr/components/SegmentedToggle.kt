package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

data class ToggleOption(
    val label: String,
    val icon: ImageVector? = null,
)

/**
 * Two-or-more option pill toggle. Used for the Species selector (Dog / Cat) on
 * Add Pet and the Pet Status selector (Safe / Lost) on Pet Profile.
 */
@Composable
fun SegmentedToggle(
    options: List<ToggleOption>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedContainerColor: Color = Color.Transparent,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.pill)
            .background(trackColor)
            .padding(Spacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            val interactionSource = remember { MutableInteractionSource() }
            val containerColor = if (selected) selectedContainerColor else unselectedContainerColor
            val contentColor = if (selected) selectedContentColor else unselectedContentColor

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(Dimensions.buttonHeightSmall)
                    .clip(ComponentShapes.pill)
                    .background(containerColor)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(),
                        role = Role.Tab,
                        onClick = { onSelect(index) },
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (option.icon != null) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                    )
                    Spacer(width = Spacing.xs)
                }
                Text(
                    text = option.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
private fun Spacer(width: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(width))
}
