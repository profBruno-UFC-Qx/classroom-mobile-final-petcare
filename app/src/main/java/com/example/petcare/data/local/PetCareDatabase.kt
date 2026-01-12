package com.example.petcare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.dao.PetEventDao
import com.example.petcare.data.local.entity.PetEntity
import com.example.petcare.data.local.entity.PetEventEntity
import com.example.petcare.utils.Converters

@Database(entities = [PetEntity::class, PetEventEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PetCareDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    abstract fun petEventDao(): PetEventDao


    companion object {
        @Volatile
        private var INSTANCE: PetCareDatabase? = null

        fun getDatabase(context: Context): PetCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetCareDatabase::class.java,
                    "petcare_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
