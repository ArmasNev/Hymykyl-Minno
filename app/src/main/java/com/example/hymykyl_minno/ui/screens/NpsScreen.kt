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

@Composable
fun NpsScreen(
    onNext: () -> Unit,
    onAdminAccess: () -> Unit = {},
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage

    FeedbackLayout(
        selectedLanguage = lang,
        onAdminAccessRequested = onAdminAccess
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

            val titleFontSize = (38 * scaleFactor).sp
            val titleLineHeight = (46 * scaleFactor).sp
            val stepFontSize = (20 * scaleFactor).sp
            val navButtonHeight = (64 * scaleFactor).dp
            val navButtonWidth = (180 * scaleFactor).dp
            val navFontSize = (22 * scaleFactor).sp
            val scaleLabelSize = (20 * scaleFactor).sp
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
                    Text(text = AppStrings.stepIndicator(1, lang), color = Color.Gray, fontSize = stepFontSize)
                    LanguageSelector(
                        selectedLanguage = lang,
                        onLanguageSelected = { viewModel.updateLanguage(it) }
                    )
                }

                Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

                Text(
                    text = AppStrings.npsTitle(lang),
                    fontSize = titleFontSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.weight(1f))

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
                        Text(text = AppStrings.npsNotAtAll(lang), color = Color.Gray, fontSize = scaleLabelSize, fontWeight = FontWeight.Medium)
                        Text(text = AppStrings.npsExtremelyLikely(lang), color = Color.Gray, fontSize = scaleLabelSize, fontWeight = FontWeight.Medium)
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
