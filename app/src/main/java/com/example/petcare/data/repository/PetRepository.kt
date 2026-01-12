package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.dao.PetEventDao
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.local.entity.PetEventEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val petDao: PetDao,
    private val petEventDao: PetEventDao
) {
    // Funções de Pet
    fun getAllPets(): Flow<List<PetEntity>> = petDao.getAllPets()
    fun getPetById(petId: Long): Flow<PetEntity?> = petDao.getPetById(petId)
    suspend fun savePet(pet: PetEntity) = petDao.savePet(pet)
    suspend fun delete(pet: PetEntity) = petDao.delete(pet)
    suspend fun deletePetById(petId: Long) = petDao.deletePetById(petId)

    // Funções de Eventos (Vacinas/Cuidados)
    fun getEventsForPet(petId: Long): Flow<List<PetEventEntity>> = petEventDao.getEventsForPet(petId)
    suspend fun insertPetEvent(event: PetEventEntity) = petEventDao.insert(event)
}