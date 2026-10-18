package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RoomDatabaseCrudTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `test expense insert, query, update, delete lifecycle`() = runBlocking {
        val expenseDao = db.expenseDao()

        // 1. Insert
        val id = expenseDao.insertExpense(
            ExpenseEntity(
                amount = "450.00",
                date = 1000L,
                department = "PERSONAL",
                category = "عناية شخصية",
                title = "مشتريات صيدلية"
            )
        )
        assertTrue(id > 0)

        // 2. Query by ID
        val retrieved = expenseDao.getExpenseById(id)
        assertNotNull(retrieved)
        assertEquals("450.00", retrieved?.amount)
        assertEquals("مشتريات صيدلية", retrieved?.title)

        // 3. Update
        val updated = retrieved!!.copy(amount = "500.00", title = "مشتريات صيدلية ومستلزمات")
        expenseDao.updateExpense(updated)

        val retrievedAfterUpdate = expenseDao.getExpenseById(id)
        assertEquals("500.00", retrievedAfterUpdate?.amount)
        assertEquals("مشتريات صيدلية ومستلزمات", retrievedAfterUpdate?.title)

        // 4. Delete
        expenseDao.deleteExpenseById(id)
        val retrievedAfterDelete = expenseDao.getExpenseById(id)
        assertNull(retrievedAfterDelete)
    }

    @Test
    fun `test date range queries filter expenses correctly`() = runBlocking {
        val expenseDao = db.expenseDao()

        // Insert expenses with different dates
        expenseDao.insertExpense(
            ExpenseEntity(amount = "100.00", date = 1000L, department = "HOME", category = "طعام", title = "1")
        )
        expenseDao.insertExpense(
            ExpenseEntity(amount = "200.00", date = 2000L, department = "HOME", category = "طعام", title = "2")
        )
        expenseDao.insertExpense(
            ExpenseEntity(amount = "300.00", date = 5000L, department = "HOME", category = "طعام", title = "3")
        )

        // Filter range between 1500L and 3000L
        val filtered = expenseDao.getExpensesBetween(1500L, 3000L).first()
        assertEquals(1, filtered.size)
        assertEquals("200.00", filtered[0].amount)
    }

    @Test
    fun `test vehicle and vehicle logs relations`() = runBlocking {
        val vehicleDao = db.vehicleDao()
        val vehicleLogDao = db.vehicleLogDao()

        val vId = vehicleDao.insertVehicle(
            VehicleEntity(
                name = "هيونداي إلنترا",
                plateNumber = "س ص ع 999",
                modelYear = "2024",
                initialOdometer = 5000
            )
        )

        val logId = vehicleLogDao.insertLog(
            VehicleLogEntity(
                vehicleId = vId,
                logType = "FUEL",
                amount = "300.00",
                date = 1000L,
                odometer = 5400,
                liters = 35.0
            )
        )
        assertTrue(logId > 0)

        val logsForVehicle = vehicleLogDao.getLogsByVehicle(vId).first()
        assertEquals(1, logsForVehicle.size)
        assertEquals("300.00", logsForVehicle[0].amount)
        assertEquals(35.0, logsForVehicle[0].liters ?: 0.0, 0.01)
    }
}
