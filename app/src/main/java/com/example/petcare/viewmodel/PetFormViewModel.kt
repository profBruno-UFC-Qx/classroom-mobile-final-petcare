package com.example.petcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Date

data class PetFormUiState(
    val id: Long = 0,
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val birthDate: Date? = null,
    val isFormValid: Boolean = false,
    val isEditing: Boolean = false
)

class PetFormViewModel(private val petRepository: PetRepository) : ViewModel() {

    var uiState by mutableStateOf(PetFormUiState())
        private set

    fun loadPetForEditing(petId: Long) {
        viewModelScope.launch {
            val pet = petRepository.getPetById(petId).firstOrNull()
            pet?.let {
                uiState = uiState.copy(
                    id = it.id,
                    name = it.name,
                    species = it.species,
                    breed = it.breed ?: "",
                    birthDate = it.birthDate,
                    isEditing = true
                )
                validateForm()
            }
        }
    }

    fun onNameChange(newName: String) {
        uiState = uiState.copy(name = newName)
        validateForm()
    }

    fun onSpeciesChange(newSpecies: String) {
        uiState = uiState.copy(species = newSpecies)
        validateForm()
    }

    fun onBreedChange(newBreed: String) {
        uiState = uiState.copy(breed = newBreed)
    }

    fun onBirthDateChange(newDate: Date) {
        uiState = uiState.copy(birthDate = newDate)
        validateForm()
    }

    private fun validateForm() {
        val isValid = uiState.name.isNotBlank() &&
                uiState.species.isNotBlank() &&
                uiState.birthDate != null
        uiState = uiState.copy(isFormValid = isValid)
    }

    fun savePet() {
        if (!uiState.isFormValid) return

        viewModelScope.launch {
            val petEntity = PetEntity(
                id = uiState.id,
                name = uiState.name,
                species = uiState.species,
                breed = uiState.breed.takeIf { it.isNotBlank() } ?: "",
                birthDate = uiState.birthDate
            )
            petRepository.savePet(petEntity)
        }
    }
}
