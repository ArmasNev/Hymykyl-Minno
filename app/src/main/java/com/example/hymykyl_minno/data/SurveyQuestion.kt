package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "survey_questions",
    indices = [Index(value = ["questionKey"], unique = true)]
)
data class SurveyQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionKey: String,
    val textFi: String,
    val textEn: String,
    val textSv: String,
    val displayOrder: Int = 0
)
