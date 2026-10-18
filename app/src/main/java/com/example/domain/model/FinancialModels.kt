package com.example.domain.model

import java.math.BigDecimal

data class FinancialSummary(
    val totalExpenses: BigDecimal,
    val totalRevenues: BigDecimal,
    val netBalance: BigDecimal,
    val departmentSummaries: List<DepartmentSummary>,
    val recentTransactions: List<TransactionItem>,
    val fuelTotal: BigDecimal,
    val maintenanceTotal: BigDecimal
)

data class DepartmentSummary(
    val department: Department,
    val totalAmount: BigDecimal,
    val percentageOfTotal: Float // 0f to 100f
)

data class VehicleSummary(
    val vehicleId: Long,
    val vehicleName: String,
    val plateNumber: String,
    val directExpenses: BigDecimal,
    val fuelExpenses: BigDecimal,
    val maintenanceExpenses: BigDecimal,
    val totalExpenses: BigDecimal,
    val totalRevenues: BigDecimal,
    val netProfitOrLoss: BigDecimal,
    val fuelLiters: Double,
    val averageFuelConsumptionKmPerLiter: Double? // Calculated if multiple odometer readings available
)

enum class TransactionType {
    EXPENSE,
    REVENUE,
    VEHICLE_LOG
}

data class TransactionItem(
    val id: Long,
    val title: String,
    val amount: BigDecimal,
    val date: Long,
    val type: TransactionType,
    val subtitle: String,
    val department: Department? = null,
    val rawDepartment: String? = null,
    val logType: String? = null // For vehicle logs: FUEL or MAINTENANCE
)

data class MonthlyReportItem(
    val year: Int,
    val month: Int,
    val monthName: String,
    val totalExpenses: BigDecimal,
    val totalRevenues: BigDecimal,
    val netBalance: BigDecimal
)
