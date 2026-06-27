package com.petfinder.qr.viewmodel.addpet

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petfinder.qr.model.PetFormData
import com.petfinder.qr.repository.ImageRepository
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
    private val imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String? = savedStateHandle["petId"]
    val isEditing: Boolean = petId != null

    private val _initialForm = MutableStateFlow(PetFormData())
    val initialForm: StateFlow<PetFormData> = _initialForm.asStateFlow()

    /** Currently-displayed photo (local `file://` now, remote URL once R2 lands). */
    private val _imageUri = MutableStateFlow<String?>(null)
    val imageUri: StateFlow<String?> = _imageUri.asStateFlow()

    private val _isProcessingImage = MutableStateFlow(false)
    val isProcessingImage: StateFlow<Boolean> = _isProcessingImage.asStateFlow()

    init {
        if (petId != null) {
            viewModelScope.launch {
                repository.getForm(petId)?.let { form ->
                    _initialForm.value = form
                    _imageUri.value = form.imageUrl
                }
            }
        }
    }

    /** Compresses + stores the gallery image, then shows it as the preview. */
    fun onImagePicked(uri: Uri) {
        if (_isProcessingImage.value) return
        viewModelScope.launch {
            _isProcessingImage.value = true
            runCatching { imageRepository.processAndStore(uri) }
                .onSuccess { _imageUri.value = it }
            _isProcessingImage.value = false
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
