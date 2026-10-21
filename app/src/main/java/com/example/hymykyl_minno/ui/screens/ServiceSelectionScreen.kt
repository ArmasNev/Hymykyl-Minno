package com.example.hymykyl_minno.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.hymykyl_minno.ui.localization.AppStrings
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel

@Composable
fun ServiceSelectionScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit = {},
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage
    val services = listOf(
        "Suun terveydenhuolto", "Fysioterapia & osteopatia",
        "Senioripalvelut", "Perhepalvelut",
        "Aikuisneuvola", "Toimintaterapia",
        "Apuvälinepalvelut", "Matalan kynnyksen palvelut"
    )

    FeedbackLayout(selectedLanguage = lang) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = AppStrings.stepIndicator(2, lang), color = Color.Gray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = AppStrings.serviceTitle(lang),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(32.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(services) { service ->
                    val isSelected = uiState.selectedService == service
                    val label = AppStrings.localizedServiceName(service, lang)

                    OutlinedButton(
                        onClick = { viewModel.updateService(service) },
                        modifier = Modifier.height(64.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MainOrange else Color.LightGray
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MainOrange.copy(alpha = 0.1f) else Color.White
                        )
                    ) {
                        Text(
                            text = label,
                            color = TextDark,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    enabled = uiState.selectedService.isNotEmpty(),
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
