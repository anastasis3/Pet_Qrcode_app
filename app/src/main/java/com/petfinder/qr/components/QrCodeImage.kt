package com.petfinder.qr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.petfinder.qr.qr.QrCodeGenerator
import com.petfinder.qr.theme.Dimensions

/**
 * Renders a QR code for [content]. The bitmap is generated once per
 * content/size pair and cached via [remember].
 */
@Composable
fun QrCodeImage(
    content: String,
    modifier: Modifier = Modifier,
    size: Dp = Dimensions.qrCodeSize,
) {
    val sizePx = with(LocalDensity.current) { size.roundToPx() }
    val bitmap = remember(content, sizePx) { QrCodeGenerator.generate(content, sizePx) }

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "QR code",
            modifier = modifier.size(size),
        )
    } else {
        Box(
            modifier = modifier.size(size),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Unable to generate QR",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
