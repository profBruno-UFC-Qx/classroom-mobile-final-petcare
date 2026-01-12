package com.example.petcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.local.entity.PetEventEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PetDetailViewModel(
    private val petRepository: PetRepository,
    private val petId: Long
) : ViewModel() {

    // Observa os detalhes do Pet
    val pet: StateFlow<PetEntity?> = petRepository.getPetById(petId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    //Observa a lista de eventos/cuidados deste pet
    val events: StateFlow<List<PetEventEntity>> = petRepository.getEventsForPet(petId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            petRepository.delete(pet)
        }
    }
}