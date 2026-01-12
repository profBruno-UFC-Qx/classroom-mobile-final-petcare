package com.example.petcare.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petcare.data.local.entity.PetEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: PetEventEntity)

    @Delete
    suspend fun delete(event: PetEventEntity)

    @Query("SELECT * FROM pet_events WHERE petOwnerId = :petId ORDER BY date DESC")
    fun getEventsForPet(petId: Long): Flow<List<PetEventEntity>>
}