package com.example.pvsalescalculator.data.model

data class LocationData(
    val province: String,
    val city: String,
    val annualSunshineHours: Double,
    val latitude: Double,
    val longitude: Double
)