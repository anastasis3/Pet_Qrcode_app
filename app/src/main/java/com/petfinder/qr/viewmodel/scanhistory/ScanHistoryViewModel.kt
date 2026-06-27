package com.petfinder.qr.viewmodel.scanhistory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.ScanEvent
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ScanHistoryViewModel @Inject constructor(
    repository: PetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = savedStateHandle["petId"] ?: ""

    val events: StateFlow<List<ScanEvent>> =
        repository.scanHistory(petId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
}
