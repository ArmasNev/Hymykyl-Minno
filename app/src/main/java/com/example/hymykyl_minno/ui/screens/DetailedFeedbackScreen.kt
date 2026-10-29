package com.example.hymykyl_minno.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
    val animStyle = uiState.animationStyle
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
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLargeScreen = maxWidth >= 1100.dp

            val titleFontSize = if (isLargeScreen) 34.sp else 26.sp
            val titleLineHeight = if (isLargeScreen) 42.sp else 34.sp
            val stepFontSize = if (isLargeScreen) 17.sp else 15.sp
            val navButtonHeight = if (isLargeScreen) 60.dp else 52.dp
            val navButtonWidth = if (isLargeScreen) 170.dp else 150.dp
            val navFontSize = if (isLargeScreen) 19.sp else 17.sp
            val activeQFontSize = if (isLargeScreen) 26.sp else 22.sp
            val inactiveQFontSize = if (isLargeScreen) 18.sp else 15.sp

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
                        Text(text = AppStrings.stepIndicator(3, lang), color = Color.Gray, fontSize = stepFontSize)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val serviceLocalized = AppStrings.localizedServiceName(uiState.selectedService, lang)
                    Text(
                        text = AppStrings.detailedTitle(serviceLocalized, lang),
                        fontSize = titleFontSize,
                        lineHeight = titleLineHeight,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content area with fixed-position active item logic (Vertical Center Slot)
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
                                animationStyle = animStyle,
                                activeFontSize = activeQFontSize,
                                inactiveFontSize = inactiveQFontSize,
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
                                            listState.animateScrollToItem(index = nextIndex, scrollOffset = -200)
                                        }
                                    } else {
                                        activeQuestionIndex = -1
                                        coroutineScope.launch {
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
                        Text(text = AppStrings.btnBack(lang), color = Color.Gray, fontSize = navFontSize)
                    }
                    TextButton(onClick = onSkip) {
                        Text(text = AppStrings.btnSkip(lang), color = Color.Gray, fontSize = navFontSize)
                    }
                    Button(
                        onClick = onNext,
                        enabled = questions.indices.all { i -> (uiState.detailedRatings[i] ?: 0) > 0 },
                        modifier = Modifier
                            .height(navButtonHeight)
                            .width(navButtonWidth),
                        colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                    ) {
                        Text(text = AppStrings.btnNext(lang), fontSize = navFontSize, color = Color.White)
                    }
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
    animationStyle: String = "FADE_EXPAND",
    activeFontSize: androidx.compose.ui.unit.TextUnit = 22.sp,
    inactiveFontSize: androidx.compose.ui.unit.TextUnit = 15.sp,
    onActivate: () -> Unit,
    onRatingSelected: (Int) -> Unit
) {
    val duration = 400
    val enterTransition = when (animationStyle) {
        "FADE_ONLY" -> fadeIn(animationSpec = tween(400))
        "SLIDE_HORIZONTAL" -> slideInHorizontally(animationSpec = tween(duration, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(duration))
        "SPRING_BOUNCE" -> expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
        "INSTANT" -> EnterTransition.None
        else -> expandVertically(animationSpec = tween(duration, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(duration))
    }

    val exitTransition = when (animationStyle) {
        "FADE_ONLY" -> fadeOut(animationSpec = tween(400))
        "SLIDE_HORIZONTAL" -> slideOutHorizontally(animationSpec = tween(duration, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(duration))
        "SPRING_BOUNCE" -> shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
        "INSTANT" -> ExitTransition.None
        else -> shrinkVertically(animationSpec = tween(duration, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(duration))
    }

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
                fontSize = if (isActive) activeFontSize else inactiveFontSize,
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
            enter = enterTransition,
            exit = exitTransition
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                FiveSmileyRatingBar(
                    selectedRating = rating,
                    onRatingSelected = onRatingSelected
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
