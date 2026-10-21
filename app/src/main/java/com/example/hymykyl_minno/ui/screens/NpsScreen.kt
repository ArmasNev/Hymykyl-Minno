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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            // Header with language and progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = AppStrings.stepIndicator(1, lang), color = Color.Gray, fontSize = 16.sp)
                LanguageSelector(
                    selectedLanguage = lang,
                    onLanguageSelected = { viewModel.updateLanguage(it) }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = AppStrings.npsTitle(lang),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(modifier = Modifier.fillMaxWidth()) {
                SmileyRatingBar(
                    selectedRating = uiState.npsRating,
                    onRatingSelected = { viewModel.updateNpsRating(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.npsNotAtAll(lang), color = Color.Gray)
                    Text(text = AppStrings.npsExtremelyLikely(lang), color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))

            Button(
                onClick = onNext,
                enabled = uiState.npsRating > 0,
                modifier = Modifier
                    .align(Alignment.End)
                    .height(56.dp)
                    .width(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
            ) {
                Text(text = AppStrings.btnNext(lang), fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
