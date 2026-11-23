package com.example.hymykyl_minno.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LandingQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: LandingQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<LandingQuestion>)

    @Update
    suspend fun updateQuestion(question: LandingQuestion)

    @Delete
    suspend fun deleteQuestion(question: LandingQuestion)

    @Query("SELECT * FROM landing_questions ORDER BY displayOrder ASC")
    fun getAllQuestions(): Flow<List<LandingQuestion>>

    @Query("SELECT COUNT(*) FROM landing_questions WHERE isArchived = 0")
    suspend fun getActiveQuestionCount(): Int
}
