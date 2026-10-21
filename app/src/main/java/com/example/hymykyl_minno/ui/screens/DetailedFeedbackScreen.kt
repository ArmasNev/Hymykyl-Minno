package com.example.hymykyl_minno.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.ui.components.FeedbackLayout
import com.example.hymykyl_minno.ui.components.FiveSmileyRatingBar
import com.example.hymykyl_minno.ui.localization.AppStrings
import com.example.hymykyl_minno.ui.theme.*
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailedFeedbackScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit = {},
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage
    val dbQuestions by viewModel.surveyQuestions.collectAsState()

    val fallbackQuestions = listOf(
        "Palvelut olivat helposti saatavilla",
        "Palvelukokemus oli mielestäni viihtyisä ja sujuva",
        "Koen saaneeni toiveeni tarvittaessa kuulluksi",
        "Haluaisin tulla uudelleen"
    )

    val questions = if (dbQuestions.isNotEmpty()) {
        dbQuestions.map { q ->
            when (lang) {
                "EN" -> q.textEn.ifEmpty { q.textFi }
                "SV" -> q.textSv.ifEmpty { q.textFi }
                else -> q.textFi
            }
        }
    } else fallbackQuestions

    var activeQuestionIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    FeedbackLayout(selectedLanguage = lang) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            // Header (fixed)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.stepIndicator(3, lang), color = Color.Gray, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                val serviceLocalized = AppStrings.localizedServiceName(uiState.selectedService, lang)
                Text(
                    text = AppStrings.detailedTitle(serviceLocalized, lang),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Content area with fixed-position active item logic
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 180.dp, bottom = 300.dp)
                ) {
                    itemsIndexed(questions) { index, question ->
                        val currentRating = uiState.detailedRatings[index] ?: 0
                        QuestionItem(
                            question = question,
                            rating = currentRating,
                            isActive = activeQuestionIndex == index,
                            onActivate = {
                                activeQuestionIndex = index
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index = index, scrollOffset = -200)
                                }
                            },
                            onRatingSelected = { rating ->
                                viewModel.updateDetailedRating(index, rating)
                                if (index < questions.size - 1) {
                                    val nextIndex = index + 1
                                    activeQuestionIndex = nextIndex
                                    coroutineScope.launch {
                                        delay(100)
                                        listState.animateScrollToItem(index = nextIndex, scrollOffset = -200)
                                    }
                                } else {
                                    activeQuestionIndex = -1
                                    coroutineScope.launch {
                                        delay(100)
                                        listState.animateScrollToItem(index = 0, scrollOffset = 0)
                                    }
                                }
                            }
                        )
                        if (index < questions.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text(text = AppStrings.btnBack(lang), color = Color.Gray, fontSize = 18.sp)
                }
                TextButton(onClick = onSkip) {
                    Text(text = AppStrings.btnSkip(lang), color = Color.Gray, fontSize = 18.sp)
                }
                Button(
                    onClick = onNext,
                    enabled = questions.indices.all { i -> (uiState.detailedRatings[i] ?: 0) > 0 },
                    modifier = Modifier
                        .height(56.dp)
                        .width(160.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text(text = AppStrings.btnNext(lang), fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: String,
    rating: Int,
    isActive: Boolean,
    onActivate: () -> Unit,
    onRatingSelected: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onActivate() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = question,
                fontSize = if (isActive) 24.sp else 16.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) TextDark else Color.Gray,
                modifier = Modifier.weight(1f)
            )
            if (!isActive && rating > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val fiveEmojis = listOf("😡", "😣", "😐", "😊", "😄")
                    val fiveColors = listOf(
                        RatingRed1,
                        RatingOrange1,
                        RatingYellow1,
                        RatingLightGreen2,
                        RatingGreen2
                    )
                    val safeIndex = (rating - 1).coerceIn(0, 4)
                    val emoji = fiveEmojis[safeIndex]
                    val color = fiveColors[safeIndex]

                    Text(
                        text = emoji,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = rating.toString(), color = color, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                }
            }
        }

        AnimatedVisibility(
            visible = isActive,
            enter = expandVertically(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                FiveSmileyRatingBar(
                    selectedRating = rating,
                    onRatingSelected = onRatingSelected
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
