package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.SmartCostApplication
import com.example.core.DateUtils
import com.example.core.MoneyUtils
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.FinancialCertificateEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import com.example.data.repository.FinanceRepository
import com.example.domain.model.FinancialSummary
import com.example.domain.model.MonthlyReportItem
import com.example.domain.model.VehicleSummary
import com.example.domain.usecase.FinancialCalculationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class MonthFilter(val year: Int, val month: Int)

class FinanceViewModel(
    private val repository: FinanceRepository,
    private val calculationUseCase: FinancialCalculationUseCase
) : ViewModel() {

    private val _currentMonthFilter = MutableStateFlow(
        MonthFilter(DateUtils.currentYear(), DateUtils.currentMonth())
    )
    val currentMonthFilter: StateFlow<MonthFilter> = _currentMonthFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDeptFilter = MutableStateFlow("ALL")
    val selectedDeptFilter: StateFlow<String> = _selectedDeptFilter.asStateFlow()

    private val _sortByAmount = MutableStateFlow(false)
    val sortByAmount: StateFlow<Boolean> = _sortByAmount.asStateFlow()

    // Monthly data based on currentMonthFilter
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMonthSummary: StateFlow<FinancialSummary> = _currentMonthFilter.flatMapLatest { filter ->
        val start = DateUtils.getMonthStartEpoch(filter.year, filter.month)
        val end = DateUtils.getMonthEndEpoch(filter.year, filter.month)

        combine(
            repository.getExpensesBetween(start, end),
            repository.getRevenuesBetween(start, end),
            repository.getVehicleLogsBetween(start, end)
        ) { expenses, revenues, logs ->
            calculationUseCase.calculateSummary(expenses, revenues, logs)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FinancialSummary(
            totalExpenses = MoneyUtils.ZERO,
            totalRevenues = MoneyUtils.ZERO,
            netBalance = MoneyUtils.ZERO,
            departmentSummaries = emptyList(),
            recentTransactions = emptyList(),
            fuelTotal = MoneyUtils.ZERO,
            maintenanceTotal = MoneyUtils.ZERO
        )
    )

    // All expenses for current month with search and department filtering applied
    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredExpenses: StateFlow<List<ExpenseEntity>> = combine(
        _currentMonthFilter.flatMapLatest { filter ->
            val start = DateUtils.getMonthStartEpoch(filter.year, filter.month)
            val end = DateUtils.getMonthEndEpoch(filter.year, filter.month)
            repository.getExpensesBetween(start, end)
        },
        _searchQuery,
        _selectedDeptFilter,
        _sortByAmount
    ) { expenses, query, dept, sortAmount ->
        var result = expenses

        if (dept != "ALL") {
            result = result.filter { it.department.equals(dept, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter {
                it.title.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.notes.lowercase().contains(q) ||
                    (it.studentName?.lowercase()?.contains(q) == true) ||
                    (it.projectName?.lowercase()?.contains(q) == true)
            }
        }

        if (sortAmount) {
            result.sortedByDescending { MoneyUtils.parseBigDecimalAllowZero(it.amount) }
        } else {
            result.sortedByDescending { it.date }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // All revenues for current month
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMonthRevenues: StateFlow<List<RevenueEntity>> = _currentMonthFilter.flatMapLatest { filter ->
        val start = DateUtils.getMonthStartEpoch(filter.year, filter.month)
        val end = DateUtils.getMonthEndEpoch(filter.year, filter.month)
        repository.getRevenuesBetween(start, end)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Vehicles
    val allVehicles: StateFlow<List<VehicleEntity>> = repository.getAllVehicles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allVehicleLogs: StateFlow<List<VehicleLogEntity>> = repository.getAllVehicleLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Vehicle Summaries per vehicle
    val vehicleSummaries: StateFlow<List<VehicleSummary>> = combine(
        repository.getAllVehicles(),
        repository.getAllExpenses(),
        repository.getAllVehicleLogs(),
        repository.getAllRevenues()
    ) { vehicles, expenses, logs, revenues ->
        vehicles.map { vehicle ->
            val vExpenses = expenses.filter { it.vehicleId == vehicle.id }
            val vLogs = logs.filter { it.vehicleId == vehicle.id }
            val vRevenues = revenues.filter { it.vehicleId == vehicle.id }
            calculationUseCase.calculateVehicleSummary(vehicle, vExpenses, vLogs, vRevenues)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Financial Certificates
    val allCertificates: StateFlow<List<FinancialCertificateEntity>> = repository.getAllCertificates()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Reports: Month by month comparison
    val monthlyReports: StateFlow<List<MonthlyReportItem>> = combine(
        repository.getAllExpenses(),
        repository.getAllRevenues(),
        repository.getAllVehicleLogs()
    ) { expenses, revenues, logs ->
        calculationUseCase.calculateMonthlyReports(expenses, revenues, logs)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setMonthFilter(year: Int, month: Int) {
        _currentMonthFilter.value = MonthFilter(year, month)
    }

    fun nextMonth() {
        val current = _currentMonthFilter.value
        val newMonth = if (current.month == 12) 1 else current.month + 1
        val newYear = if (current.month == 12) current.year + 1 else current.year
        _currentMonthFilter.value = MonthFilter(newYear, newMonth)
    }

    fun previousMonth() {
        val current = _currentMonthFilter.value
        val newMonth = if (current.month == 1) 12 else current.month - 1
        val newYear = if (current.month == 1) current.year - 1 else current.year
        _currentMonthFilter.value = MonthFilter(newYear, newMonth)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDepartmentFilter(deptId: String) {
        _selectedDeptFilter.value = deptId
    }

    fun toggleSortOrder() {
        _sortByAmount.value = !_sortByAmount.value
    }

    // CRUD Expense
    fun saveExpense(
        id: Long = 0,
        amount: BigDecimal,
        date: Long,
        department: String,
        category: String,
        title: String,
        notes: String = "",
        vehicleId: Long? = null,
        studentName: String? = null,
        institution: String? = null,
        academicTerm: String? = null,
        projectName: String? = null,
        isTaxDeductible: Boolean = false
    ) {
        viewModelScope.launch {
            val entity = ExpenseEntity(
                id = id,
                amount = amount.toPlainString(),
                date = date,
                department = department,
                category = category,
                title = title,
                notes = notes,
                vehicleId = vehicleId,
                studentName = studentName,
                institution = institution,
                academicTerm = academicTerm,
                projectName = projectName,
                isTaxDeductible = isTaxDeductible
            )
            if (id == 0L) {
                repository.insertExpense(entity)
            } else {
                repository.updateExpense(entity)
            }
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            repository.deleteExpenseById(id)
        }
    }

    // CRUD Revenue
    fun saveRevenue(
        id: Long = 0,
        amount: BigDecimal,
        date: Long,
        category: String,
        title: String,
        notes: String = "",
        vehicleId: Long? = null,
        certificateId: Long? = null
    ) {
        viewModelScope.launch {
            val entity = RevenueEntity(
                id = id,
                amount = amount.toPlainString(),
                date = date,
                category = category,
                title = title,
                notes = notes,
                vehicleId = vehicleId,
                certificateId = certificateId
            )
            if (id == 0L) {
                repository.insertRevenue(entity)
            } else {
                repository.updateRevenue(entity)
            }
        }
    }

    fun deleteRevenue(id: Long) {
        viewModelScope.launch {
            repository.deleteRevenueById(id)
        }
    }

    // CRUD Vehicle
    fun saveVehicle(
        id: Long = 0,
        name: String,
        plateNumber: String,
        modelYear: String,
        initialOdometer: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val entity = VehicleEntity(
                id = id,
                name = name,
                plateNumber = plateNumber,
                modelYear = modelYear,
                initialOdometer = initialOdometer,
                notes = notes
            )
            if (id == 0L) {
                repository.insertVehicle(entity)
            } else {
                repository.updateVehicle(entity)
            }
        }
    }

    fun deleteVehicle(id: Long) {
        viewModelScope.launch {
            repository.deleteVehicleById(id)
        }
    }

    // Vehicle Logs
    fun saveVehicleLog(
        id: Long = 0,
        vehicleId: Long,
        logType: String,
        amount: BigDecimal,
        date: Long,
        odometer: Int?,
        liters: Double?,
        serviceType: String?,
        serviceCenter: String?,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val entity = VehicleLogEntity(
                id = id,
                vehicleId = vehicleId,
                logType = logType,
                amount = amount.toPlainString(),
                date = date,
                odometer = odometer,
                liters = liters,
                serviceType = serviceType,
                serviceCenter = serviceCenter,
                notes = notes
            )
            if (id == 0L) {
                repository.insertVehicleLog(entity)
            } else {
                repository.updateVehicleLog(entity)
            }
        }
    }

    fun deleteVehicleLog(id: Long) {
        viewModelScope.launch {
            repository.deleteVehicleLogById(id)
        }
    }

    // Certificates
    fun saveCertificate(
        id: Long = 0,
        name: String,
        bankName: String,
        principalAmount: BigDecimal,
        annualInterestRate: Double,
        payoutFrequency: String,
        issueDate: Long,
        maturityDate: Long,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val entity = FinancialCertificateEntity(
                id = id,
                name = name,
                bankName = bankName,
                principalAmount = principalAmount.toPlainString(),
                annualInterestRate = annualInterestRate,
                payoutFrequency = payoutFrequency,
                issueDate = issueDate,
                maturityDate = maturityDate,
                notes = notes
            )
            if (id == 0L) {
                repository.insertCertificate(entity)
            } else {
                repository.updateCertificate(entity)
            }
        }
    }

    fun deleteCertificate(id: Long) {
        viewModelScope.launch {
            repository.deleteCertificateById(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as SmartCostApplication
                return FinanceViewModel(
                    application.container.financeRepository,
                    application.container.financialCalculationUseCase
                ) as T
            }
        }
    }
}
