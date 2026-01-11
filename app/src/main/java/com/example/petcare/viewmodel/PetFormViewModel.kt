package com.example.petcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.launch
import java.util.Date

data class PetFormUiState(
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val birthDate: Date? = null,
    val isFormValid: Boolean = false
)

class PetFormViewModel(private val petRepository: PetRepository) : ViewModel() {

    var uiState by mutableStateOf(PetFormUiState())
        private set

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
                name = uiState.name,
                species = uiState.species,
                breed = uiState.breed.takeIf { it.isNotBlank() } ?: "",
                birthDate = uiState.birthDate
            )
            petRepository.savePet(petEntity)
        }
    }
}
