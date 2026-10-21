package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback_answers")
data class FeedbackAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val feedbackId: Long,
    val questionId: Int,
    val questionTextSnapshot: String,
    val rating: Int
)
