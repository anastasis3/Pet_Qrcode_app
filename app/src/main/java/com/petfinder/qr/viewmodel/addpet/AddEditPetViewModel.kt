package com.petfinder.qr.viewmodel.addpet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Drives both the Add and Edit Pet flows. When the route carries a `petId`
 * argument the form is pre-loaded and [save] performs an update; otherwise it
 * inserts a new pet.
 */
@HiltViewModel
class AddEditPetViewModel @Inject constructor(
    private val repository: PetRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String? = savedStateHandle["petId"]
    val isEditing: Boolean = petId != null

    private val _initialForm = MutableStateFlow(PetFormData())
    val initialForm: StateFlow<PetFormData> = _initialForm.asStateFlow()

    init {
        if (petId != null) {
            viewModelScope.launch {
                repository.getForm(petId)?.let { _initialForm.value = it }
            }
        }
    }

    fun save(form: PetFormData, onSaved: () -> Unit) {
        viewModelScope.launch {
            if (petId == null) {
                repository.addPet(form)
            } else {
                repository.updatePet(petId, form)
            }
            onSaved()
        }
    }
}
