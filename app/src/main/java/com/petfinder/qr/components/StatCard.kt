package com.petfinder.qr.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Summary tile used on the Home dashboard (e.g. "All Safe 2/2", "QR Scans 12").
 */
@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    containerColor: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(140.dp)
            .clip(ComponentShapes.card)
            .background(containerColor)
            .padding(Spacing.lg),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(Dimensions.iconSizeLarge),
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = iconTint,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = Spacing.xxs),
            )
        }
    }
}
