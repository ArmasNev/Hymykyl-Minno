package com.example.hymykyl_minno.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: SurveyQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<SurveyQuestion>)

    @Update
    suspend fun updateQuestion(question: SurveyQuestion)

    @Delete
    suspend fun deleteQuestion(question: SurveyQuestion)

    @Query("SELECT * FROM survey_questions ORDER BY displayOrder ASC")
    fun getAllQuestions(): Flow<List<SurveyQuestion>>

    @Query("SELECT COUNT(*) FROM survey_questions")
    suspend fun getQuestionCount(): Int
}
