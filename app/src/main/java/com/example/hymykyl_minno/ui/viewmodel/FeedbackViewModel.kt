package com.example.hymykyl_minno.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hymykyl_minno.data.AdminUser
import com.example.hymykyl_minno.data.Feedback
import com.example.hymykyl_minno.data.FeedbackAnswer
import com.example.hymykyl_minno.data.FeedbackRepository
import com.example.hymykyl_minno.data.ServiceOption
import com.example.hymykyl_minno.data.SurveyQuestion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FeedbackUiState(
    val selectedLanguage: String = "FI",
    val npsRating: Int = 0,
    val selectedService: String = "",
    val detailedRatings: Map<Int, Int> = emptyMap(),
    val comment: String = "",
    val isSubmitted: Boolean = false
)

class FeedbackViewModel(private val repository: FeedbackRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    val allFeedback: StateFlow<List<Feedback>> = repository.getAllFeedback()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAnswers: StateFlow<List<FeedbackAnswer>> = repository.getAllAnswers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val surveyQuestions: StateFlow<List<SurveyQuestion>> = repository.getAllQuestions()
        .map { list -> list.distinctBy { it.questionKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceOptions: StateFlow<List<ServiceOption>> = repository.getAllServices()
        .map { list -> list.distinctBy { it.serviceKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            if (repository.getQuestionCount() == 0) {
                val defaultQuestions = listOf(
                    SurveyQuestion(
                        questionKey = "ACCESSIBILITY",
                        textFi = "Palvelut olivat helposti saatavilla",
                        textEn = "Services were easily accessible",
                        textSv = "Tjänsterna var lättillgängliga",
                        displayOrder = 1
                    ),
                    SurveyQuestion(
                        questionKey = "FLUENCY",
                        textFi = "Palvelukokemus oli mielestäni viihtyisä ja sujuva",
                        textEn = "The service experience was pleasant and smooth",
                        textSv = "Tjänsteupplevelsen var trevlig och smidig",
                        displayOrder = 2
                    ),
                    SurveyQuestion(
                        questionKey = "HEARD",
                        textFi = "Koen saaneeni toiveeni tarvittaessa kuulluksi",
                        textEn = "I feel my wishes were heard if needed",
                        textSv = "Jag känner att mina önskemål blev hörda",
                        displayOrder = 3
                    ),
                    SurveyQuestion(
                        questionKey = "RETURN",
                        textFi = "Haluaisin tulla uudelleen",
                        textEn = "I would like to come again",
                        textSv = "Jag skulle vilja komma igen",
                        displayOrder = 4
                    )
                )
                repository.insertQuestions(defaultQuestions)
            }

            if (repository.getServiceCount() == 0) {
                val defaultServices = listOf(
                    ServiceOption(
                        serviceKey = "SERVICE_1",
                        textFi = "Suun terveydenhuolto",
                        textEn = "Oral Health Care",
                        textSv = "Munhälsovård",
                        displayOrder = 1
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_2",
                        textFi = "Fysioterapia & osteopatia",
                        textEn = "Physiotherapy & Osteopathy",
                        textSv = "Fysioterapi & osteopati",
                        displayOrder = 2
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_3",
                        textFi = "Senioripalvelut",
                        textEn = "Senior Services",
                        textSv = "Seniortjänster",
                        displayOrder = 3
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_4",
                        textFi = "Perhepalvelut",
                        textEn = "Family Services",
                        textSv = "Familjetjänster",
                        displayOrder = 4
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_5",
                        textFi = "Aikuisneuvola",
                        textEn = "Adult Health Clinic",
                        textSv = "Vuxenrådgivning",
                        displayOrder = 5
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_6",
                        textFi = "Toimintaterapia",
                        textEn = "Occupational Therapy",
                        textSv = "Arbetsterapi",
                        displayOrder = 6
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_7",
                        textFi = "Apuvälinepalvelut",
                        textEn = "Assistive Device Services",
                        textSv = "Hjälpmedelstjänster",
                        displayOrder = 7
                    ),
                    ServiceOption(
                        serviceKey = "SERVICE_8",
                        textFi = "Matalan kynnyksen palvelut",
                        textEn = "Low-Threshold Services",
                        textSv = "Lågsköskeltjänster",
                        displayOrder = 8
                    )
                )
                repository.insertServices(defaultServices)
            }
        }
    }

    fun updateLanguage(lang: String) {
        _uiState.update { it.copy(selectedLanguage = lang) }
    }

    fun updateNpsRating(rating: Int) {
        _uiState.update { it.copy(npsRating = rating) }
    }

    fun updateService(service: String) {
        _uiState.update { it.copy(selectedService = service) }
    }

    fun updateDetailedRating(index: Int, rating: Int) {
        _uiState.update { state ->
            val updatedMap = state.detailedRatings.toMutableMap()
            updatedMap[index] = rating
            state.copy(detailedRatings = updatedMap)
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
            accessibilityRating = state.detailedRatings[0] ?: 0,
            fluencyRating = state.detailedRatings[1] ?: 0,
            heardRating = state.detailedRatings[2] ?: 0,
            returnRating = state.detailedRatings[3] ?: 0,
            comment = state.comment,
            languageUsed = state.selectedLanguage,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            val feedbackId = repository.insertFeedback(feedback)
            val currentQuestions = repository.getAllQuestions().first().distinctBy { it.questionKey }

            val answerList = mutableListOf<FeedbackAnswer>()
            for (i in currentQuestions.indices) {
                val rating = state.detailedRatings[i] ?: 0
                if (rating > 0) {
                    val question = currentQuestions[i]
                    val qText = when (state.selectedLanguage) {
                        "EN" -> question.textEn.ifEmpty { question.textFi }
                        "SV" -> question.textSv.ifEmpty { question.textFi }
                        else -> question.textFi
                    }
                    answerList.add(
                        FeedbackAnswer(
                            feedbackId = feedbackId,
                            questionId = question.id,
                            questionTextSnapshot = qText,
                            rating = rating
                        )
                    )
                }
            }
            if (answerList.isNotEmpty()) {
                repository.insertFeedbackAnswers(answerList)
            }
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

    fun addQuestion(textFi: String, textEn: String, textSv: String) {
        viewModelScope.launch {
            val currentList = surveyQuestions.value
            val nextOrder = (currentList.maxOfOrNull { it.displayOrder } ?: 0) + 1
            val newQuestion = SurveyQuestion(
                questionKey = "QUESTION_${System.currentTimeMillis()}",
                textFi = textFi,
                textEn = textEn,
                textSv = textSv,
                displayOrder = nextOrder
            )
            repository.insertQuestion(newQuestion)
        }
    }

    fun deleteQuestion(question: SurveyQuestion) {
        viewModelScope.launch {
            repository.deleteQuestion(question)
        }
    }

    fun moveQuestionUp(question: SurveyQuestion) {
        viewModelScope.launch {
            val list = surveyQuestions.value
            val index = list.indexOfFirst { it.id == question.id }
            if (index > 0) {
                val prev = list[index - 1]
                val tempOrder = question.displayOrder
                repository.updateQuestion(question.copy(displayOrder = prev.displayOrder))
                repository.updateQuestion(prev.copy(displayOrder = tempOrder))
            }
        }
    }

    fun moveQuestionDown(question: SurveyQuestion) {
        viewModelScope.launch {
            val list = surveyQuestions.value
            val index = list.indexOfFirst { it.id == question.id }
            if (index >= 0 && index < list.size - 1) {
                val next = list[index + 1]
                val tempOrder = question.displayOrder
                repository.updateQuestion(question.copy(displayOrder = next.displayOrder))
                repository.updateQuestion(next.copy(displayOrder = tempOrder))
            }
        }
    }

    // Service Option Management
    fun updateServiceOption(service: ServiceOption) {
        viewModelScope.launch {
            repository.updateService(service)
        }
    }

    fun addServiceOption(textFi: String, textEn: String, textSv: String) {
        viewModelScope.launch {
            val currentList = serviceOptions.value
            val nextOrder = (currentList.maxOfOrNull { it.displayOrder } ?: 0) + 1
            val newService = ServiceOption(
                serviceKey = "SERVICE_${System.currentTimeMillis()}",
                textFi = textFi,
                textEn = textEn,
                textSv = textSv,
                displayOrder = nextOrder
            )
            repository.insertService(newService)
        }
    }

    fun deleteServiceOption(service: ServiceOption) {
        viewModelScope.launch {
            repository.deleteService(service)
        }
    }

    fun moveServiceUp(service: ServiceOption) {
        viewModelScope.launch {
            val list = serviceOptions.value
            val index = list.indexOfFirst { it.id == service.id }
            if (index > 0) {
                val prev = list[index - 1]
                val tempOrder = service.displayOrder
                repository.updateService(service.copy(displayOrder = prev.displayOrder))
                repository.updateService(prev.copy(displayOrder = tempOrder))
            }
        }
    }

    fun moveServiceDown(service: ServiceOption) {
        viewModelScope.launch {
            val list = serviceOptions.value
            val index = list.indexOfFirst { it.id == service.id }
            if (index >= 0 && index < list.size - 1) {
                val next = list[index + 1]
                val tempOrder = service.displayOrder
                repository.updateService(service.copy(displayOrder = next.displayOrder))
                repository.updateService(next.copy(displayOrder = tempOrder))
            }
        }
    }

    // CSV Data Exporter (Wide Format - 1 Clean Row Per Submission)
    suspend fun generateCsvContent(feedbackList: List<Feedback>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val freshAnswers = repository.getAllAnswersDirect()
        val answersMap = freshAnswers.groupBy { it.feedbackId }
        val currentQuestions = repository.getAllQuestions().first().distinctBy { it.questionKey }

        val sb = StringBuilder()

        // Header
        sb.append("Palaute_ID,Päivämäärä,Kieli,Palvelu,NPS_Arvio")
        for (i in currentQuestions.indices) {
            val q = currentQuestions[i]
            val cleanQText = q.textFi.replace("\n", " ").replace(",", ";")
            sb.append(",\"K${i + 1}: $cleanQText\"")
        }
        sb.append(",Kommentti\n")

        // Rows
        for (item in feedbackList) {
            val dateStr = dateFormat.format(Date(item.timestamp))
            val cleanComment = item.comment.replace("\n", " ").replace(",", ";")
            val cleanService = item.service.replace(",", ";")

            sb.append("${item.id},\"$dateStr\",\"${item.languageUsed}\",\"$cleanService\",${item.npsRating}")

            val itemAnswers = answersMap[item.id]
            for (i in currentQuestions.indices) {
                val q = currentQuestions[i]
                val ans = itemAnswers?.find {
                    it.questionId == q.id ||
                    it.questionTextSnapshot.equals(q.textFi, ignoreCase = true) ||
                    it.questionTextSnapshot.equals(q.textEn, ignoreCase = true) ||
                    it.questionTextSnapshot.equals(q.textSv, ignoreCase = true)
                } ?: itemAnswers?.find { it.questionId == (i + 1) }

                val rating = when {
                    ans != null -> ans.rating.toString()
                    (i == 0) && (item.accessibilityRating > 0) -> item.accessibilityRating.toString()
                    (i == 1) && (item.fluencyRating > 0) -> item.fluencyRating.toString()
                    (i == 2) && (item.heardRating > 0) -> item.heardRating.toString()
                    (i == 3) && (item.returnRating > 0) -> item.returnRating.toString()
                    else -> ""
                }
                sb.append(",$rating")
            }

            sb.append(",\"$cleanComment\"\n")
        }
        return sb.toString()
    }
}
