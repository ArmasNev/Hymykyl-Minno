package com.example.hymykyl_minno.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hymykyl_minno.ui.theme.MainOrange
import com.example.hymykyl_minno.ui.theme.BackgroundCream
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.hymykyl_minno.R

@Composable
fun FeedbackLayout(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream)
    ) {
        // Orange Sidebar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .background(MainOrange)
                .padding(48.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo placeholder
                Image(
                    painter = painterResource(id = R.drawable.metropolia),
                    contentDescription = "Metropolia Logo",
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.TopStart
                )

                Text(
                    text = "Kokemuksesi\non meille tärkeä.",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 48.sp
                )

                Column {
                    Text(
                        text = "Vastaukset ovat anonyymejä ja auttavat meitä kehittymään. Tähän kuluu noin 2 minuuttia.",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.3f)))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "HyMy-kylä · 2026",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Main Content Area
        Row(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}
