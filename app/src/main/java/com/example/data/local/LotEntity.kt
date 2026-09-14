package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lots")
data class LotEntity(
    @PrimaryKey val lotId: String, // e.g. "KW-2026-00125"
    val materialCategoryId: String,
    val subCategory: String,
    val approximateWeightKg: Double,
    val condition: String, // Good, Used, Damaged
    val sourceType: String, // Household, Shop, Office, Industrial, Other
    val description: String = "",
    val photoUri: String = "",
    val quotedRatePerKg: Int,
    val estimatedMinPrice: Int,
    val estimatedMaxPrice: Int,
    val finalPrice: Int,
    val recyclerId: String,
    val recyclerName: String,
    val pickupRequired: Boolean = true,
    val status: String, // "CREATED", "HANDOVER_PENDING", "CONFIRMED", "PAID", "RECYCLED"
    val paymentMethod: String = "Cash", // "Cash", "Digital"
    val paymentStatus: String = "Pending", // "Paid", "Pending"
    val timestamp: Long = System.currentTimeMillis(),
    val location: String = "Indore, MP",
    val gpsCoordinates: String = "22.7196° N, 75.8577° E",
    val handoverReference: String = "",
    val isSynced: Boolean = true
)
