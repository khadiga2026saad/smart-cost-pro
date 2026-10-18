package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicle_logs",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["vehicleId"]),
        Index(value = ["date"]),
        Index(value = ["logType"])
    ]
)
data class VehicleLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleId: Long,
    val logType: String, // "FUEL" or "MAINTENANCE"
    val amount: String,  // BigDecimal as String
    val date: Long,      // Epoch millis
    val odometer: Int? = null,
    val liters: Double? = null,
    val serviceType: String? = null,
    val serviceCenter: String? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
