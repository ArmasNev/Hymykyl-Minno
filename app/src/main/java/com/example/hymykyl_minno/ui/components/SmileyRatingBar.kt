package com.example.hymykyl_minno.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hymykyl_minno.ui.theme.*

@Composable
fun SmileyRatingBar(
    selectedRating: Int,
    onRatingSelected: (Int) -> Unit
) {
    val ratingColors = listOf(
        RatingRed1, RatingRed2,
        RatingOrange1, RatingOrange2,
        RatingYellow1, RatingYellow2,
        RatingLightGreen1, RatingLightGreen2,
        RatingGreen1, RatingGreen2
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..10) {
            RatingItem(
                rating = i,
                color = ratingColors[i - 1],
                isSelected = selectedRating == i,
                onClick = { onRatingSelected(i) }
            )
        }
    }
}

@Composable
private fun RatingItem(
    rating: Int,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // NPS Standard Mapping:
    // 1-6 Detractors: 😡 😠 😣 🙁 😕 😐 (6 is Neutral)
    // 7-8 Passives:   🙂 😊
    // 9-10 Promoters: 😀 😄
    val emoji = when (rating) {
        1 -> "😡"
        2 -> "😠"
        3 -> "😣"
        4 -> "🙁"
        5 -> "😕"
        6 -> "😐" // Neutral
        7 -> "🙂"
        8 -> "😊"
        9 -> "😀"
        10 -> "😄" // Happy / Success
        else -> "😐"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(if (isSelected) color else color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 55.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    lineHeight = 55.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = rating.toString(),
            color = if (isSelected) TextDark else TextDark.copy(alpha = 0.5f),
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
