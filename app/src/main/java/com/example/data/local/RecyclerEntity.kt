package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recyclers")
data class RecyclerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val isAuthorized: Boolean = true,
    val authorizationRegNo: String = "MPPCB/E-WASTE/2024/091",
    val distanceKm: Double,
    val materialsAccepted: String, // "PCB, Cable, Battery"
    val offeredRatePerKg: Int, // for preferred material
    val pickupAvailable: Boolean = true,
    val rating: Double = 4.8,
    val contactPhone: String = "+91 98260 12345",
    val address: String = "Plot 42, Sanwer Road Industrial Area, Sector E, Indore",
    val operatingHours: String = "8:00 AM – 7:30 PM",
    val reviewsCount: Int = 142
)
