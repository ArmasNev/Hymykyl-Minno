package com.example.hymykyl_minno.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Feedback::class], version = 1, exportSchema = false)
abstract class FeedbackDatabase : RoomDatabase() {
    abstract fun feedbackDao(): FeedbackDao

    companion object {
        @Volatile
        private var Instance: FeedbackDatabase? = null

        fun getDatabase(context: Context): FeedbackDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FeedbackDatabase::class.java, "feedback_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
