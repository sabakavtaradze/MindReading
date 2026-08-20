package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.GeorgianNeuroLinguisticEngine
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

@Composable
fun GeorgianPhonemeMatrixCard(
    onSelectPhoneme: (GeorgianNeuroLinguisticEngine.GeorgianPhoneme) -> Unit = {}
) {
    var selectedLetter by remember { mutableStateOf('ყ') }
    val currentPhoneme = GeorgianNeuroLinguisticEngine.GEORGIAN_PHONEME_MAP[selectedLetter] 
        ?: GeorgianNeuroLinguisticEngine.GeorgianPhoneme('ყ', "ხორხისმიერი მკვეთრი", 34.5f, 0.98f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(NeuralSurface)
            .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = AppIcons.RecordVoiceOver,
                        contentDescription = "ქართული სუბვოკალური ფონეტიკა",
                        tint = Color(0xFF00FFB2),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "ქართული სუბვოკალური ფონეტიკური მატრიცა",
                        color = Color(0xFF00FFB2),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00FFB2).copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "33 ფონემა / EMG",
                        color = Color(0xFF00FFB2),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "ქართული ენის უნიკალური გლოტალური და მკვეთრი ბგერების (ყ, ჭ, წ, კ, პ, ტ, ხ, ღ) შინაგანი მეტყველების სიხშირული დეკოდერი.",
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            // Live Selected Phoneme Telemetry HUD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF08181B))
                    .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF00FFB2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentPhoneme.letter}",
                                color = NeuralDeepPurple,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                text = "ტიპი: ${currentPhoneme.phoneticType}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "არტიკულაციური დატვირთვა: ${(currentPhoneme.articulatoryEffort * 100).toInt()}%",
                                color = Color(0xFF00FFB2),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${currentPhoneme.laryngealEmgFrequencyHz} Hz",
                            color = Color(0xFF00E5FF),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "ხორხის EMG სიგნალი",
                            color = NeuralTextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }
            }

            // Quick Select Interactive Letter Grid for Ejectives & Key Consonants
            Text(
                text = "აირჩიეთ ფონემა ნეირონული პროფილის სანახავად:",
                color = NeuralTextSecondary,
                fontSize = 10.sp
            )

            val keyLetters = listOf('ყ', 'ჭ', 'წ', 'კ', 'პ', 'ტ', 'ხ', 'ღ', 'ჩ', 'ც', 'ძ', 'ჯ', 'ა', 'ე', 'ი')
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                keyLetters.take(8).forEach { ch ->
                    val isSelected = ch == selectedLetter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF00FFB2) else NeuralDeepPurple)
                            .clickable {
                                selectedLetter = ch
                                GeorgianNeuroLinguisticEngine.GEORGIAN_PHONEME_MAP[ch]?.let(onSelectPhoneme)
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$ch",
                            color = if (isSelected) NeuralDeepPurple else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                keyLetters.drop(8).forEach { ch ->
                    val isSelected = ch == selectedLetter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFF00FFB2) else NeuralDeepPurple)
                            .clickable {
                                selectedLetter = ch
                                GeorgianNeuroLinguisticEngine.GEORGIAN_PHONEME_MAP[ch]?.let(onSelectPhoneme)
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$ch",
                            color = if (isSelected) NeuralDeepPurple else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
