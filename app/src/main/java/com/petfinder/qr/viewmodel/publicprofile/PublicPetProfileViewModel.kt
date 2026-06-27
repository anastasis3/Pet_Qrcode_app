package com.petfinder.qr.viewmodel.publicprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.PetUiModel
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PublicPetProfileViewModel @Inject constructor(
    repository: PetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = savedStateHandle["petId"] ?: ""

    val pet: StateFlow<PetUiModel?> =
        repository.pet(petId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val lastScan: StateFlow<ScanEvent?> =
        repository.lastScan(petId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
