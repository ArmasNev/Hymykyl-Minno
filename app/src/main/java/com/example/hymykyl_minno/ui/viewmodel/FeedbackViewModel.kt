package com.example.hymykyl_minno.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hymykyl_minno.data.AdminUser
import com.example.hymykyl_minno.data.Feedback
import com.example.hymykyl_minno.data.FeedbackRepository
import com.example.hymykyl_minno.data.SurveyQuestion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    val allFeedback: StateFlow<List<Feedback>> = repository.getAllFeedback()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val surveyQuestions: StateFlow<List<SurveyQuestion>> = repository.getAllQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    // Admin Authentication
    suspend fun hasAdminUser(): Boolean {
        return repository.getAdminCount() > 0
    }

    suspend fun registerAdmin(username: String, password: String) {
        repository.insertAdmin(AdminUser(username = username, passwordHash = password))
    }

    suspend fun verifyAdminLogin(username: String, password: String): Boolean {
        val admin = repository.getAdminByUsername(username) ?: return false
        return admin.passwordHash == password
    }

    // Question Management
    fun updateQuestion(question: SurveyQuestion) {
        viewModelScope.launch {
            repository.updateQuestion(question)
        }
    }

    // CSV Data Exporter
    fun generateCsvContent(feedbackList: List<Feedback>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val sb = StringBuilder()
        sb.append("ID,Päivämäärä,Palvelu,NPS_Arvio,Saatavuus,Sujuvuus,Kuulluksi_Tuleminen,Uudelleentulo,Palaute_Kommentti\n")

        for (item in feedbackList) {
            val dateStr = dateFormat.format(Date(item.timestamp))
            val cleanComment = item.comment.replace("\n", " ").replace(",", ";")
            val cleanService = item.service.replace(",", ";")
            sb.append("${item.id},\"$dateStr\",\"$cleanService\",${item.npsRating},${item.accessibilityRating},${item.fluencyRating},${item.heardRating},${item.returnRating},\"$cleanComment\"\n")
        }
        return sb.toString()
    }
}
