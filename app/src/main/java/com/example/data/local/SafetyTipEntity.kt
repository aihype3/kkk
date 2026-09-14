package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safety_tips")
data class SafetyTipEntity(
    @PrimaryKey val id: String,
    val iconEmoji: String,
    val isDanger: Boolean,
    val titleHi: String,
    val titleMr: String,
    val titleEn: String,
    val descHi: String,
    val descMr: String,
    val descEn: String,
    val audioHi: String,
    val audioMr: String,
    val audioEn: String
)
