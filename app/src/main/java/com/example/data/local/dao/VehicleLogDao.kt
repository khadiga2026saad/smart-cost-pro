package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.VehicleLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleLogDao {
    @Query("SELECT * FROM vehicle_logs ORDER BY date DESC, id DESC")
    fun getAllLogs(): Flow<List<VehicleLogEntity>>

    @Query("SELECT * FROM vehicle_logs WHERE vehicleId = :vehicleId ORDER BY date DESC, id DESC")
    fun getLogsByVehicle(vehicleId: Long): Flow<List<VehicleLogEntity>>

    @Query("SELECT * FROM vehicle_logs WHERE logType = :type ORDER BY date DESC, id DESC")
    fun getLogsByType(type: String): Flow<List<VehicleLogEntity>>

    @Query("SELECT * FROM vehicle_logs WHERE date BETWEEN :startEpoch AND :endEpoch ORDER BY date DESC, id DESC")
    fun getLogsBetween(startEpoch: Long, endEpoch: Long): Flow<List<VehicleLogEntity>>

    @Query("SELECT * FROM vehicle_logs WHERE vehicleId = :vehicleId AND date BETWEEN :startEpoch AND :endEpoch ORDER BY date DESC, id DESC")
    fun getLogsByVehicleBetween(vehicleId: Long, startEpoch: Long, endEpoch: Long): Flow<List<VehicleLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: VehicleLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<VehicleLogEntity>)

    @Update
    suspend fun updateLog(log: VehicleLogEntity)

    @Delete
    suspend fun deleteLog(log: VehicleLogEntity)

    @Query("DELETE FROM vehicle_logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)
}
