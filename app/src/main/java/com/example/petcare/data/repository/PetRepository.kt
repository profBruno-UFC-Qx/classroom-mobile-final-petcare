package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(private val petDao: PetDao) {

    fun getAllPets(): Flow<List<PetEntity>> = petDao.getAllPets()

    suspend fun savePet(pet: PetEntity) {
        petDao.savePet(pet)
    }

    fun getPetById(petId: Long): Flow<PetEntity?> = petDao.getPetById(petId)

    suspend fun deletePetById(petId: Long) {
        petDao.deletePetById(petId)
    }
}
