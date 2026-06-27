package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.petFinderExtraColors
import com.petfinder.qr.theme.softShadow

/**
 * Community alert card on the Lost Pets screen: photo with a LOST badge, the
 * pet's name, location & "lost since" rows, and a "View Details" action.
 */
@Composable
fun LostPetCard(
    pet: LostPetUiModel,
    modifier: Modifier = Modifier,
    onViewDetails: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
) {
    val extraColors = petFinderExtraColors()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .border(Dimensions.borderThin, extraColors.cardBorder, ComponentShapes.card)
            .softShadow()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.petCardImageHeightCompact),
        ) {
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = pet.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.petCardImageHeightCompact)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            )
            LostBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Spacing.sm),
            )
        }

        Column(modifier = Modifier.padding(Spacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(Dimensions.topBarIconButtonSize),
                ) {
                    Icon(
                        imageVector = if (pet.isFavorite) AppIcons.FavoriteFilled else AppIcons.Favorite,
                        contentDescription = "Favorite",
                        tint = if (pet.isFavorite) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    )
                }
            }

            IconTextRow(icon = AppIcons.Location, text = pet.location)
            Spacer(modifier = Modifier.size(Spacing.xxs))
            IconTextRow(icon = AppIcons.Calendar, text = pet.lostSince)

            Spacer(modifier = Modifier.size(Spacing.md))

            PrimaryButton(
                text = "View Details",
                onClick = onViewDetails,
                icon = AppIcons.Forward,
                useGradient = true,
            )
        }
    }
}

@Composable
private fun IconTextRow(
    icon: ImageVector,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Dimensions.iconSizeSmall),
        )
        Spacer(modifier = Modifier.size(Spacing.xxs))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
