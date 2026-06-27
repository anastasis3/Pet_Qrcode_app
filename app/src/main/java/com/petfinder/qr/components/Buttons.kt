package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.petFinderExtraColors

@Composable
internal fun PetFinderButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color? = null,
    gradient: Brush? = null,
    height: Dp = Dimensions.buttonHeightMedium,
    shape: Shape = ComponentShapes.pill,
    fillMaxWidth: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundModifier = if (gradient != null) {
        Modifier.background(gradient)
    } else {
        Modifier.background(containerColor)
    }

    Box(
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .height(height)
            .clip(shape)
            .then(
                if (borderColor != null) {
                    Modifier.border(Dimensions.borderMedium, borderColor, shape)
                } else {
                    Modifier
                },
            )
            .then(backgroundModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = contentColor.copy(alpha = 0.2f)),
                enabled = enabled && !isLoading,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimensions.iconSizeDefault),
                color = contentColor,
                strokeWidth = Dimensions.borderMedium,
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = Spacing.xl),
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(Dimensions.iconSizeDefault),
                        tint = contentColor,
                    )
                    Box(modifier = Modifier.size(Spacing.sm))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                )
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    useGradient: Boolean = true,
    fillMaxWidth: Boolean = true,
    height: Dp = Dimensions.buttonHeightLarge,
) {
    val extraColors = petFinderExtraColors()
    val gradient = if (useGradient) {
        Brush.verticalGradient(
            colors = listOf(
                extraColors.primaryGradientStart,
                extraColors.primaryGradientEnd,
            ),
        )
    } else {
        null
    }
    // Gradient buttons are light blue → dark text; solid buttons are dark navy → white text.
    val contentColor = if (useGradient) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    PetFinderButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        icon = icon,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = contentColor,
        gradient = gradient,
        height = height,
        fillMaxWidth = fillMaxWidth,
    )
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    fillMaxWidth: Boolean = true,
) {
    PetFinderButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        icon = icon,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = MaterialTheme.colorScheme.secondary,
        borderColor = MaterialTheme.colorScheme.secondaryContainer,
        height = Dimensions.buttonHeightMedium,
        shape = ComponentShapes.button,
        fillMaxWidth = fillMaxWidth,
    )
}

@Composable
fun DangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    fillMaxWidth: Boolean = true,
) {
    PetFinderButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        icon = icon,
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        height = Dimensions.buttonHeightMedium,
        fillMaxWidth = fillMaxWidth,
    )
}
