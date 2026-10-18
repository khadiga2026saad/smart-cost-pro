package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.FinancialCertificateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CertificateDao {
    @Query("SELECT * FROM financial_certificates ORDER BY issueDate DESC, id DESC")
    fun getAllCertificates(): Flow<List<FinancialCertificateEntity>>

    @Query("SELECT * FROM financial_certificates WHERE id = :id LIMIT 1")
    suspend fun getCertificateById(id: Long): FinancialCertificateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(certificate: FinancialCertificateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificates(certificates: List<FinancialCertificateEntity>)

    @Update
    suspend fun updateCertificate(certificate: FinancialCertificateEntity)

    @Delete
    suspend fun deleteCertificate(certificate: FinancialCertificateEntity)

    @Query("DELETE FROM financial_certificates WHERE id = :id")
    suspend fun deleteCertificateById(id: Long)
}
