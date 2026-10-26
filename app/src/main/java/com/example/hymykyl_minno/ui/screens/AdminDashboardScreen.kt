package com.example.hymykyl_minno.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.hymykyl_minno.data.ServiceOption
import com.example.hymykyl_minno.data.SurveyQuestion
import com.example.hymykyl_minno.ui.components.FeedbackLayout
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.TextDark
import com.example.hymykyl_minno.ui.viewmodel.AppViewModelProvider
import com.example.hymykyl_minno.ui.viewmodel.FeedbackViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    viewModel: FeedbackViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val feedbackList by viewModel.allFeedback.collectAsState()
    val questions by viewModel.surveyQuestions.collectAsState()
    val services by viewModel.serviceOptions.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Export, 1 = Kysymykset, 2 = Palvelut
    var editingQuestion by remember { mutableStateOf<SurveyQuestion?>(null) }
    var deletingQuestion by remember { mutableStateOf<SurveyQuestion?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    var editingService by remember { mutableStateOf<ServiceOption?>(null) }
    var deletingService by remember { mutableStateOf<ServiceOption?>(null) }
    var showAddServiceDialog by remember { mutableStateOf(false) }

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
                    text = { Text("Kysymysten hallinta (${questions.size})") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Palveluiden hallinta (${services.size})") }
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
                            text = "Tallenna CSV-tiedosto suoraan laitteen latauskansioon ilman Google-tiliä, tai jaa se eteenpäin.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val csvData = viewModel.generateCsvContent(feedbackList)
                                        val resultPath = saveCsvToDownloads(context, csvData)
                                        if (resultPath != null) {
                                            Toast.makeText(
                                                context,
                                                "Tiedosto tallennettu kansioon: $resultPath",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Tallennus epäonnistui",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                enabled = feedbackList.isNotEmpty(),
                                modifier = Modifier.height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tallenna laitteen Lataukset-kansioon", fontSize = 16.sp, color = Color.White)
                            }

                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val csvData = viewModel.generateCsvContent(feedbackList)
                                        shareCsvFile(context, csvData)
                                    }
                                },
                                enabled = feedbackList.isNotEmpty(),
                                modifier = Modifier.height(56.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Jaa / Lähetä eteenpäin", fontSize = 16.sp)
                            }
                        }
                    }
                }

                1 -> {
                    // Questions Tab
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kyselyvaiheen 3 kysymykset",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Lisää uusi kysymys", color = Color.White)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
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

                                        Row {
                                            IconButton(
                                                onClick = { viewModel.moveQuestionUp(q) },
                                                enabled = q != questions.firstOrNull()
                                            ) {
                                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Siirrä ylös", tint = Color.Gray)
                                            }
                                            IconButton(
                                                onClick = { viewModel.moveQuestionDown(q) },
                                                enabled = q != questions.lastOrNull()
                                            ) {
                                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Siirrä alas", tint = Color.Gray)
                                            }
                                            IconButton(onClick = { editingQuestion = q }) {
                                                Icon(Icons.Default.Edit, contentDescription = "Muokkaa", tint = MainOrange)
                                            }
                                            IconButton(onClick = { deletingQuestion = q }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Poista", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Services Tab
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kyselyvaiheen 2 palveluvaihtoehdot",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )

                            Button(
                                onClick = { showAddServiceDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Lisää uusi palvelu", color = Color.White)
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(services) { s ->
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
                                            Text(text = "FI: ${s.textFi}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = "EN: ${s.textEn}", fontSize = 14.sp, color = Color.Gray)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(text = "SV: ${s.textSv}", fontSize = 14.sp, color = Color.Gray)
                                        }

                                        Row {
                                            IconButton(
                                                onClick = { viewModel.moveServiceUp(s) },
                                                enabled = s != services.firstOrNull()
                                            ) {
                                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Siirrä ylös", tint = Color.Gray)
                                            }
                                            IconButton(
                                                onClick = { viewModel.moveServiceDown(s) },
                                                enabled = s != services.lastOrNull()
                                            ) {
                                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Siirrä alas", tint = Color.Gray)
                                            }
                                            IconButton(onClick = { editingService = s }) {
                                                Icon(Icons.Default.Edit, contentDescription = "Muokkaa", tint = MainOrange)
                                            }
                                            IconButton(onClick = { deletingService = s }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Poista", tint = MaterialTheme.colorScheme.error)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add New Question Dialog
    if (showAddDialog) {
        var textFi by remember { mutableStateOf("") }
        var textEn by remember { mutableStateOf("") }
        var textSv by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Lisää uusi kysymys (3 kieltä)") },
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
                        if (textFi.isNotBlank()) {
                            viewModel.addQuestion(
                                textFi = textFi.trim(),
                                textEn = textEn.trim().ifEmpty { textFi.trim() },
                                textSv = textSv.trim().ifEmpty { textFi.trim() }
                            )
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text("Lisää")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Peruuta")
                }
            }
        )
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

    // Delete Question Confirmation Dialog
    deletingQuestion?.let { q ->
        AlertDialog(
            onDismissRequest = { deletingQuestion = null },
            title = { Text("Poista kysymys?") },
            text = { Text("Haluatko varmasti poistaa kysymyksen: \"${q.textFi}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteQuestion(q)
                        deletingQuestion = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Poista", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingQuestion = null }) {
                    Text("Peruuta")
                }
            }
        )
    }

    // Add New Service Dialog
    if (showAddServiceDialog) {
        var textFi by remember { mutableStateOf("") }
        var textEn by remember { mutableStateOf("") }
        var textSv by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddServiceDialog = false },
            title = { Text("Lisää uusi palvelu (3 kieltä)") },
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
                        if (textFi.isNotBlank()) {
                            viewModel.addServiceOption(
                                textFi = textFi.trim(),
                                textEn = textEn.trim().ifEmpty { textFi.trim() },
                                textSv = textSv.trim().ifEmpty { textFi.trim() }
                            )
                            showAddServiceDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text("Lisää")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddServiceDialog = false }) {
                    Text("Peruuta")
                }
            }
        )
    }

    // Edit Service Dialog
    editingService?.let { s ->
        var textFi by remember { mutableStateOf(s.textFi) }
        var textEn by remember { mutableStateOf(s.textEn) }
        var textSv by remember { mutableStateOf(s.textSv) }

        AlertDialog(
            onDismissRequest = { editingService = null },
            title = { Text("Muokkaa palvelua (3 kieltä)") },
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
                        val updated = s.copy(textFi = textFi, textEn = textEn, textSv = textSv)
                        viewModel.updateServiceOption(updated)
                        editingService = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                ) {
                    Text("Tallenna")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingService = null }) {
                    Text("Peruuta")
                }
            }
        )
    }

    // Delete Service Confirmation Dialog
    deletingService?.let { s ->
        AlertDialog(
            onDismissRequest = { deletingService = null },
            title = { Text("Poista palvelu?") },
            text = { Text("Haluatko varmasti poistaa palvelun: \"${s.textFi}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteServiceOption(s)
                        deletingService = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Poista", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingService = null }) {
                    Text("Peruuta")
                }
            }
        )
    }
}

private fun saveCsvToDownloads(context: Context, csvData: String): String? {
    return try {
        val fileName = "hymykyla_palautteet_${System.currentTimeMillis()}.csv"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { stream ->
                    stream.write(csvData.toByteArray())
                }
                "Lataukset / Downloads"
            } else null
        } else {
            @Suppress("DEPRECATION")
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val file = File(downloadsDir, fileName)
            file.writeText(csvData)
            file.absolutePath
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
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
