package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.core.DateUtils
import com.example.data.local.dao.CertificateDao
import com.example.data.local.dao.ExpenseDao
import com.example.data.local.dao.RevenueDao
import com.example.data.local.dao.VehicleDao
import com.example.data.local.dao.VehicleLogDao
import com.example.data.local.entities.ExpenseEntity
import com.example.data.local.entities.FinancialCertificateEntity
import com.example.data.local.entities.RevenueEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VehicleLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ExpenseEntity::class,
        RevenueEntity::class,
        VehicleEntity::class,
        VehicleLogEntity::class,
        FinancialCertificateEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun revenueDao(): RevenueDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun vehicleLogDao(): VehicleLogDao
    abstract fun certificateDao(): CertificateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_cost_pro_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(database: AppDatabase) {
            val now = DateUtils.currentEpochMillis()
            val dayMillis = 86_400_000L

            // 1. Starter Vehicle
            val vehicleId = database.vehicleDao().insertVehicle(
                VehicleEntity(
                    name = "تويوتا كورولا",
                    plateNumber = "أ ب ج 4589",
                    modelYear = "2023",
                    initialOdometer = 35400,
                    notes = "سيارة العمل والتنقل اليومي"
                )
            )

            // 2. Starter Expenses across specialized departments
            val initialExpenses = listOf(
                ExpenseEntity(
                    amount = "1450.00",
                    date = now - (dayMillis * 1),
                    department = "HOME",
                    category = "تموين ومستلزمات",
                    title = "مشتريات غذائية شهرية",
                    notes = "سوبرماركت الهايبر"
                ),
                ExpenseEntity(
                    amount = "650.00",
                    date = now - (dayMillis * 2),
                    department = "HOME",
                    category = "فواتير ومرافق",
                    title = "فاتورة الكهرباء والإنترنت",
                    notes = "سداد إلكتروني"
                ),
                ExpenseEntity(
                    amount = "420.00",
                    date = now - (dayMillis * 3),
                    department = "PERSONAL",
                    category = "عناية شخصية",
                    title = "مشتريات صيدلية وصحة",
                    notes = "فيتامينات ومستلزمات"
                ),
                ExpenseEntity(
                    amount = "3500.00",
                    date = now - (dayMillis * 5),
                    department = "EDUCATION",
                    category = "رسوم دراسية",
                    title = "قسط الفصل الدراسي الأول",
                    studentName = "أحمد محمد",
                    institution = "مدرسة النور الدولية",
                    academicTerm = "الصف الثالث الثانوي",
                    notes = "إيصال رقم 8821"
                ),
                ExpenseEntity(
                    amount = "850.00",
                    date = now - (dayMillis * 6),
                    department = "EDUCATION",
                    category = "كتب ومراجع",
                    title = "كتب خارجية وأدوات مدرسية",
                    studentName = "سارة محمد",
                    institution = "المدرسة التجريبية",
                    academicTerm = "الإعدادية"
                ),
                ExpenseEntity(
                    amount = "1200.00",
                    date = now - (dayMillis * 7),
                    department = "WORK",
                    category = "معدات ومكتب",
                    title = "اشتراك برمجيات وأدوات تصميم",
                    projectName = "مشروع المنصة الرقمية",
                    isTaxDeductible = true,
                    notes = "فاتورة ضريبية معتمدة"
                ),
                ExpenseEntity(
                    amount = "350.00",
                    date = now - (dayMillis * 8),
                    department = "VEHICLE",
                    category = "غسيل ومواقف",
                    title = "اشتراك موقف وغسيل شامل",
                    vehicleId = vehicleId,
                    notes = "موقف العمل الشهري"
                )
            )
            database.expenseDao().insertExpenses(initialExpenses)

            // 3. Vehicle Logs (Fuel & Maintenance)
            val vehicleLogs = listOf(
                VehicleLogEntity(
                    vehicleId = vehicleId,
                    logType = "FUEL",
                    amount = "450.00",
                    date = now - (dayMillis * 2),
                    odometer = 35850,
                    liters = 40.0,
                    notes = "بنزين 95 محطة الوطنية"
                ),
                VehicleLogEntity(
                    vehicleId = vehicleId,
                    logType = "MAINTENANCE",
                    amount = "1650.00",
                    date = now - (dayMillis * 10),
                    odometer = 35000,
                    serviceType = "تغيير زيت المحرك وفلتر أصلي",
                    serviceCenter = "مركز تويوتا المعتمد",
                    notes = "صيانة دورية 35 ألف كم"
                )
            )
            database.vehicleLogDao().insertLogs(vehicleLogs)

            // 4. Starter Revenues
            val initialRevenues = listOf(
                RevenueEntity(
                    amount = "18500.00",
                    date = now - (dayMillis * 12),
                    category = "راتب شهري",
                    title = "راتب وظيفة الهندسة",
                    notes = "تحويل بنكي"
                ),
                RevenueEntity(
                    amount = "4200.00",
                    date = now - (dayMillis * 4),
                    category = "عمل حر واستشارات",
                    title = "دفعة استشارة مالية لشركة الشروق",
                    notes = "عقد استشاري"
                ),
                RevenueEntity(
                    amount = "2100.00",
                    date = now - (dayMillis * 3),
                    category = "إيرادات مركبات",
                    title = "إيراد تشغيل السيارة (نقل/توصيل)",
                    vehicleId = vehicleId,
                    notes = "إيراد رحلات الأسبوع"
                )
            )
            database.revenueDao().insertRevenues(initialRevenues)

            // 5. Financial Certificate
            database.certificateDao().insertCertificate(
                FinancialCertificateEntity(
                    name = "شهادة القمة البلاتينية",
                    bankName = "البنك الأهلي",
                    principalAmount = "100000.00",
                    annualInterestRate = 23.5,
                    payoutFrequency = "شهري",
                    issueDate = now - (dayMillis * 60),
                    maturityDate = now + (dayMillis * 300),
                    notes = "عائد شهري يصرف في الحساب الجاري"
                )
            )
        }
    }
}
