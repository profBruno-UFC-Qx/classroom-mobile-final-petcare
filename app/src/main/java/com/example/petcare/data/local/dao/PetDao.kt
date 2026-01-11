package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petcare.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePet(pet: PetEntity)

    @Query("SELECT * FROM pets WHERE id = :petId")
    fun getPetById(petId: Long): Flow<PetEntity?>

    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: Long)
}
