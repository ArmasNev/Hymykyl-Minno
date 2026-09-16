package com.example.hymykyl_minno.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.ui.components.FeedbackLayout
import com.example.hymykyl_minno.ui.components.SmileyRatingBar
import com.example.hymykyl_minno.ui.theme.*
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailedFeedbackScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val questions = listOf(
        "Palvelut olivat helposti saatavilla",
        "Palvelukokemus oli mielestäni viihtyisä ja sujuva",
        "Koen saaneeni toiveeni tarvittaessa kuulluksi",
        "Haluaisin tulla uudelleen"
    )
    val ratings = listOf(
        uiState.accessibilityRating,
        uiState.fluencyRating,
        uiState.heardRating,
        uiState.returnRating
    )

    var activeQuestionIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = androidx.compose.ui.platform.LocalDensity.current

    FeedbackLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 64.dp, vertical = 32.dp)
        ) {
            // Header (fixed)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Vaihe 3 / 4", color = Color.Gray, fontSize = 16.sp)
                    Row {
                        Text(text = "FI", fontWeight = FontWeight.Bold, color = MainOrange)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "EN", color = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "SV", color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Vielä muutama kysymys palvelusta ${uiState.selectedService}.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Content
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                val maxHeightPx = constraints.maxHeight
                // centerOffset moves the *top* of the item to (maxHeight / 2 - averageItemHalfHeight)
                // Using 130.dp as an estimate for half the height of an expanded question item.
                val centerOffset = - (maxHeightPx / 2 - with(density) { 130.dp.roundToPx() })

                // Function to handle rating and smooth transition
                val handleRatingSelected: (Int, Int) -> Unit = { index, rating ->
                    viewModel.updateDetailedRating(index, rating)
                    if (index < questions.size - 1) {
                        val nextIndex = index + 1
                        activeQuestionIndex = nextIndex
                        coroutineScope.launch {
                            delay(150) // Slightly longer delay to ensure expansion starts
                            listState.animateScrollToItem(index = nextIndex, scrollOffset = centerOffset)
                        }
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 100.dp, bottom = 400.dp)
                ) {
                    itemsIndexed(questions) { index, question ->
                        QuestionItem(
                            question = question,
                            rating = ratings[index],
                            isActive = activeQuestionIndex == index,
                            onActivate = { 
                                activeQuestionIndex = index
                                coroutineScope.launch {
                                    delay(150)
                                    listState.animateScrollToItem(index = index, scrollOffset = centerOffset)
                                }
                            },
                            onRatingSelected = { rating ->
                                handleRatingSelected(index, rating)
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
                    Text(text = "Takaisin", color = Color.Gray, fontSize = 18.sp)
                }
                Button(
                    onClick = onNext,
                    enabled = ratings.all { it > 0 },
                    modifier = Modifier
                        .height(56.dp)
                        .width(160.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text(text = "Jatka", fontSize = 18.sp, color = Color.White)
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
                    val icon: ImageVector = when (rating) {
                        1 -> Icons.Default.SentimentVeryDissatisfied
                        2 -> Icons.Default.MoodBad
                        3 -> Icons.Default.SentimentDissatisfied
                        4 -> Icons.Default.SentimentDissatisfied
                        5 -> Icons.Default.SentimentNeutral
                        6 -> Icons.Default.SentimentSatisfied
                        7 -> Icons.Default.SentimentSatisfiedAlt
                        8 -> Icons.Default.Mood
                        9 -> Icons.Default.SentimentVerySatisfied
                        10 -> Icons.Default.SentimentVerySatisfied
                        else -> Icons.Default.SentimentNeutral
                    }
                    val ratingColors = listOf(
                        RatingRed1, RatingRed2,
                        RatingOrange1, RatingOrange2,
                        RatingYellow1, RatingYellow2,
                        RatingLightGreen1, RatingLightGreen2,
                        RatingGreen1, RatingGreen2
                    )
                    val color = ratingColors[rating - 1]
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
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
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                SmileyRatingBar(
                    selectedRating = rating,
                    onRatingSelected = onRatingSelected
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
