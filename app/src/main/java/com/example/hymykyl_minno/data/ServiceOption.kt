package com.example.hymykyl_minno.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_options",
    indices = [Index(value = ["serviceKey"], unique = true)]
)
data class ServiceOption(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serviceKey: String,
    val textFi: String,
    val textEn: String,
    val textSv: String,
    val displayOrder: Int = 0
)
