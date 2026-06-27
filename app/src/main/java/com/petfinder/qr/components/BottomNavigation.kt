package com.petfinder.qr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.ComponentShapes
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.softShadow

enum class BottomNavDestination(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
) {
    Home(
        label = "Home",
        icon = AppIcons.Home,
        selectedIcon = AppIcons.HomeFilled,
    ),
    AddPet(
        label = "Add Pet",
        icon = AppIcons.AddPet,
        selectedIcon = AppIcons.AddPetFilled,
    ),
    LostPets(
        label = "Lost Pets",
        icon = AppIcons.LostPets,
        selectedIcon = AppIcons.LostPetsFilled,
    ),
}

@Composable
fun BottomNavigation(
    selectedDestination: BottomNavDestination?,
    onDestinationSelected: (BottomNavDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(ComponentShapes.bottomNav)
            .softShadow()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .navigationBarsPadding()
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BottomNavDestination.entries.forEach { destination ->
            BottomNavItem(
                destination = destination,
                selected = destination == selectedDestination,
                onClick = { onDestinationSelected(destination) },
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    destination: BottomNavDestination,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val icon = if (selected) destination.selectedIcon else destination.icon

    Column(
        modifier = Modifier
            .clip(ComponentShapes.bottomNavItem)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                role = Role.Tab,
                onClick = onClick,
            )
            .padding(horizontal = Spacing.gutter, vertical = Spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = destination.label,
            modifier = Modifier.size(Dimensions.bottomNavIconSize),
            tint = contentColor,
        )
        Text(
            text = destination.label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            ),
            color = contentColor,
            modifier = Modifier.padding(top = Spacing.xxs),
        )
    }
}
