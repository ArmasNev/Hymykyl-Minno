package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val npsRating: Int,
    val service: String,
    val accessibilityRating: Int,
    val fluencyRating: Int,
    val heardRating: Int,
    val returnRating: Int,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)
