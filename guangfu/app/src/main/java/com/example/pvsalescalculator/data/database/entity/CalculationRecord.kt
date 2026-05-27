package com.example.pvsalescalculator.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_records")
data class CalculationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long?,
    val roofArea: Double,
    val scenario: String,
    val province: String,
    val city: String,
    val industryType: String?,
    val electricityType: String?,
    val peakPrice: Double,
    val valleyPrice: Double,
    val normalPrice: Double,
    val generationMode: String,
    val modulePower: Int,
    val inclination: Double,
    val systemEfficiency: Double,
    val installedCapacity: Double,
    val moduleCount: Int,
    val dailyGeneration: Double,
    val monthlyGeneration: Double,
    val yearlyGeneration: Double,
    val total25YearGeneration: Double,
    val totalInvestment: Double,
    val monthlyProfit: Double,
    val yearlyProfit: Double,
    val total25YearProfit: Double,
    val staticPayback: Double,
    val dynamicPayback: Double,
    val coverageRatio: Double,
    val savingRate: Double,
    val carbonReduction: Double,
    val treeEquivalent: Int,
    val createdAt: Long = System.currentTimeMillis()
)