package com.example.hymykyl_minno.ui.screens

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hymykyl_minno.data.SurveyQuestion
import com.example.hymykyl_minno.ui.components.FeedbackLayout
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val feedbackList by viewModel.allFeedback.collectAsState()
    val questions by viewModel.surveyQuestions.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Export, 1 = Kysymykset
    var editingQuestion by remember { mutableStateOf<SurveyQuestion?>(null) }

    FeedbackLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ylläpitopaneeli",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Kirjaudu ulos", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("CSV-Vienti (${feedbackList.size} vastausta)") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Kysymysten hallinta (FI, EN, SV)") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (selectedTab) {
                0 -> {
                    // Export Tab
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tallennettujen palautteiden määrä: ${feedbackList.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Lataa tietokannan vastaukset CSV-muodossa Exceliä tai PowerBI:tä varten.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                val csvData = viewModel.generateCsvContent(feedbackList)
                                shareCsvFile(context, csvData)
                            },
                            enabled = feedbackList.isNotEmpty(),
                            modifier = Modifier.height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Jaa / Vie CSV-tiedosto", fontSize = 18.sp, color = Color.White)
                        }
                    }
                }

                1 -> {
                    // Questions Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(questions) { q ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "FI: ${q.textFi}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "EN: ${q.textEn}", fontSize = 14.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = "SV: ${q.textSv}", fontSize = 14.sp, color = Color.Gray)
                                    }

                                    IconButton(onClick = { editingQuestion = q }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Muokkaa", tint = MainOrange)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Question Dialog
    editingQuestion?.let { q ->
        var textFi by remember { mutableStateOf(q.textFi) }
        var textEn by remember { mutableStateOf(q.textEn) }
        var textSv by remember { mutableStateOf(q.textSv) }

        AlertDialog(
            onDismissRequest = { editingQuestion = null },
            title = { Text("Muokkaa kysymystä (3 kieltä)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = textFi,
                        onValueChange = { textFi = it },
                        label = { Text("Suomi (FI)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = textEn,
                        onValueChange = { textEn = it },
                        label = { Text("Englanti (EN)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = textSv,
                        onValueChange = { textSv = it },
                        label = { Text("Ruotsi (SV)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = q.copy(textFi = textFi, textEn = textEn, textSv = textSv)
                        viewModel.updateQuestion(updated)
                        editingQuestion = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text("Tallenna")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingQuestion = null }) {
                    Text("Peruuta")
                }
            }
        )
    }
}

private fun shareCsvFile(context: Context, csvData: String) {
    val file = File(context.cacheDir, "hymykyla_palautteet.csv")
    file.writeText(csvData)

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_SUBJECT, "HyMy-kylä Palauteraportti CSV")
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Jaa CSV-tiedosto"))
}
