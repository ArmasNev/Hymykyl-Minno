package com.example.hymykyl_minno.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import kotlinx.coroutines.delay

@Composable
fun ThankYouScreen(
    onFinish: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage
    var countdown by remember { mutableIntStateOf(10) }

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
        viewModel.resetFeedback()
        onFinish()
    }

    FeedbackLayout(selectedLanguage = lang) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLargeScreen = maxWidth >= 1100.dp

            val iconSize = if (isLargeScreen) 96.dp else 80.dp
            val titleFontSize = if (isLargeScreen) 40.sp else 32.sp
            val subtitleFontSize = if (isLargeScreen) 20.sp else 16.sp
            val resetFontSize = if (isLargeScreen) 16.sp else 14.sp
            val btnFontSize = if (isLargeScreen) 18.sp else 16.sp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MainOrange,
                    modifier = Modifier.size(iconSize)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = AppStrings.thankYouTitle(lang),
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = AppStrings.thankYouSubtitle(lang),
                    fontSize = subtitleFontSize,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = AppStrings.thankYouResetText(countdown, lang),
                    fontSize = resetFontSize,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    viewModel.resetFeedback()
                    onFinish()
                }) {
                    Text(text = AppStrings.btnReturnToStart(lang), color = MainOrange, fontSize = btnFontSize)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { countdown / 10f },
                        modifier = Modifier.size(64.dp),
                        color = MainOrange,
                        strokeWidth = 4.dp
                    )
                    Text(text = countdown.toString(), fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
