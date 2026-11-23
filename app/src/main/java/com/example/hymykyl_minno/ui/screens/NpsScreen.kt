package com.example.hymykyl_minno.ui.screens

import androidx.compose.foundation.layout.*
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
import com.example.hymykyl_minno.ui.components.LanguageSelector
import com.example.hymykyl_minno.ui.components.SmileyRatingBar
import com.example.hymykyl_minno.ui.localization.AppStrings
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NpsScreen(
    onNext: () -> Unit,
    onAdminAccess: () -> Unit = {},
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage
    val landingQuestions by viewModel.activeLandingQuestions.collectAsState()

    FeedbackLayout(
        selectedLanguage = lang,
        onAdminAccessRequested = onAdminAccess,
        customTitle = viewModel.getSidebarTitle(lang),
        customDesc = viewModel.getSidebarDesc(lang)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

            val titleFontSize = (38 * scaleFactor).sp
            val titleLineHeight = (46 * scaleFactor).sp
            val stepFontSize = (20 * scaleFactor).sp
            val navButtonHeight = (64 * scaleFactor).dp
            val navButtonWidth = (180 * scaleFactor).dp
            val navFontSize = (22 * scaleFactor).sp
            val scaleLabelSize = (22 * scaleFactor).sp
            val screenPadding = (32 * scaleFactor).dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(screenPadding)
            ) {
                // Header with language and progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.stepIndicator(1, lang), color = Color.DarkGray, fontSize = stepFontSize, fontWeight = FontWeight.SemiBold)
                    LanguageSelector(
                        selectedLanguage = lang,
                        onLanguageSelected = { viewModel.updateLanguage(it) }
                    )
                }

                Spacer(modifier = Modifier.height((20 * scaleFactor).dp))

                Text(
                    text = AppStrings.npsTitle(lang),
                    fontSize = titleFontSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.weight(1f))

                // Optional Landing Page Freeform Multiple Choice Questions (Max 3)
                if (landingQuestions.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = (8 * scaleFactor).dp),
                        verticalArrangement = Arrangement.spacedBy((12 * scaleFactor).dp)
                    ) {
                        landingQuestions.take(3).forEach { lq ->
                            val qText = when (lang) {
                                "EN" -> lq.textEn.ifEmpty { lq.textFi }
                                "SV" -> lq.textSv.ifEmpty { lq.textFi }
                                else -> lq.textFi
                            }
                            val rawOptions = when (lang) {
                                "EN" -> lq.optionsEn.ifEmpty { lq.optionsFi }
                                "SV" -> lq.optionsSv.ifEmpty { lq.optionsFi }
                                else -> lq.optionsFi
                            }
                            val optionsList = rawOptions.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            val currentSelected = uiState.landingAnswers[lq.id]

                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = qText,
                                    fontSize = (20 * scaleFactor).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.height((6 * scaleFactor).dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy((8 * scaleFactor).dp)
                                ) {
                                    optionsList.forEach { option ->
                                        val isSelected = currentSelected == option
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { viewModel.updateLandingAnswer(lq.id, option) },
                                            label = { Text(text = option, fontSize = (18 * scaleFactor).sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MainOrange,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    SmileyRatingBar(
                        selectedRating = uiState.npsRating,
                        onRatingSelected = { viewModel.updateNpsRating(it) }
                    )

                    Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = AppStrings.npsNotAtAll(lang), color = Color.Black, fontSize = scaleLabelSize, fontWeight = FontWeight.Bold)
                        Text(text = AppStrings.npsExtremelyLikely(lang), color = Color.Black, fontSize = scaleLabelSize, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.weight(1.2f))

                Button(
                    onClick = onNext,
                    enabled = uiState.npsRating > 0,
                    modifier = Modifier
                        .align(Alignment.End)
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
