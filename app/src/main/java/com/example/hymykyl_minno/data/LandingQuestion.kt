package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "landing_questions",
    indices = [Index(value = ["questionKey"], unique = true)]
)
data class LandingQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questionKey: String,
    val textFi: String,
    val textEn: String,
    val textSv: String,
    val optionsFi: String, // Comma-separated options (e.g. "Opiskelija, Henkilökunta, Ulkopuolinen")
    val optionsEn: String,
    val optionsSv: String,
    val displayOrder: Int = 0,
    val isArchived: Boolean = false
)
