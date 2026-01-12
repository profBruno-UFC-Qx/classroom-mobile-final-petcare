package com.example.petcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcare.data.local.entity.PetEventEntity
import com.example.petcare.data.repository.PetRepository
import kotlinx.coroutines.launch
import java.util.Date

class EventFormViewModel(
    private val petRepository: PetRepository,
    private val petId: Long
) : ViewModel() {

    var eventType by mutableStateOf("Vacina")
    var eventName by mutableStateOf("")

    // Foco em agendamento obrigatório
    var nextDueDate by mutableStateOf<Long?>(null)
    var eventTime by mutableStateOf("") // Ex: "14:30"

    fun onEventTypeChange(newType: String) { eventType = newType }
    fun onNameChange(newName: String) { eventName = newName }
    fun onNextDueDateChange(newDateMillis: Long?) { nextDueDate = newDateMillis }
    fun onTimeChange(newTime: String) { eventTime = newTime }

    fun saveEvent() {
        viewModelScope.launch {
            val scheduledDate = nextDueDate ?: return@launch

            // Validação básica: nome preenchido e horário com pelo menos 4 caracteres (ex: 9:00)
            if (eventName.isBlank() || eventTime.length < 4) return@launch

            val newEvent = PetEventEntity(
                petOwnerId = petId,
                type = eventType,
                title = eventName,
                date = Date(scheduledDate), // Data do compromisso
                time = eventTime,           // Passando o horário para o campo específico
                details = null,             // Detalhes podem ser nulos ou vazios
                nextDueDate = Date(scheduledDate)
            )

            petRepository.insertPetEvent(newEvent)
        }
    }
}