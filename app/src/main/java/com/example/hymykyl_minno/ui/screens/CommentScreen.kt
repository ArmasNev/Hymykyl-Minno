package com.example.hymykyl_minno.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.ui.components.FeedbackLayout
import com.example.hymykyl_minno.ui.localization.AppStrings
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit = {},
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.selectedLanguage
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    FeedbackLayout(selectedLanguage = lang) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLargeScreen = maxWidth >= 1100.dp

            val titleFontSize = if (isLargeScreen) 36.sp else 28.sp
            val titleLineHeight = if (isLargeScreen) 44.sp else 36.sp
            val subtitleFontSize = if (isLargeScreen) 18.sp else 15.sp
            val stepFontSize = if (isLargeScreen) 17.sp else 15.sp
            val fieldHeight = if (isLargeScreen) 200.dp else 160.dp
            val navButtonHeight = if (isLargeScreen) 60.dp else 52.dp
            val navButtonWidth = if (isLargeScreen) 190.dp else 170.dp
            val navFontSize = if (isLargeScreen) 19.sp else 17.sp
            val reminderFontSize = if (isLargeScreen) 15.sp else 13.sp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = AppStrings.stepIndicator(4, lang), color = Color.Gray, fontSize = stepFontSize)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = AppStrings.commentTitle(lang),
                    fontSize = titleFontSize,
                    lineHeight = titleLineHeight,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = AppStrings.commentSubtitle(lang),
                    color = Color.Gray,
                    fontSize = subtitleFontSize
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = uiState.comment,
                    onValueChange = { viewModel.updateComment(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight),
                    placeholder = { Text(text = AppStrings.commentPlaceholder(lang), fontSize = subtitleFontSize) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MainOrange,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFF9E5))
                        .padding(14.dp)
                ) {
                    Text(
                        text = when (lang) {
                            "EN" -> "Reminder: Please do not include personal data like your name, email, or phone number."
                            "SV" -> "Påminnelse: Vänligen skriv inte personuppgifter som namn, e-post eller telefonnummer."
                            else -> "Muistutus: ethän kirjoita henkilötietoja, kuten nimeä, sähköpostia tai puhelinnumeroa."
                        },
                        color = Color(0xFF856404),
                        fontSize = reminderFontSize
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

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
                        onClick = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            viewModel.submitFeedback()
                            onNext()
                        },
                        modifier = Modifier
                            .height(navButtonHeight)
                            .width(navButtonWidth),
                        colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                    ) {
                        Text(text = AppStrings.btnSubmit(lang), fontSize = navFontSize, color = Color.White)
                    }
                }
            }
        }
    }
}
