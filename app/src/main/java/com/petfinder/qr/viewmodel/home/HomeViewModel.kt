package com.petfinder.qr.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val pets: List<PetUiModel> = emptyList(),
    val safeCount: Int = 0,
    val totalCount: Int = 0,
    val scanCount: Int = 0,
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PetRepository,
) : ViewModel() {

    init {
        // Populate demo data on first launch so the dashboard isn't empty offline.
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    val uiState: StateFlow<HomeUiState> =
        combine(repository.allPets, repository.scanCount) { pets, scans ->
            HomeUiState(
                pets = pets,
                safeCount = pets.count { it.status == PetStatus.SAFE },
                totalCount = pets.size,
                scanCount = scans,
                isLoading = false,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(),
        )
}
