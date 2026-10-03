package com.example.hymykyl_minno.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hymykyl_minno.data.Feedback
import com.example.hymykyl_minno.data.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedbackUiState(
    val npsRating: Int = 0,
    val selectedService: String = "",
    val accessibilityRating: Int = 0,
    val fluencyRating: Int = 0,
    val heardRating: Int = 0,
    val returnRating: Int = 0,
    val comment: String = "",
    val isSubmitted: Boolean = false
)

class FeedbackViewModel(private val repository: FeedbackRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun updateNpsRating(rating: Int) {
        _uiState.update { it.copy(npsRating = rating) }
    }

    fun updateService(service: String) {
        _uiState.update { it.copy(selectedService = service) }
    }

    fun updateDetailedRating(index: Int, rating: Int) {
        _uiState.update { state ->
            when (index) {
                0 -> state.copy(accessibilityRating = rating)
                1 -> state.copy(fluencyRating = rating)
                2 -> state.copy(heardRating = rating)
                3 -> state.copy(returnRating = rating)
                else -> state
            }
        }
    }

    fun updateComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }

    fun submitFeedback() {
        val state = _uiState.value
        if (state.isSubmitted) return // Guard against duplicate submissions

        _uiState.update { it.copy(isSubmitted = true) }

        val feedback = Feedback(
            npsRating = state.npsRating,
            service = state.selectedService,
            accessibilityRating = state.accessibilityRating,
            fluencyRating = state.fluencyRating,
            heardRating = state.heardRating,
            returnRating = state.returnRating,
            comment = state.comment
        )
        viewModelScope.launch {
            repository.insertFeedback(feedback)
        }
    }

    fun resetFeedback() {
        _uiState.value = FeedbackUiState()
    }
}
