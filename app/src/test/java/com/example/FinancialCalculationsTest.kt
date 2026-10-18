package com.example

import com.example.core.MoneyUtils
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import com.example.domain.model.Department
import com.example.domain.usecase.FinancialCalculationUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class FinancialCalculationsTest {

    private val useCase = FinancialCalculationUseCase()

    @Test
    fun `test money parsing handles arabic and english decimals correctly`() {
        assertEquals(BigDecimal("1250.50"), MoneyUtils.parseBigDecimal("1250.50"))
        assertEquals(BigDecimal("1250.50"), MoneyUtils.parseBigDecimal("1250،50"))
        assertEquals(BigDecimal("1250.50"), MoneyUtils.parseBigDecimal("١٢٥٠.٥٠"))
        assertNull(MoneyUtils.parseBigDecimal("invalid-amount"))
        assertNull(MoneyUtils.parseBigDecimal("-50.00"))
        assertNull(MoneyUtils.parseBigDecimal("0"))
    }

    @Test
    fun `test precision calculation avoids floating point errors`() {
        val a = BigDecimal("0.10")
        val b = BigDecimal("0.20")
        val sum = MoneyUtils.add(a, b)
        assertEquals(BigDecimal("0.30"), sum)
    }

    @Test
    fun `test financial summary calculation combines expenses revenues and vehicle logs`() {
        val expenses = listOf(
            ExpenseEntity(
                id = 1,
                amount = "1000.00",
                date = 1000L,
                department = "PERSONAL",
                category = "عناية",
                title = "مشتريات"
            ),
            ExpenseEntity(
                id = 2,
                amount = "2500.00",
                date = 2000L,
                department = "EDUCATION",
                category = "رسوم",
                title = "قسط مدرسة"
            )
        )

        val vehicleLogs = listOf(
            VehicleLogEntity(
                id = 1,
                vehicleId = 1,
                logType = "FUEL",
                amount = "500.00",
                date = 3000L,
                liters = 50.0,
                odometer = 10000
            ),
            VehicleLogEntity(
                id = 2,
                vehicleId = 1,
                logType = "MAINTENANCE",
                amount = "1500.00",
                date = 4000L,
                serviceType = "صيانة دورية"
            )
        )

        val revenues = listOf(
            RevenueEntity(
                id = 1,
                amount = "10000.00",
                date = 5000L,
                category = "راتب",
                title = "راتب وظيفي"
            )
        )

        val summary = useCase.calculateSummary(expenses, revenues, vehicleLogs)

        // Total Expenses = 1000 + 2500 + 500 (fuel) + 1500 (maintenance) = 5500.00
        assertEquals(BigDecimal("5500.00"), summary.totalExpenses)
        assertEquals(BigDecimal("10000.00"), summary.totalRevenues)
        // Net Balance = 10000 - 5500 = 4500.00
        assertEquals(BigDecimal("4500.00"), summary.netBalance)
        assertEquals(BigDecimal("500.00"), summary.fuelTotal)
        assertEquals(BigDecimal("1500.00"), summary.maintenanceTotal)

        // Verify recent transactions include all 5 entries
        assertEquals(5, summary.recentTransactions.size)
    }

    @Test
    fun `test vehicle summary calculation and fuel efficiency`() {
        val vehicle = VehicleEntity(
            id = 1,
            name = "تويوتا",
            plateNumber = "123",
            modelYear = "2023",
            initialOdometer = 10000
        )

        val logs = listOf(
            VehicleLogEntity(
                id = 1,
                vehicleId = 1,
                logType = "FUEL",
                amount = "400.00",
                date = 1000L,
                odometer = 10000,
                liters = 40.0
            ),
            VehicleLogEntity(
                id = 2,
                vehicleId = 1,
                logType = "FUEL",
                amount = "400.00",
                date = 2000L,
                odometer = 10500, // 500 km difference
                liters = 50.0      // 50 liters spent
            ),
            VehicleLogEntity(
                id = 3,
                vehicleId = 1,
                logType = "MAINTENANCE",
                amount = "800.00",
                date = 3000L,
                serviceType = "تغيير زيت"
            )
        )

        val revenues = listOf(
            RevenueEntity(
                id = 1,
                amount = "2500.00",
                date = 4000L,
                category = "إيرادات مركبات",
                title = "نقل ركاب",
                vehicleId = 1
            )
        )

        val summary = useCase.calculateVehicleSummary(vehicle, emptyList(), logs, revenues)

        // Fuel expenses = 800.00, Maintenance = 800.00, Total Expenses = 1600.00
        assertEquals(BigDecimal("800.00"), summary.fuelExpenses)
        assertEquals(BigDecimal("800.00"), summary.maintenanceExpenses)
        assertEquals(BigDecimal("1600.00"), summary.totalExpenses)
        assertEquals(BigDecimal("2500.00"), summary.totalRevenues)
        // Net profit = 2500 - 1600 = 900.00
        assertEquals(BigDecimal("900.00"), summary.netProfitOrLoss)

        // Average fuel efficiency: 500 km / 50 liters = 10.0 km/L
        assertNotNull(summary.averageFuelConsumptionKmPerLiter)
        assertEquals(10.0, summary.averageFuelConsumptionKmPerLiter!!, 0.01)
    }

    @Test
    fun `test empty inputs return zero balances safely`() {
        val summary = useCase.calculateSummary(emptyList(), emptyList(), emptyList())
        assertEquals(MoneyUtils.ZERO, summary.totalExpenses)
        assertEquals(MoneyUtils.ZERO, summary.totalRevenues)
        assertEquals(MoneyUtils.ZERO, summary.netBalance)
        assertTrue(summary.recentTransactions.isEmpty())
    }
}
