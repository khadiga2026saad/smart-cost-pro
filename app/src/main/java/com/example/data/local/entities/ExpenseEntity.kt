package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    indices = [
        Index(value = ["date"]),
        Index(value = ["department"]),
        Index(value = ["vehicleId"])
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: String, // String representation of BigDecimal
    val date: Long,     // Epoch timestamp in milliseconds
    val department: String, // PERSONAL, HOME, EDUCATION, WORK, VEHICLE
    val category: String,
    val title: String,
    val notes: String = "",
    // Department-specific fields
    val vehicleId: Long? = null,
    val studentName: String? = null,
    val institution: String? = null,
    val academicTerm: String? = null,
    val projectName: String? = null,
    val isTaxDeductible: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
