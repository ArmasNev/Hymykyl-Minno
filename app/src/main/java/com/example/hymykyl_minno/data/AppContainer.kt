package com.example.hymykyl_minno.data

import android.content.Context

interface AppContainer {
    val feedbackRepository: FeedbackRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val feedbackRepository: FeedbackRepository by lazy {
        val db = FeedbackDatabase.getDatabase(context)
        FeedbackRepository(
            feedbackDao = db.feedbackDao(),
            adminUserDao = db.adminUserDao(),
            surveyQuestionDao = db.surveyQuestionDao(),
            feedbackAnswerDao = db.feedbackAnswerDao()
        )
    }
}
