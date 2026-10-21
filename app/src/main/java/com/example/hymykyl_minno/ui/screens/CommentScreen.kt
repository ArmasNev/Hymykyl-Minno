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
                Text(text = AppStrings.stepIndicator(4, lang), color = Color.Gray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = AppStrings.commentTitle(lang),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = AppStrings.commentSubtitle(lang),
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.comment,
                onValueChange = { viewModel.updateComment(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                placeholder = { Text(text = AppStrings.commentPlaceholder(lang)) },
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
                    .padding(16.dp)
            ) {
                Text(
                    text = when (lang) {
                        "EN" -> "Reminder: Please do not include personal data like your name, email, or phone number."
                        "SV" -> "Påminnelse: Vänligen skriv inte personuppgifter som namn, e-post eller telefonnummer."
                        else -> "Muistutus: ethän kirjoita henkilötietoja, kuten nimeä, sähköpostia tai puhelinnumeroa."
                    },
                    color = Color(0xFF856404),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

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
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        viewModel.submitFeedback()
                        onNext()
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .width(200.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text(text = AppStrings.btnSubmit(lang), fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}
