package com.example.pvsalescalculator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pvsalescalculator.data.database.dao.CalculationRecordDao
import com.example.pvsalescalculator.data.database.dao.ClientDao
import com.example.pvsalescalculator.data.database.entity.CalculationRecord
import com.example.pvsalescalculator.data.database.entity.Client

@Database(
    entities = [Client::class, CalculationRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun calculationRecordDao(): CalculationRecordDao
}