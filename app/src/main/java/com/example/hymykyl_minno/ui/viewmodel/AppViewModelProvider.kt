package com.example.hymykyl_minno.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hymykyl_minno.FeedbackApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = feedbackApplication()
            FeedbackViewModel(
                repository = app.container.feedbackRepository,
                application = app
            )
        }
    }
}

fun CreationExtras.feedbackApplication(): FeedbackApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FeedbackApplication)
