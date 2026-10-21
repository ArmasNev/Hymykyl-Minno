package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val npsRating: Int,
    val service: String,
    val accessibilityRating: Int = 0,
    val fluencyRating: Int = 0,
    val heardRating: Int = 0,
    val returnRating: Int = 0,
    val comment: String,
    val languageUsed: String = "FI",
    val timestamp: Long = System.currentTimeMillis()
)
