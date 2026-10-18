package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "revenues",
    indices = [
        Index(value = ["date"]),
        Index(value = ["category"]),
        Index(value = ["vehicleId"])
    ]
)
data class RevenueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: String, // String representation of BigDecimal
    val date: Long,     // Epoch timestamp in milliseconds
    val category: String, // SALARY, FREELANCE, VEHICLE_EARNINGS, CERTIFICATE_INTEREST, INVESTMENT, OTHER
    val title: String,
    val notes: String = "",
    val vehicleId: Long? = null,        // Associated vehicle for vehicle revenues
    val certificateId: Long? = null,    // Associated certificate if interest payout
    val createdAt: Long = System.currentTimeMillis()
)
