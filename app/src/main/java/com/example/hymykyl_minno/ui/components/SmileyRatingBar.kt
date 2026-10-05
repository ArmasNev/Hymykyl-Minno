package com.example.hymykyl_minno.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..10) {
            Box(modifier = Modifier.weight(1f)) {
                RatingItem(
                    rating = i,
                    color = ratingColors[i - 1],
                    isSelected = selectedRating == i,
                    onClick = { onRatingSelected(i) }
                )
            }
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            val circleSize = constraints.maxWidth.dp.coerceAtMost(72.dp)
            val emojiFontSize = (circleSize.value * 0.65f).sp

            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(if (isSelected) color else color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = emojiFontSize,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeight = emojiFontSize,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = rating.toString(),
            color = if (isSelected) TextDark else TextDark.copy(alpha = 0.5f),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun FiveSmileyRatingBar(
    selectedRating: Int,
    onRatingSelected: (Int) -> Unit
) {
    val fiveColors = listOf(
        RatingRed1,
        RatingOrange1,
        RatingYellow1,
        RatingLightGreen2,
        RatingGreen2
    )
    val fiveEmojis = listOf("😡", "😣", "😐", "😊", "😄")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 1..5) {
            Box(modifier = Modifier.weight(1f)) {
                FiveRatingItem(
                    rating = i,
                    emoji = fiveEmojis[i - 1],
                    color = fiveColors[i - 1],
                    isSelected = selectedRating == i,
                    onClick = { onRatingSelected(i) }
                )
            }
        }
    }
}

@Composable
private fun FiveRatingItem(
    rating: Int,
    emoji: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            val circleSize = constraints.maxWidth.dp.coerceAtMost(72.dp)
            val emojiFontSize = (circleSize.value * 0.65f).sp

            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(if (isSelected) color else color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = emojiFontSize,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeight = emojiFontSize,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = rating.toString(),
            color = if (isSelected) TextDark else TextDark.copy(alpha = 0.5f),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
