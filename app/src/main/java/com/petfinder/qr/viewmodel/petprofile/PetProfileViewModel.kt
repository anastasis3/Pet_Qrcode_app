package com.petfinder.qr.viewmodel.petprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.PetStatus
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PetProfileUiState(
    val pet: PetUiModel? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class PetProfileViewModel @Inject constructor(
    private val repository: PetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = savedStateHandle["petId"] ?: ""

    val uiState: StateFlow<PetProfileUiState> =
        repository.pet(petId)
            .map { PetProfileUiState(pet = it, isLoading = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PetProfileUiState(),
            )

    val lastScan: StateFlow<ScanEvent?> =
        repository.lastScan(petId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setStatus(status: PetStatus) {
        viewModelScope.launch { repository.setStatus(petId, status) }
    }

    fun delete(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deletePet(petId)
            onDeleted()
        }
    }
}
