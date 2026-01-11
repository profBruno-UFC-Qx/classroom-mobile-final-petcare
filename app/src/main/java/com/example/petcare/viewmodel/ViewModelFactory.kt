package com.example.petcare.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petcare.data.local.PetCareDatabase
import com.example.petcare.data.repository.PetRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val database by lazy { PetCareDatabase.getDatabase(context) }
    private val petRepository by lazy { PetRepository(database.petDao()) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetFormViewModel(petRepository) as T
        }

        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(petRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
