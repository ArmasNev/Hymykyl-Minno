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

    FeedbackLayout(
        selectedLanguage = lang,
        customTitle = viewModel.getSidebarTitle(lang),
        customDesc = viewModel.getSidebarDesc(lang)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

            val screenPadding = (32 * scaleFactor).dp
            val titleFontSize = (38 * scaleFactor).sp
            val titleLineHeight = (46 * scaleFactor).sp
            val stepFontSize = (20 * scaleFactor).sp
            val buttonHeight = (72 * scaleFactor).dp
            val serviceFontSize = (22 * scaleFactor).sp
            val gridSpacing = (16 * scaleFactor).dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(screenPadding)
            ) {
                // Header with step indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.stepIndicator(2, lang), color = Color.DarkGray, fontSize = stepFontSize, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

                Text(
                    text = AppStrings.serviceTitle(lang),
                    fontSize = titleFontSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

                // Dynamic Service Options Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                    verticalArrangement = Arrangement.spacedBy(gridSpacing),
                    modifier = Modifier.weight(1f)
                ) {
                    items(dbServices) { option ->
                        val isSelected = uiState.selectedService == option.serviceKey
                        val serviceText = when (lang) {
                            "EN" -> option.textEn.ifEmpty { option.textFi }
                            "SV" -> option.textSv.ifEmpty { option.textFi }
                            else -> option.textFi
                        }

                        Button(
                            onClick = {
                                viewModel.updateService(option.serviceKey)
                                onNext()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(buttonHeight),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MainOrange else Color.White,
                                contentColor = if (isSelected) Color.White else TextDark
                            ),
                            border = BorderStroke(
                                (2 * scaleFactor).dp,
                                if (isSelected) MainOrange else Color.LightGray
                            )
                        ) {
                            Text(
                                text = serviceText,
                                fontSize = serviceFontSize,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

                // Footer Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.height((64 * scaleFactor).dp).width((180 * scaleFactor).dp)
                    ) {
                        Text(text = AppStrings.btnBack(lang), fontSize = (22 * scaleFactor).sp)
                    }

                    TextButton(
                        onClick = onSkip,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                    ) {
                        Text(text = AppStrings.btnSkip(lang), fontSize = (22 * scaleFactor).sp)
                    }
                }
            }
        }
    }
}
