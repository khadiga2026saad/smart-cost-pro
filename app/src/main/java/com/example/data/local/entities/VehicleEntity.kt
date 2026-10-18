package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val plateNumber: String,
    val modelYear: String,
    val initialOdometer: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
