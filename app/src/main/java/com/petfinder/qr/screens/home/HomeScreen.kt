package com.petfinder.qr.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.PetCard
import com.petfinder.qr.components.PetFinderFab
import com.petfinder.qr.components.SectionTitle
import com.petfinder.qr.components.StatCard
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarAccountAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.preview.SampleData
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing
import com.petfinder.qr.theme.StatCardBlue
import com.petfinder.qr.theme.StatCardPurple

@Composable
fun HomeScreen(
    pets: List<PetUiModel> = SampleData.myPets,
    safeText: String = "2/2",
    scanCountText: String = "12",
    onViewAll: () -> Unit = {},
    onViewProfile: (PetUiModel) -> Unit = {},
    onAddPet: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBar(actions = { TopBarAccountAction(onClick = onLogout) })
        },
        bottomBar = {
            BottomNavigation(
                selectedDestination = BottomNavDestination.Home,
                onDestinationSelected = onNavigate,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.containerPadding),
            ) {
                VGap(Spacing.xs)
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                )
                VGap(Spacing.xxs)
                Text(
                    text = "Your furry family is safe and sound.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                VGap(Spacing.xl)
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    StatCard(
                        icon = AppIcons.Shield,
                        label = "All Safe",
                        value = safeText,
                        containerColor = StatCardBlue,
                        iconTint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                    )
                    StatCard(
                        icon = AppIcons.QrCode,
                        label = "QR Scans",
                        value = scanCountText,
                        containerColor = StatCardPurple,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f),
                    )
                }

                VGap(Spacing.xxl)
                SectionTitle(
                    title = "My Pets",
                    trailing = {
                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(Spacing.xxs),
                        )
                    },
                )

                VGap(Spacing.md)
                pets.forEach { pet ->
                    PetCard(
                        name = pet.name,
                        breed = pet.breed,
                        imageUrl = pet.imageUrl,
                        status = pet.status,
                        onViewProfileClick = { onViewProfile(pet) },
                    )
                    VGap(Spacing.lg)
                }

                VGap(Spacing.bottomNavClearance)
            }

            PetFinderFab(
                label = "Add Pet",
                onClick = onAddPet,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = Spacing.lg, bottom = Spacing.lg),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    PetFinderTheme {
        HomeScreen()
    }
}
