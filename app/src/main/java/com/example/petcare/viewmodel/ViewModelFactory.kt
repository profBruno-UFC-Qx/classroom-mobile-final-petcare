package com.example.petcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.repository.PetRepository

/**
 * Fábrica de ViewModels que injeta as dependências necessárias.
 * O petId é opcional e usado apenas pelos ViewModels que precisam dele.
 */
class ViewModelFactory(
    private val context: Context,
    private val petId: Long? = null // petId é opcional
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Inicializa o banco de dados e o repositório uma vez aqui
        val db = PetCareDatabase.getDatabase(context)
        val petRepository = PetRepository(db.petDao(), db.petEventDao())

        return when {
            // ViewModel para a tela principal
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(petRepository) as T
            }
            // ViewModel para o formulário de criar/editar Pet
            modelClass.isAssignableFrom(PetFormViewModel::class.java) -> {
                PetFormViewModel(petRepository) as T
            }
            // ViewModel para a tela de detalhes de um Pet
            modelClass.isAssignableFrom(PetDetailViewModel::class.java) -> {
                // Requer que o petId tenha sido fornecido ao criar a factory
                requireNotNull(petId) { "petId não pode ser nulo para PetDetailViewModel" }
                PetDetailViewModel(petRepository, petId) as T
            }
            
            // ViewModel para o formulário de criar um novo Cuidado/Evento
            modelClass.isAssignableFrom(EventFormViewModel::class.java) -> {
                // Também requer o petId para saber a qual pet o evento pertence
                requireNotNull(petId) { "petId não pode ser nulo para EventFormViewModel" }
                EventFormViewModel(petRepository, petId) as T
            }
            // Caso algum ViewModel não seja encontrado
            else -> throw IllegalArgumentException("Classe ViewModel desconhecida: ${modelClass.name}")
        }
    }
}
