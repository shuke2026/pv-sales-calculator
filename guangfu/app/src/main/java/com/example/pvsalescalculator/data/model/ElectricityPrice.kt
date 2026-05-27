package com.example.pvsalescalculator.data.model

data class ElectricityPrice(
    val province: String,
    val city: String,
    val priceType: String, // residential, commercial, industrial
    val peakPrice: Double,
    val valleyPrice: Double,
    val normalPrice: Double
)