package com.petfinder.qr.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Soft drop shadow matching Stitch mockups:
 * `box-shadow: 0px 0px 12px 0px rgba(0, 0, 0, 0.04)`
 */
fun Modifier.softShadow(
    color: Color = Color.Black.copy(alpha = 0.04f),
    blurRadius: Dp = 12.dp,
    offsetY: Dp = 0.dp,
): Modifier = this.drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = Color.Transparent.toArgb()
        frameworkPaint.setShadowLayer(
            blurRadius.toPx(),
            0f,
            offsetY.toPx(),
            color.toArgb(),
        )
        canvas.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = 0f,
            radiusY = 0f,
            paint = paint,
        )
    }
}
