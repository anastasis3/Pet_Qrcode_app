package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Large soft-coloured call-to-action with the icon stacked above the label.
 * Used for "Call Owner", "Email Owner" and "Send My Location" on the public
 * recovery profile.
 */
@Composable
fun SoftActionButton(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(ComponentShapes.card)
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(Dimensions.iconSizeDefault),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = contentColor,
            modifier = Modifier.padding(top = Spacing.xs),
        )
    }
}
