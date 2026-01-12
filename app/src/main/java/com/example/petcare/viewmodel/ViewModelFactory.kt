package com.example.petcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.remote.RetrofitClient
import com.example.petcare.data.repository.PetRepository
import com.example.petcare.data.repository.PetShopRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val context: Context,
    private val petId: Long? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = PetCareDatabase.getDatabase(context)
        val petRepository = PetRepository(db.petDao(), db.petEventDao())

        val petShopRepository = PetShopRepository(RetrofitClient.petShopApiService)

        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(petRepository) as T
            }
            modelClass.isAssignableFrom(PetFormViewModel::class.java) -> {
                PetFormViewModel(petRepository) as T
            }
            modelClass.isAssignableFrom(PetDetailViewModel::class.java) -> {
                requireNotNull(petId) { "petId não pode ser nulo para PetDetailViewModel" }
                PetDetailViewModel(petRepository, petId) as T
            }
            modelClass.isAssignableFrom(EventFormViewModel::class.java) -> {
                requireNotNull(petId) { "petId não pode ser nulo para EventFormViewModel" }
                EventFormViewModel(petRepository, petId) as T
            }
            modelClass.isAssignableFrom(PetShopViewModel::class.java) -> {
                PetShopViewModel(petShopRepository) as T
            }

            else -> throw IllegalArgumentException("Classe ViewModel desconhecida: ${modelClass.name}")
        }
    }
}
