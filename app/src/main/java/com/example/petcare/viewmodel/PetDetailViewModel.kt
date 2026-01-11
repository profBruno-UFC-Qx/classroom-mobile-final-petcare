package com.example.petcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetDetailViewModel(
    private val petRepository: PetRepository
) : ViewModel() {

    // StateFlow para guardar os dados do pet que estamos visualizando
    private val _pet = MutableStateFlow<PetEntity?>(null)
    val pet = _pet.asStateFlow()

    // Função para ser chamada quando a tela é aberta com um ID de pet
    fun loadPet(petId: Long) {
        viewModelScope.launch {
            petRepository.getPetById(petId).collect { petFromDb ->
                _pet.update { petFromDb }
            }
        }
    }

    // Função para excluir o pet
    fun deletePet(petId: Long) {
        viewModelScope.launch {
            petRepository.deletePetById(petId)
        }
    }
}
