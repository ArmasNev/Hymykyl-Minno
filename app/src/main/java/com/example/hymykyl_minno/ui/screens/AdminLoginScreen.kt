package com.example.hymykyl_minno.ui.screens

import com.example.hymykyl_minno.ui.components.FeedbackLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.data.AdminUser
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel
import kotlinx.coroutines.launch

@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isAdminRegistered by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isAdminRegistered = viewModel.hasAdminUser()
        }
    }

    FeedbackLayout {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

            val titleFontSize = (32 * scaleFactor).sp
            val subtitleFontSize = (18 * scaleFactor).sp
            val buttonHeight = (56 * scaleFactor).dp
            val buttonFontSize = (18 * scaleFactor).sp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((48 * scaleFactor).dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isAdminRegistered) "Ylläpidon kirjautuminen" else "Luo ylläpitäjätunnus",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height((12 * scaleFactor).dp))

                Text(
                    text = if (isAdminRegistered) "Syötä käyttäjätunnus ja salasana päästäksesi hallintaan."
                    else "Aseta ensimmäinen käyttäjätunnus ja salasana ylläpitoa varten.",
                    color = Color.Gray,
                    fontSize = subtitleFontSize
                )

                Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Käyttäjätunnus", fontSize = subtitleFontSize) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Salasana", fontSize = subtitleFontSize) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.6f)
                )

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, fontSize = (16 * scaleFactor).sp)
                }

                Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy((16 * scaleFactor).dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.height(buttonHeight)
                    ) {
                        Text("Takaisin", fontSize = buttonFontSize)
                    }

                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                errorMessage = "Täytä molemmat kentät."
                                return@Button
                            }
                            coroutineScope.launch {
                                if (!isAdminRegistered) {
                                    viewModel.registerAdmin(username, password)
                                    onLoginSuccess()
                                } else {
                                    val success = viewModel.verifyAdminLogin(username, password)
                                    if (success) {
                                        onLoginSuccess()
                                    } else {
                                        errorMessage = "Virheellinen käyttäjätunnus tai salasana."
                                    }
                                }
                            }
                        },
                        modifier = Modifier.height(buttonHeight),
                        colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                    ) {
                        Text(
                            text = if (isAdminRegistered) "Kirjaudu" else "Rekisteröidy",
                            fontSize = buttonFontSize,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
