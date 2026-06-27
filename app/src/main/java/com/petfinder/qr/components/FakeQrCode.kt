package com.petfinder.qr.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions

/**
 * A purely decorative, deterministic QR-looking pattern drawn with Canvas.
 * This is a UI placeholder — it does NOT encode any real data.
 */
@Composable
fun FakeQrCode(
    modifier: Modifier = Modifier,
    size: Dp = Dimensions.qrCodeSize,
    moduleColor: Color = Color(0xFF1B1C1A),
    backgroundColor: Color = Color.White,
) {
    val modules = 25

    Box(
        modifier = modifier
            .size(size)
            .clip(ComponentShapes.small)
            .background(backgroundColor)
            .padding(12.dp),
    ) {
        Canvas(modifier = Modifier.size(size - 24.dp)) {
            val cell = this.size.width / modules

            fun fill(x: Int, y: Int) {
                drawRect(
                    color = moduleColor,
                    topLeft = Offset(x * cell, y * cell),
                    size = Size(cell, cell),
                )
            }

            // Three finder patterns (top-left, top-right, bottom-left).
            fun finder(originX: Int, originY: Int) {
                for (dx in 0 until 7) {
                    for (dy in 0 until 7) {
                        val onBorder = dx == 0 || dx == 6 || dy == 0 || dy == 6
                        val innerCore = dx in 2..4 && dy in 2..4
                        if (onBorder || innerCore) fill(originX + dx, originY + dy)
                    }
                }
            }
            finder(0, 0)
            finder(modules - 7, 0)
            finder(0, modules - 7)

            // Pseudo-random data modules, skipping the finder zones.
            for (x in 0 until modules) {
                for (y in 0 until modules) {
                    val inFinder = (x < 8 && y < 8) ||
                        (x > modules - 9 && y < 8) ||
                        (x < 8 && y > modules - 9)
                    if (inFinder) continue
                    if ((x * 7 + y * 13 + x * y) % 3 == 0) fill(x, y)
                }
            }
        }
    }
}
