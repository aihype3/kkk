package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "price_rates")
data class PriceRateEntity(
    @PrimaryKey val materialId: String,
    val materialName: String,
    val pricePerKg: Int,
    val trend: String, // "UP", "STABLE", "DOWN"
    val changePercent: Double,
    val lastUpdated: String, // "Today, 10:00 AM"
    val location: String = "Indore Mandi / Formal Hub",
    val juneRate: Int,
    val julyRate: Int,
    val augustRate: Int,
    val septRate: Int
)
