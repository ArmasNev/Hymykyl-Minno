package com.example.hymykyl_minno.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hymykyl_minno.R
import com.example.hymykyl_minno.ui.localization.AppStrings
import com.example.hymykyl_minno.ui.theme.BackgroundCream
import com.example.hymykyl_minno.ui.theme.MainOrange

@Composable
fun FeedbackLayout(
    selectedLanguage: String = "FI",
    onAdminAccessRequested: () -> Unit = {},
    content: @Composable RowScope.() -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scaleFactor = (maxWidth / 1280.dp).coerceIn(0.85f, 1.55f)

        val sidebarPadding = (24 * scaleFactor).dp
        val logoHeight = (46 * scaleFactor).dp
        val titleFontSize = (26 * scaleFactor).sp
        val titleLineHeight = (34 * scaleFactor).sp
        val descFontSize = (13 * scaleFactor).sp
        val descLineHeight = (18 * scaleFactor).sp
        val footerFontSize = (12 * scaleFactor).sp

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCream)
        ) {
            // Orange Sidebar (Narrower weight 0.20f)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.20f)
                    .background(MainOrange)
                    .padding(sidebarPadding)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.metropolia),
                        contentDescription = "Metropolia Logo",
                        modifier = Modifier
                            .height(logoHeight)
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        val startTime = System.currentTimeMillis()
                                        val released = tryAwaitRelease()
                                        val duration = System.currentTimeMillis() - startTime
                                        if (released && duration >= 3000L) { // 3-5 sec long press
                                            onAdminAccessRequested()
                                        }
                                    }
                                )
                            },
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.TopStart
                    )

                    Text(
                        text = AppStrings.sidebarTitle(selectedLanguage),
                        color = Color.White,
                        fontSize = titleFontSize,
                        fontWeight = FontWeight.Bold,
                        lineHeight = titleLineHeight
                    )

                    Column {
                        Text(
                            text = AppStrings.sidebarDesc(selectedLanguage),
                            color = Color.White,
                            fontSize = descFontSize,
                            lineHeight = descLineHeight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "HyMy-kylä · 2026",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = footerFontSize
                        )
                    }
                }
            }

            // Main Content Area (Wider weight 0.80f)
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.80f),
                content = content
            )
        }
    }
}
