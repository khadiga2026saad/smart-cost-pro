package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.RevenueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RevenueDao {
    @Query("SELECT * FROM revenues ORDER BY date DESC, id DESC")
    fun getAllRevenues(): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenues WHERE date BETWEEN :startEpoch AND :endEpoch ORDER BY date DESC, id DESC")
    fun getRevenuesBetween(startEpoch: Long, endEpoch: Long): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenues WHERE vehicleId = :vehicleId ORDER BY date DESC")
    fun getRevenuesByVehicle(vehicleId: Long): Flow<List<RevenueEntity>>

    @Query("SELECT * FROM revenues WHERE id = :id LIMIT 1")
    suspend fun getRevenueById(id: Long): RevenueEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenue(revenue: RevenueEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevenues(revenues: List<RevenueEntity>)

    @Update
    suspend fun updateRevenue(revenue: RevenueEntity)

    @Delete
    suspend fun deleteRevenue(revenue: RevenueEntity)

    @Query("DELETE FROM revenues WHERE id = :id")
    suspend fun deleteRevenueById(id: Long)
}
