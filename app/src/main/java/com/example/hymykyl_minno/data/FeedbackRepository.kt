package com.example.hymykyl_minno.data

import kotlinx.coroutines.flow.Flow

class FeedbackRepository(
    private val feedbackDao: FeedbackDao,
    private val adminUserDao: AdminUserDao,
    private val surveyQuestionDao: SurveyQuestionDao
) {
    // Feedback
    suspend fun insertFeedback(feedback: Feedback) = feedbackDao.insertFeedback(feedback)
    fun getAllFeedback(): Flow<List<Feedback>> = feedbackDao.getAllFeedback()

    // Admin
    suspend fun insertAdmin(admin: AdminUser) = adminUserDao.insertAdmin(admin)
    suspend fun getAdminByUsername(username: String) = adminUserDao.getAdminByUsername(username)
    suspend fun getAdminCount() = adminUserDao.getAdminCount()

    // Survey Questions
    fun getAllQuestions(): Flow<List<SurveyQuestion>> = surveyQuestionDao.getAllQuestions()
    suspend fun updateQuestion(question: SurveyQuestion) = surveyQuestionDao.updateQuestion(question)
    suspend fun insertQuestions(questions: List<SurveyQuestion>) = surveyQuestionDao.insertQuestions(questions)
    suspend fun getQuestionCount() = surveyQuestionDao.getQuestionCount()
}
