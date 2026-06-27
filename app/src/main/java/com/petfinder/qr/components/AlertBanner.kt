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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * Full-width attention banner (e.g. the red "If I'm lost, please contact my
 * owner." strip on the public recovery profile).
 */
@Composable
fun AlertBanner(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = AppIcons.WarningOutlined,
    containerColor: Color = MaterialTheme.colorScheme.errorContainer,
    contentColor: Color = MaterialTheme.colorScheme.onErrorContainer,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.pill)
            .background(containerColor)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(Dimensions.iconSizeMedium),
        )
        Spacer(modifier = Modifier.size(Spacing.xs))
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = contentColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
