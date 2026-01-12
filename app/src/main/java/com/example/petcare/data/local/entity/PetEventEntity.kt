package com.example.petcare.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "pet_events",
    foreignKeys = [ForeignKey(
        entity = PetEntity::class,
        parentColumns = ["id"],
        childColumns = ["petOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["petOwnerId"])]
)
data class PetEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val petOwnerId: Long,

    val type: String,
    val title: String,
    val date: Date,

    val time: String, // Armazena o horário (ex: "14:30")

    val details: String?,
    val nextDueDate: Date?
)