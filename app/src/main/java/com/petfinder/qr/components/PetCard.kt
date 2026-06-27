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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.petFinderExtraColors
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

@Composable
fun PetCard(
    name: String,
    breed: String,
    imageUrl: String?,
    status: PetStatus,
    onViewProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    onFavoriteClick: (() -> Unit)? = null,
    isFavorite: Boolean = false,
    showFavorite: Boolean = false,
    actionButtonText: String = "View Profile",
    onActionClick: (() -> Unit)? = null,
) {
    val extraColors = petFinderExtraColors()
    val actionClick = onActionClick ?: onViewProfileClick

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.card)
            .border(
                width = Dimensions.borderThin,
                color = extraColors.cardBorder,
                shape = ComponentShapes.card,
            )
            .softShadow()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.petCardImageHeight),
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.petCardImageHeight)
                    .clip(ComponentShapes.card),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Spacing.md),
            ) {
                when (status) {
                    PetStatus.SAFE -> SafeBadge()
                    PetStatus.LOST -> LostBadge()
                }
            }
        }

        Column(
            modifier = Modifier.padding(Spacing.containerPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = Spacing.xxs),
                    ) {
                        Icon(
                            imageVector = AppIcons.LostPets,
                            contentDescription = null,
                            modifier = Modifier.size(Dimensions.iconSizeSmall),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.size(Spacing.xxs))
                        Text(
                            text = breed,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                if (showFavorite && onFavoriteClick != null) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) AppIcons.FavoriteFilled else AppIcons.Favorite,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            PrimaryButton(
                text = actionButtonText,
                onClick = actionClick,
                icon = if (status == PetStatus.LOST) AppIcons.ForwardFilled else null,
                useGradient = status == PetStatus.LOST,
            )
        }
    }
}
