package com.example.hymykyl_minno.data

import kotlinx.coroutines.flow.Flow

class FeedbackRepository(
    private val feedbackDao: FeedbackDao,
    private val adminUserDao: AdminUserDao,
    private val surveyQuestionDao: SurveyQuestionDao,
    private val feedbackAnswerDao: FeedbackAnswerDao
) {
    // Feedback Header
    suspend fun insertFeedback(feedback: Feedback): Long = feedbackDao.insertFeedback(feedback)
    fun getAllFeedback(): Flow<List<Feedback>> = feedbackDao.getAllFeedback()

    // Feedback Answers
    suspend fun insertFeedbackAnswers(answers: List<FeedbackAnswer>) = feedbackAnswerDao.insertAnswers(answers)
    fun getAllAnswers(): Flow<List<FeedbackAnswer>> = feedbackAnswerDao.getAllAnswers()
    suspend fun getAllAnswersDirect(): List<FeedbackAnswer> = feedbackAnswerDao.getAllAnswersDirect()

    // Admin
    suspend fun insertAdmin(admin: AdminUser) = adminUserDao.insertAdmin(admin)
    suspend fun getAdminByUsername(username: String) = adminUserDao.getAdminByUsername(username)
    suspend fun getAdminCount() = adminUserDao.getAdminCount()

    // Survey Questions
    fun getAllQuestions(): Flow<List<SurveyQuestion>> = surveyQuestionDao.getAllQuestions()
    suspend fun updateQuestion(question: SurveyQuestion) = surveyQuestionDao.updateQuestion(question)
    suspend fun insertQuestion(question: SurveyQuestion) = surveyQuestionDao.insertQuestion(question)
    suspend fun insertQuestions(questions: List<SurveyQuestion>) = surveyQuestionDao.insertQuestions(questions)
    suspend fun deleteQuestion(question: SurveyQuestion) = surveyQuestionDao.deleteQuestion(question)
    suspend fun getQuestionCount() = surveyQuestionDao.getQuestionCount()
}
