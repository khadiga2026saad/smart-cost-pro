package com.example.domain.usecase

import com.example.core.DateUtils
import com.example.core.MoneyUtils
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import com.example.domain.model.Department
import com.example.domain.model.DepartmentSummary
import com.example.domain.model.FinancialSummary
import com.example.domain.model.MonthlyReportItem
import com.example.domain.model.TransactionItem
import com.example.domain.model.TransactionType
import com.example.domain.model.VehicleSummary
import java.math.BigDecimal
import java.math.RoundingMode

class FinancialCalculationUseCase {

    fun calculateSummary(
        expenses: List<ExpenseEntity>,
        revenues: List<RevenueEntity>,
        vehicleLogs: List<VehicleLogEntity>
    ): FinancialSummary {
        var directExpensesSum = MoneyUtils.ZERO
        val deptTotals = mutableMapOf<Department, BigDecimal>()
        Department.entries.forEach { dept ->
            deptTotals[dept] = MoneyUtils.ZERO
        }

        // Sum direct expenses and bucket by department
        for (expense in expenses) {
            val amount = MoneyUtils.parseBigDecimalAllowZero(expense.amount)
            directExpensesSum = MoneyUtils.add(directExpensesSum, amount)

            val dept = Department.fromId(expense.department)
            val currentDeptTotal = deptTotals[dept] ?: MoneyUtils.ZERO
            deptTotals[dept] = MoneyUtils.add(currentDeptTotal, amount)
        }

        // Sum vehicle logs
        var fuelSum = MoneyUtils.ZERO
        var maintenanceSum = MoneyUtils.ZERO
        for (log in vehicleLogs) {
            val amount = MoneyUtils.parseBigDecimalAllowZero(log.amount)
            if (log.logType == "FUEL") {
                fuelSum = MoneyUtils.add(fuelSum, amount)
            } else {
                maintenanceSum = MoneyUtils.add(maintenanceSum, amount)
            }
        }
        val vehicleLogsSum = MoneyUtils.add(fuelSum, maintenanceSum)

        // Vehicle department includes direct vehicle expenses + fuel + maintenance
        val currentVehicleDept = deptTotals[Department.VEHICLE] ?: MoneyUtils.ZERO
        deptTotals[Department.VEHICLE] = MoneyUtils.add(currentVehicleDept, vehicleLogsSum)

        val totalExpenses = MoneyUtils.add(directExpensesSum, vehicleLogsSum)

        // Sum revenues
        var totalRevenues = MoneyUtils.ZERO
        for (revenue in revenues) {
            val amount = MoneyUtils.parseBigDecimalAllowZero(revenue.amount)
            totalRevenues = MoneyUtils.add(totalRevenues, amount)
        }

        val netBalance = MoneyUtils.subtract(totalRevenues, totalExpenses)

        // Calculate department percentages
        val deptSummaries = deptTotals.map { (dept, sum) ->
            val percentage = if (totalExpenses > BigDecimal.ZERO) {
                sum.divide(totalExpenses, 4, RoundingMode.HALF_UP).toFloat() * 100f
            } else {
                0f
            }
            DepartmentSummary(
                department = dept,
                totalAmount = sum,
                percentageOfTotal = percentage
            )
        }.sortedByDescending { it.totalAmount }

        // Compile unified recent transactions
        val transactions = mutableListOf<TransactionItem>()
        expenses.forEach { exp ->
            transactions.add(
                TransactionItem(
                    id = exp.id,
                    title = exp.title,
                    amount = MoneyUtils.parseBigDecimalAllowZero(exp.amount),
                    date = exp.date,
                    type = TransactionType.EXPENSE,
                    subtitle = "${Department.fromId(exp.department).titleAr} • ${exp.category}",
                    department = Department.fromId(exp.department),
                    rawDepartment = exp.department
                )
            )
        }

        revenues.forEach { rev ->
            transactions.add(
                TransactionItem(
                    id = rev.id,
                    title = rev.title,
                    amount = MoneyUtils.parseBigDecimalAllowZero(rev.amount),
                    date = rev.date,
                    type = TransactionType.REVENUE,
                    subtitle = "إيراد • ${rev.category}"
                )
            )
        }

        vehicleLogs.forEach { log ->
            val isFuel = log.logType == "FUEL"
            val title = if (isFuel) "تزويد وقود" else (log.serviceType ?: "صيانة دورية")
            val subtitle = if (isFuel) {
                "${log.liters ?: 0.0} لتر • ${log.odometer ?: 0} كم"
            } else {
                "${log.serviceCenter ?: "مركز الخدمة"}"
            }
            transactions.add(
                TransactionItem(
                    id = log.id,
                    title = title,
                    amount = MoneyUtils.parseBigDecimalAllowZero(log.amount),
                    date = log.date,
                    type = TransactionType.VEHICLE_LOG,
                    subtitle = "مركبات • $subtitle",
                    department = Department.VEHICLE,
                    logType = log.logType
                )
            )
        }

        val recentSorted = transactions.sortedByDescending { it.date }.take(20)

        return FinancialSummary(
            totalExpenses = totalExpenses,
            totalRevenues = totalRevenues,
            netBalance = netBalance,
            departmentSummaries = deptSummaries,
            recentTransactions = recentSorted,
            fuelTotal = fuelSum,
            maintenanceTotal = maintenanceSum
        )
    }

    fun calculateVehicleSummary(
        vehicle: VehicleEntity,
        vehicleExpenses: List<ExpenseEntity>,
        vehicleLogs: List<VehicleLogEntity>,
        vehicleRevenues: List<RevenueEntity>
    ): VehicleSummary {
        var directExpenses = MoneyUtils.ZERO
        for (exp in vehicleExpenses) {
            directExpenses = MoneyUtils.add(directExpenses, MoneyUtils.parseBigDecimalAllowZero(exp.amount))
        }

        var fuelExpenses = MoneyUtils.ZERO
        var maintenanceExpenses = MoneyUtils.ZERO
        var totalFuelLiters = 0.0

        for (log in vehicleLogs) {
            val amount = MoneyUtils.parseBigDecimalAllowZero(log.amount)
            if (log.logType == "FUEL") {
                fuelExpenses = MoneyUtils.add(fuelExpenses, amount)
                totalFuelLiters += (log.liters ?: 0.0)
            } else {
                maintenanceExpenses = MoneyUtils.add(maintenanceExpenses, amount)
            }
        }

        val totalExpenses = MoneyUtils.add(directExpenses, MoneyUtils.add(fuelExpenses, maintenanceExpenses))

        var totalRevenues = MoneyUtils.ZERO
        for (rev in vehicleRevenues) {
            totalRevenues = MoneyUtils.add(totalRevenues, MoneyUtils.parseBigDecimalAllowZero(rev.amount))
        }

        val netProfitOrLoss = MoneyUtils.subtract(totalRevenues, totalExpenses)

        // Calculate fuel consumption if multiple fuel logs with valid odometer and liters exist
        val fuelLogsSortedByOdo = vehicleLogs
            .filter { it.logType == "FUEL" && (it.odometer ?: 0) > 0 && (it.liters ?: 0.0) > 0.0 }
            .sortedBy { it.odometer }

        val avgConsumption: Double? = if (fuelLogsSortedByOdo.size >= 2) {
            val deltaKm = (fuelLogsSortedByOdo.last().odometer ?: 0) - (fuelLogsSortedByOdo.first().odometer ?: 0)
            // exclude first fill or sum remaining liters
            val litersSpent = fuelLogsSortedByOdo.drop(1).sumOf { it.liters ?: 0.0 }
            if (deltaKm > 0 && litersSpent > 0.0) {
                // Kilometers per liter
                Math.round((deltaKm / litersSpent) * 100.0) / 100.0
            } else null
        } else null

        return VehicleSummary(
            vehicleId = vehicle.id,
            vehicleName = vehicle.name,
            plateNumber = vehicle.plateNumber,
            directExpenses = directExpenses,
            fuelExpenses = fuelExpenses,
            maintenanceExpenses = maintenanceExpenses,
            totalExpenses = totalExpenses,
            totalRevenues = totalRevenues,
            netProfitOrLoss = netProfitOrLoss,
            fuelLiters = totalFuelLiters,
            averageFuelConsumptionKmPerLiter = avgConsumption
        )
    }

    fun calculateMonthlyReports(
        allExpenses: List<ExpenseEntity>,
        allRevenues: List<RevenueEntity>,
        allVehicleLogs: List<VehicleLogEntity>
    ): List<MonthlyReportItem> {
        val monthsMap = mutableMapOf<Pair<Int, Int>, MutableList<Any>>()

        allExpenses.forEach { exp ->
            val date = DateUtils.formatToLocalDate(exp.date)
            val key = Pair(date.year, date.monthValue)
            monthsMap.getOrPut(key) { mutableListOf() }.add(exp)
        }

        allVehicleLogs.forEach { log ->
            val date = DateUtils.formatToLocalDate(log.date)
            val key = Pair(date.year, date.monthValue)
            monthsMap.getOrPut(key) { mutableListOf() }.add(log)
        }

        allRevenues.forEach { rev ->
            val date = DateUtils.formatToLocalDate(rev.date)
            val key = Pair(date.year, date.monthValue)
            monthsMap.getOrPut(key) { mutableListOf() }.add(rev)
        }

        return monthsMap.keys.sortedWith(compareByDescending<Pair<Int, Int>> { it.first }.thenByDescending { it.second })
            .map { key ->
                val year = key.first
                val month = key.second
                val items = monthsMap[key] ?: emptyList()

                var expTotal = MoneyUtils.ZERO
                var revTotal = MoneyUtils.ZERO

                items.forEach { item ->
                    when (item) {
                        is ExpenseEntity -> expTotal = MoneyUtils.add(expTotal, MoneyUtils.parseBigDecimalAllowZero(item.amount))
                        is VehicleLogEntity -> expTotal = MoneyUtils.add(expTotal, MoneyUtils.parseBigDecimalAllowZero(item.amount))
                        is RevenueEntity -> revTotal = MoneyUtils.add(revTotal, MoneyUtils.parseBigDecimalAllowZero(item.amount))
                    }
                }

                MonthlyReportItem(
                    year = year,
                    month = month,
                    monthName = DateUtils.getMonthName(month),
                    totalExpenses = expTotal,
                    totalRevenues = revTotal,
                    netBalance = MoneyUtils.subtract(revTotal, expTotal)
                )
            }
    }
}
