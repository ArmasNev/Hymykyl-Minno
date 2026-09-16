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
import com.example.hymykyl_minno.ui.components.SmileyRatingBar
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel

@Composable
fun NpsScreen(
    onNext: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    FeedbackLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp)
        ) {
            // Header with language and progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Vaihe 1 / 4", color = Color.Gray, fontSize = 16.sp)
                // Language selector placeholder
                Row {
                    Text(text = "FI", fontWeight = FontWeight.Bold, color = MainOrange)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "EN", color = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "SV", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Kuinka todennäköisesti suosittelisit HyMy-kylän palveluita?",
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
                    Text(text = "En lainkaan", color = Color.Gray)
                    Text(text = "Erittäin todennäköisesti", color = Color.Gray)
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
                Text(text = "Jatka", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
