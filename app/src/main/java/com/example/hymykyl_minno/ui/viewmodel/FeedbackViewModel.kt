package com.example.hymykyl_minno.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hymykyl_minno.data.AdminUser
import com.example.hymykyl_minno.data.Feedback
import com.example.hymykyl_minno.data.FeedbackAnswer
import com.example.hymykyl_minno.data.FeedbackRepository
import com.example.hymykyl_minno.data.LandingQuestion
import com.example.hymykyl_minno.data.ServiceOption
import com.example.hymykyl_minno.data.SurveyQuestion
import com.example.hymykyl_minno.ui.localization.AppStrings
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
    val landingAnswers: Map<Int, String> = emptyMap(),
    val comment: String = "",
    val isSubmitted: Boolean = false,
    val animationStyle: String = "FADE_EXPAND",
    val sidebarUpdateTrigger: Long = 0L
)

class FeedbackViewModel(
    private val repository: FeedbackRepository,
    private val application: Application? = null
) : ViewModel() {

    private val prefs by lazy {
        application?.getSharedPreferences("hymykyla_prefs", Context.MODE_PRIVATE)
    }

    private val initialAnimStyle = prefs?.getString("ANIMATION_STYLE", "FADE_EXPAND") ?: "FADE_EXPAND"

    private val _uiState = MutableStateFlow(FeedbackUiState(animationStyle = initialAnimStyle))
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    val allFeedback: StateFlow<List<Feedback>> = repository.getAllFeedback()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAnswers: StateFlow<List<FeedbackAnswer>> = repository.getAllAnswers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val surveyQuestions: StateFlow<List<SurveyQuestion>> = repository.getAllQuestions()
        .map { list -> list.distinctBy { it.questionKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeSurveyQuestions: StateFlow<List<SurveyQuestion>> = repository.getAllQuestions()
        .map { list -> list.filter { !it.isArchived }.distinctBy { it.questionKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceOptions: StateFlow<List<ServiceOption>> = repository.getAllServices()
        .map { list -> list.distinctBy { it.serviceKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val landingQuestions: StateFlow<List<LandingQuestion>> = repository.getAllLandingQuestions()
        .map { list -> list.distinctBy { it.questionKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeLandingQuestions: StateFlow<List<LandingQuestion>> = repository.getAllLandingQuestions()
        .map { list -> list.filter { !it.isArchived }.take(3).distinctBy { it.questionKey } }
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

    // Sidebar Customization
    fun getSidebarTitle(lang: String): String {
        val key = "SIDEBAR_TITLE_$lang"
        val stored = prefs?.getString(key, null)
        return if (!stored.isNullOrBlank()) stored else AppStrings.sidebarTitle(lang)
    }

    fun getSidebarDesc(lang: String): String {
        val key = "SIDEBAR_DESC_$lang"
        val stored = prefs?.getString(key, null)
        return if (!stored.isNullOrBlank()) stored else AppStrings.sidebarDesc(lang)
    }

    fun saveSidebarTexts(
        titleFi: String, descFi: String,
        titleEn: String, descEn: String,
        titleSv: String, descSv: String
    ) {
        prefs?.edit()
            ?.putString("SIDEBAR_TITLE_FI", titleFi.trim())
            ?.putString("SIDEBAR_DESC_FI", descFi.trim())
            ?.putString("SIDEBAR_TITLE_EN", titleEn.trim())
            ?.putString("SIDEBAR_DESC_EN", descEn.trim())
            ?.putString("SIDEBAR_TITLE_SV", titleSv.trim())
            ?.putString("SIDEBAR_DESC_SV", descSv.trim())
            ?.apply()
        _uiState.update { it.copy(sidebarUpdateTrigger = System.currentTimeMillis()) }
    }

    fun resetSidebarTexts() {
        prefs?.edit()
            ?.remove("SIDEBAR_TITLE_FI")?.remove("SIDEBAR_DESC_FI")
            ?.remove("SIDEBAR_TITLE_EN")?.remove("SIDEBAR_DESC_EN")
            ?.remove("SIDEBAR_TITLE_SV")?.remove("SIDEBAR_DESC_SV")
            ?.apply()
        _uiState.update { it.copy(sidebarUpdateTrigger = System.currentTimeMillis()) }
    }

    fun updateLanguage(lang: String) {
        _uiState.update { it.copy(selectedLanguage = lang) }
    }

    fun updateAnimationStyle(style: String) {
        _uiState.update { it.copy(animationStyle = style) }
        prefs?.edit()?.putString("ANIMATION_STYLE", style)?.apply()
    }

    fun updateNpsRating(rating: Int) {
        _uiState.update { it.copy(npsRating = rating) }
    }

    fun updateLandingAnswer(questionId: Int, choice: String) {
        _uiState.update { state ->
            val updatedMap = state.landingAnswers.toMutableMap()
            if (updatedMap[questionId] == choice) {
                updatedMap.remove(questionId) // Deselect if tapped again
            } else {
                updatedMap[questionId] = choice
            }
            state.copy(landingAnswers = updatedMap)
        }
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

            // Save landing question selections
            for ((lqId, choice) in state.landingAnswers) {
                val lq = activeLandingQuestions.value.find { it.id == lqId }
                val lqText = lq?.textFi ?: "Sivu 1 kysymys"
                answerList.add(
                    FeedbackAnswer(
                        feedbackId = feedbackId,
                        questionId = 1000 + lqId,
                        questionTextSnapshot = "[Sivu 1] $lqText: $choice",
                        rating = 1
                    )
                )
            }

            // Save detailed question ratings
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
        val currentAnimStyle = _uiState.value.animationStyle
        _uiState.value = FeedbackUiState(animationStyle = currentAnimStyle)
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

    // Question Management (Step 3 Detailed Survey Questions)
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
            repository.updateQuestion(question.copy(isArchived = true))
        }
    }

    fun restoreQuestion(question: SurveyQuestion) {
        viewModelScope.launch {
            repository.updateQuestion(question.copy(isArchived = false))
        }
    }

    fun permanentlyDeleteQuestion(question: SurveyQuestion) {
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

    // Landing Question Management (Step 1 Freeform Multiple Choice, Max 3)
    fun addLandingQuestion(
        textFi: String, textEn: String, textSv: String,
        optionsFi: String, optionsEn: String, optionsSv: String
    ) {
        viewModelScope.launch {
            val currentList = landingQuestions.value
            val activeCount = currentList.count { !it.isArchived }
            if (activeCount >= 3) return@launch // Enforce max 3 active questions

            val nextOrder = (currentList.maxOfOrNull { it.displayOrder } ?: 0) + 1
            val newQ = LandingQuestion(
                questionKey = "LANDING_Q_${System.currentTimeMillis()}",
                textFi = textFi.trim(),
                textEn = textEn.trim().ifEmpty { textFi.trim() },
                textSv = textSv.trim().ifEmpty { textFi.trim() },
                optionsFi = optionsFi.trim(),
                optionsEn = optionsEn.trim().ifEmpty { optionsFi.trim() },
                optionsSv = optionsSv.trim().ifEmpty { optionsFi.trim() },
                displayOrder = nextOrder
            )
            repository.insertLandingQuestion(newQ)
        }
    }

    fun updateLandingQuestion(question: LandingQuestion) {
        viewModelScope.launch {
            repository.updateLandingQuestion(question)
        }
    }

    fun deleteLandingQuestion(question: LandingQuestion) {
        viewModelScope.launch {
            repository.updateLandingQuestion(question.copy(isArchived = true))
        }
    }

    fun restoreLandingQuestion(question: LandingQuestion) {
        viewModelScope.launch {
            val activeCount = landingQuestions.value.count { !it.isArchived }
            if (activeCount >= 3) return@launch // Enforce max 3 active limit
            repository.updateLandingQuestion(question.copy(isArchived = false))
        }
    }

    fun permanentlyDeleteLandingQuestion(question: LandingQuestion) {
        viewModelScope.launch {
            repository.deleteLandingQuestion(question)
        }
    }

    fun moveLandingQuestionUp(question: LandingQuestion) {
        viewModelScope.launch {
            val list = landingQuestions.value
            val index = list.indexOfFirst { it.id == question.id }
            if (index > 0) {
                val prev = list[index - 1]
                val tempOrder = question.displayOrder
                repository.updateLandingQuestion(question.copy(displayOrder = prev.displayOrder))
                repository.updateLandingQuestion(prev.copy(displayOrder = tempOrder))
            }
        }
    }

    fun moveLandingQuestionDown(question: LandingQuestion) {
        viewModelScope.launch {
            val list = landingQuestions.value
            val index = list.indexOfFirst { it.id == question.id }
            if (index >= 0 && index < list.size - 1) {
                val next = list[index + 1]
                val tempOrder = question.displayOrder
                repository.updateLandingQuestion(question.copy(displayOrder = next.displayOrder))
                repository.updateLandingQuestion(next.copy(displayOrder = tempOrder))
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
        val freshServices = repository.getAllServices().first().distinctBy { it.serviceKey }
        val answersMap = freshAnswers.groupBy { it.feedbackId }
        val currentQuestions = repository.getAllQuestions().first().filter { !it.isArchived }.distinctBy { it.questionKey }
        val currentLandingQuestions = repository.getAllLandingQuestions().first().filter { !it.isArchived }.take(3).distinctBy { it.questionKey }

        val sb = StringBuilder()

        // Header
        sb.append("Palaute_ID,Päivämäärä,Kieli,Palvelu,NPS_Arvio")
        for (i in currentLandingQuestions.indices) {
            val lq = currentLandingQuestions[i]
            val cleanLqText = lq.textFi.replace("\n", " ").replace(",", ";")
            sb.append(",\"Sivu1_K${i + 1}: $cleanLqText\"")
        }
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

            val matchingService = freshServices.find { it.serviceKey.equals(item.service, ignoreCase = true) }
            val displayServiceName = when {
                matchingService != null -> when (item.languageUsed) {
                    "EN" -> matchingService.textEn.ifEmpty { matchingService.textFi }
                    "SV" -> matchingService.textSv.ifEmpty { matchingService.textFi }
                    else -> matchingService.textFi
                }
                else -> item.service
            }
            val cleanService = displayServiceName.replace(",", ";")

            sb.append("${item.id},\"$dateStr\",\"${item.languageUsed}\",\"$cleanService\",${item.npsRating}")

            val itemAnswers = answersMap[item.id]

            // Landing question choices
            for (i in currentLandingQuestions.indices) {
                val lq = currentLandingQuestions[i]
                val ans = itemAnswers?.find {
                    it.questionId == (1000 + lq.id) ||
                    it.questionTextSnapshot.contains(lq.textFi, ignoreCase = true)
                }
                val choiceText = if (ans != null) {
                    ans.questionTextSnapshot.substringAfter(": ").replace(",", ";")
                } else ""
                sb.append(",\"$choiceText\"")
            }

            // Survey question ratings
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
