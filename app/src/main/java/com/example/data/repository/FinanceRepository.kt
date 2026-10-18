package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.FinancialCertificateEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    // Expenses
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    fun getExpensesBetween(startEpoch: Long, endEpoch: Long): Flow<List<ExpenseEntity>>
    fun getExpensesByDepartment(department: String): Flow<List<ExpenseEntity>>
    fun getExpensesByDepartmentBetween(department: String, startEpoch: Long, endEpoch: Long): Flow<List<ExpenseEntity>>
    fun getExpensesByVehicle(vehicleId: Long): Flow<List<ExpenseEntity>>
    suspend fun getExpenseById(id: Long): ExpenseEntity?
    suspend fun insertExpense(expense: ExpenseEntity): Long
    suspend fun updateExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)
    suspend fun deleteExpenseById(id: Long)

    // Revenues
    fun getAllRevenues(): Flow<List<RevenueEntity>>
    fun getRevenuesBetween(startEpoch: Long, endEpoch: Long): Flow<List<RevenueEntity>>
    fun getRevenuesByVehicle(vehicleId: Long): Flow<List<RevenueEntity>>
    suspend fun getRevenueById(id: Long): RevenueEntity?
    suspend fun insertRevenue(revenue: RevenueEntity): Long
    suspend fun updateRevenue(revenue: RevenueEntity)
    suspend fun deleteRevenue(revenue: RevenueEntity)
    suspend fun deleteRevenueById(id: Long)

    // Vehicles
    fun getAllVehicles(): Flow<List<VehicleEntity>>
    suspend fun getVehicleById(id: Long): VehicleEntity?
    suspend fun insertVehicle(vehicle: VehicleEntity): Long
    suspend fun updateVehicle(vehicle: VehicleEntity)
    suspend fun deleteVehicle(vehicle: VehicleEntity)
    suspend fun deleteVehicleById(id: Long)

    // Vehicle Logs (Fuel & Maintenance)
    fun getAllVehicleLogs(): Flow<List<VehicleLogEntity>>
    fun getVehicleLogs(vehicleId: Long): Flow<List<VehicleLogEntity>>
    fun getVehicleLogsBetween(startEpoch: Long, endEpoch: Long): Flow<List<VehicleLogEntity>>
    fun getVehicleLogsByVehicleBetween(vehicleId: Long, startEpoch: Long, endEpoch: Long): Flow<List<VehicleLogEntity>>
    suspend fun insertVehicleLog(log: VehicleLogEntity): Long
    suspend fun updateVehicleLog(log: VehicleLogEntity)
    suspend fun deleteVehicleLog(log: VehicleLogEntity)
    suspend fun deleteVehicleLogById(id: Long)

    // Certificates
    fun getAllCertificates(): Flow<List<FinancialCertificateEntity>>
    suspend fun getCertificateById(id: Long): FinancialCertificateEntity?
    suspend fun insertCertificate(certificate: FinancialCertificateEntity): Long
    suspend fun updateCertificate(certificate: FinancialCertificateEntity)
    suspend fun deleteCertificate(certificate: FinancialCertificateEntity)
    suspend fun deleteCertificateById(id: Long)
}

class FinanceRepositoryImpl(private val database: AppDatabase) : FinanceRepository {
    private val expenseDao = database.expenseDao()
    private val revenueDao = database.revenueDao()
    private val vehicleDao = database.vehicleDao()
    private val vehicleLogDao = database.vehicleLogDao()
    private val certificateDao = database.certificateDao()

    override fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    override fun getExpensesBetween(startEpoch: Long, endEpoch: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesBetween(startEpoch, endEpoch)

    override fun getExpensesByDepartment(department: String): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByDepartment(department)

    override fun getExpensesByDepartmentBetween(
        department: String,
        startEpoch: Long,
        endEpoch: Long
    ): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByDepartmentBetween(department, startEpoch, endEpoch)

    override fun getExpensesByVehicle(vehicleId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByVehicle(vehicleId)

    override suspend fun getExpenseById(id: Long): ExpenseEntity? = expenseDao.getExpenseById(id)

    override suspend fun insertExpense(expense: ExpenseEntity): Long = expenseDao.insertExpense(expense)

    override suspend fun updateExpense(expense: ExpenseEntity) = expenseDao.updateExpense(expense)

    override suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)

    override suspend fun deleteExpenseById(id: Long) = expenseDao.deleteExpenseById(id)

    override fun getAllRevenues(): Flow<List<RevenueEntity>> = revenueDao.getAllRevenues()

    override fun getRevenuesBetween(startEpoch: Long, endEpoch: Long): Flow<List<RevenueEntity>> =
        revenueDao.getRevenuesBetween(startEpoch, endEpoch)

    override fun getRevenuesByVehicle(vehicleId: Long): Flow<List<RevenueEntity>> =
        revenueDao.getRevenuesByVehicle(vehicleId)

    override suspend fun getRevenueById(id: Long): RevenueEntity? = revenueDao.getRevenueById(id)

    override suspend fun insertRevenue(revenue: RevenueEntity): Long = revenueDao.insertRevenue(revenue)

    override suspend fun updateRevenue(revenue: RevenueEntity) = revenueDao.updateRevenue(revenue)

    override suspend fun deleteRevenue(revenue: RevenueEntity) = revenueDao.deleteRevenue(revenue)

    override suspend fun deleteRevenueById(id: Long) = revenueDao.deleteRevenueById(id)

    override fun getAllVehicles(): Flow<List<VehicleEntity>> = vehicleDao.getAllVehicles()

    override suspend fun getVehicleById(id: Long): VehicleEntity? = vehicleDao.getVehicleById(id)

    override suspend fun insertVehicle(vehicle: VehicleEntity): Long = vehicleDao.insertVehicle(vehicle)

    override suspend fun updateVehicle(vehicle: VehicleEntity) = vehicleDao.updateVehicle(vehicle)

    override suspend fun deleteVehicle(vehicle: VehicleEntity) = vehicleDao.deleteVehicle(vehicle)

    override suspend fun deleteVehicleById(id: Long) = vehicleDao.deleteVehicleById(id)

    override fun getAllVehicleLogs(): Flow<List<VehicleLogEntity>> = vehicleLogDao.getAllLogs()

    override fun getVehicleLogs(vehicleId: Long): Flow<List<VehicleLogEntity>> =
        vehicleLogDao.getLogsByVehicle(vehicleId)

    override fun getVehicleLogsBetween(startEpoch: Long, endEpoch: Long): Flow<List<VehicleLogEntity>> =
        vehicleLogDao.getLogsBetween(startEpoch, endEpoch)

    override fun getVehicleLogsByVehicleBetween(
        vehicleId: Long,
        startEpoch: Long,
        endEpoch: Long
    ): Flow<List<VehicleLogEntity>> =
        vehicleLogDao.getLogsByVehicleBetween(vehicleId, startEpoch, endEpoch)

    override suspend fun insertVehicleLog(log: VehicleLogEntity): Long = vehicleLogDao.insertLog(log)

    override suspend fun updateVehicleLog(log: VehicleLogEntity) = vehicleLogDao.updateLog(log)

    override suspend fun deleteVehicleLog(log: VehicleLogEntity) = vehicleLogDao.deleteLog(log)

    override suspend fun deleteVehicleLogById(id: Long) = vehicleLogDao.deleteLogById(id)

    override fun getAllCertificates(): Flow<List<FinancialCertificateEntity>> = certificateDao.getAllCertificates()

    override suspend fun getCertificateById(id: Long): FinancialCertificateEntity? = certificateDao.getCertificateById(id)

    override suspend fun insertCertificate(certificate: FinancialCertificateEntity): Long =
        certificateDao.insertCertificate(certificate)

    override suspend fun updateCertificate(certificate: FinancialCertificateEntity) =
        certificateDao.updateCertificate(certificate)

    override suspend fun deleteCertificate(certificate: FinancialCertificateEntity) =
        certificateDao.deleteCertificate(certificate)

    override suspend fun deleteCertificateById(id: Long) = certificateDao.deleteCertificateById(id)
}
