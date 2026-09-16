package com.example.hymykyl_minno.data

import kotlinx.coroutines.flow.Flow

class FeedbackRepository(private val feedbackDao: FeedbackDao) {
    suspend fun insertFeedback(feedback: Feedback) = feedbackDao.insertFeedback(feedback)
    fun getAllFeedback(): Flow<List<Feedback>> = feedbackDao.getAllFeedback()
}
