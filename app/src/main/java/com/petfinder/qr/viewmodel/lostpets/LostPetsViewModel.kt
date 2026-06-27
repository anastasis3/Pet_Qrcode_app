package com.petfinder.qr.viewmodel.lostpets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.LostPetUiModel
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class LostPetsUiState(
    val pets: List<LostPetUiModel> = emptyList(),
    val query: String = "",
    val resultCount: Int = 0,
)

@HiltViewModel
class LostPetsViewModel @Inject constructor(
    repository: PetRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    fun onQueryChange(value: String) {
        _query.value = value
    }

    val uiState: StateFlow<LostPetsUiState> =
        combine(repository.lostPets, _query) { pets, query ->
            val filtered = if (query.isBlank()) {
                pets
            } else {
                pets.filter {
                    it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
                }
            }
            LostPetsUiState(pets = filtered, query = query, resultCount = filtered.size)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LostPetsUiState(),
        )
}
