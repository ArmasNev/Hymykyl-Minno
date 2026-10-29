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
        val isLargeScreen = maxWidth >= 1100.dp

        val sidebarPadding = if (isLargeScreen) 40.dp else 32.dp
        val logoHeight = if (isLargeScreen) 58.dp else 48.dp
        val titleFontSize = if (isLargeScreen) 32.sp else 26.sp
        val titleLineHeight = if (isLargeScreen) 40.sp else 34.sp
        val descFontSize = if (isLargeScreen) 15.sp else 13.sp
        val footerFontSize = if (isLargeScreen) 13.sp else 12.sp

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCream)
        ) {
            // Orange Sidebar (proportional width so it adapts to any tablet size)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.26f)
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
                            fontSize = descFontSize
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "HyMy-kylä · 2026",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = footerFontSize
                        )
                    }
                }
            }

            // Main Content Area
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.74f),
                content = content
            )
        }
    }
}
