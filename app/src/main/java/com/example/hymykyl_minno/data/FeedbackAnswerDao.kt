package com.example.hymykyl_minno.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswers(answers: List<FeedbackAnswer>)

    @Query("SELECT * FROM feedback_answers WHERE feedbackId = :feedbackId")
    fun getAnswersForFeedback(feedbackId: Long): Flow<List<FeedbackAnswer>>

    @Query("SELECT * FROM feedback_answers")
    fun getAllAnswers(): Flow<List<FeedbackAnswer>>

    @Query("SELECT * FROM feedback_answers")
    suspend fun getAllAnswersDirect(): List<FeedbackAnswer>
}
