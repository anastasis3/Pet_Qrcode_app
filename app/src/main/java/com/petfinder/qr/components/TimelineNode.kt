package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing

/**
 * A single timeline row: a left rail (dot + connecting line) and the supplied
 * content on the right. Used by the Scan History screen.
 */
@Composable
fun TimelineNode(
    isActive: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .padding(bottom = Spacing.lg),
    ) {
        Column(
            modifier = Modifier.width(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val dotColor = if (isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                modifier = Modifier
                    .padding(top = Spacing.xs)
                    .size(if (isActive) 16.dp else 10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .then(
                        if (isActive) {
                            Modifier.border(3.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        } else {
                            Modifier
                        },
                    ),
            )
            if (!isLast) {
                Spacer(modifier = Modifier.size(Spacing.xs))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                )
            }
        }
        Spacer(modifier = Modifier.size(Spacing.md))
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}
