package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey_questions")
data class SurveyQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionKey: String,
    val textFi: String,
    val textEn: String,
    val textSv: String,
    val displayOrder: Int = 0
)
