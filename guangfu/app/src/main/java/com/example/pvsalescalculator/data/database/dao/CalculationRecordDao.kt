package com.example.pvsalescalculator.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationRecordDao {
    @Insert
    suspend fun insert(record: CalculationRecord): Long

    @Update
    suspend fun update(record: CalculationRecord)

    @Delete
    suspend fun delete(record: CalculationRecord)

    @Query("SELECT * FROM calculation_records ORDER BY createdAt DESC")
    fun getAllRecords(): Flow<List<CalculationRecord>>

    @Query("SELECT * FROM calculation_records WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getRecordsByClient(clientId: Long): Flow<List<CalculationRecord>>

    @Query("SELECT * FROM calculation_records WHERE id = :id")
    suspend fun getRecordById(id: Long): CalculationRecord?
}