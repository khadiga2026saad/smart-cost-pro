package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_certificates")
data class FinancialCertificateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val bankName: String,
    val principalAmount: String, // Principal investment as BigDecimal string
    val annualInterestRate: Double, // e.g. 23.5%
    val payoutFrequency: String, // MONTHLY, QUARTERLY, ANNUALLY, AT_MATURITY
    val issueDate: Long,
    val maturityDate: Long,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
