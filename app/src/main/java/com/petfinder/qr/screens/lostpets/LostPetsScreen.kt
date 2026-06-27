package com.petfinder.qr.screens.lostpets

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.petfinder.qr.components.BottomNavDestination
import com.petfinder.qr.components.BottomNavigation
import com.petfinder.qr.components.FilterChipRow
import com.petfinder.qr.components.LostPetCard
import com.petfinder.qr.components.PetFinderFab
import com.petfinder.qr.components.SearchBar
import com.petfinder.qr.components.SecondaryButton
import com.petfinder.qr.components.TopBar
import com.petfinder.qr.components.TopBarIconAction
import com.petfinder.qr.components.VGap
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.preview.SampleData
import com.petfinder.qr.theme.AppIcons
import com.petfinder.qr.theme.Dimensions
import com.petfinder.qr.theme.PetFinderTheme
import com.petfinder.qr.theme.Spacing

@Composable
fun LostPetsScreen(
    lostPets: List<LostPetUiModel> = SampleData.lostPets,
    onViewDetails: (LostPetUiModel) -> Unit = {},
    onLoadMore: () -> Unit = {},
    onAddPet: () -> Unit = {},
    onNavigate: (BottomNavDestination) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopBar(
                actions = {
                    TopBarIconAction(
                        icon = AppIcons.Notifications,
                        contentDescription = "Notifications",
                        onClick = {},
                    )
                },
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedDestination = BottomNavDestination.LostPets,
                onDestinationSelected = onNavigate,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.containerPadding),
        ) {
            VGap(Spacing.xs)
            SearchBar(query = query, onQueryChange = { query = it })

            VGap(Spacing.md)
            FilterChipRow(
                options = listOf("All Pets", "Dogs", "Cats", "Birds"),
                selectedIndex = selectedFilter,
                onSelect = { selectedFilter = it },
            )

            VGap(Spacing.lg)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Community Alerts",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "Showing 24 results",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            VGap(Spacing.md)
            lostPets.forEach { pet ->
                LostPetCard(
                    pet = pet,
                    onViewDetails = { onViewDetails(pet) },
                )
                VGap(Spacing.lg)
            }

            VGap(Spacing.xs)
            Box(modifier = Modifier.fillMaxWidth()) {
                SecondaryButton(
                    text = "Load More Pets",
                    onClick = onLoadMore,
                    icon = AppIcons.ExpandMore,
                    fillMaxWidth = false,
                    modifier = Modifier.align(Alignment.Center),
                )
                PetFinderFab(
                    icon = AppIcons.AddFilled,
                    size = Dimensions.fabSizeSmall,
                    onClick = onAddPet,
                    modifier = Modifier.align(Alignment.CenterEnd),
                )
            }

            VGap(Spacing.xl)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LostPetsScreenPreview() {
    PetFinderTheme {
        LostPetsScreen()
    }
}
