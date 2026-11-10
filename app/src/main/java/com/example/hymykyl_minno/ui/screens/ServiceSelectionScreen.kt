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
    val dbServices by viewModel.serviceOptions.collectAsState()

    FeedbackLayout(selectedLanguage = lang) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

            val screenPadding = (32 * scaleFactor).dp
            val titleFontSize = (38 * scaleFactor).sp
            val titleLineHeight = (46 * scaleFactor).sp
            val stepFontSize = (20 * scaleFactor).sp
            val buttonHeight = (72 * scaleFactor).dp
            val serviceFontSize = (22 * scaleFactor).sp
            val gridSpacing = (16 * scaleFactor).dp
            val navButtonHeight = (64 * scaleFactor).dp
            val navButtonWidth = (180 * scaleFactor).dp
            val navFontSize = (22 * scaleFactor).sp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(screenPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.stepIndicator(2, lang), color = Color.Gray, fontSize = stepFontSize)
                }

                Spacer(modifier = Modifier.height((20 * scaleFactor).dp))

                Text(
                    text = AppStrings.serviceTitle(lang),
                    fontSize = titleFontSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height((20 * scaleFactor).dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                    verticalArrangement = Arrangement.spacedBy(gridSpacing),
                    modifier = Modifier.weight(1f)
                ) {
                    items(dbServices) { s ->
                        val canonicalName = s.textFi
                        val label = when (lang) {
                            "EN" -> s.textEn.ifEmpty { s.textFi }
                            "SV" -> s.textSv.ifEmpty { s.textFi }
                            else -> s.textFi
                        }
                        val isSelected = uiState.selectedService == canonicalName

                        OutlinedButton(
                            onClick = { viewModel.updateService(canonicalName) },
                            modifier = Modifier.height(buttonHeight),
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) MainOrange else Color.LightGray
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MainOrange.copy(alpha = 0.1f) else Color.White
                            )
                        ) {
                            Text(
                                text = label,
                                color = TextDark,
                                fontSize = serviceFontSize,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
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
                        Text(text = AppStrings.btnBack(lang), color = Color.Gray, fontSize = navFontSize)
                    }
                    TextButton(onClick = onSkip) {
                        Text(text = AppStrings.btnSkip(lang), color = Color.Gray, fontSize = navFontSize)
                    }
                    Button(
                        onClick = onNext,
                        enabled = uiState.selectedService.isNotEmpty(),
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
