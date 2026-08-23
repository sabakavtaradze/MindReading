package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

import com.example.viewmodel.SubjectRecognitionState

@Composable
fun HeaderView(
    isSyncing: Boolean,
    subjectState: SubjectRecognitionState? = null,
    onMasterToggleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        ),
        label = "angle"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Branding Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isSyncing) "● ნეირონული კავშირი აქტიურია" else "○ მოლოდინში",
                        color = if (isSyncing) NeuralAccent else NeuralTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    if (subjectState != null) {
                        val activeP = subjectState.profiles.find { it.id == subjectState.activePersonId }
                        val isMatched = subjectState.activePersonId == subjectState.detectedPersonId
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isMatched) Color(0xFF00FFB2).copy(alpha = 0.15f) else Color(0xFFFF9800).copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isMatched) "🔒 ${activeP?.name?.take(10)}" else "⚠️ MISMATCH",
                                color = if (isMatched) Color(0xFF00FFB2) else Color(0xFFFF9800),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "NeuroSync ",
                        color = NeuralTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = "v2.5",
                        color = NeuralAccent,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(NeuralAccent)
                    .clickable { onMasterToggleClick() },
                contentAlignment = Alignment.Center
            ) {
                if (isSyncing) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(angle)
                            .border(
                                width = 3.dp,
                                color = NeuralDeepPurple.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .border(
                                width = 3.dp,
                                color = NeuralDeepPurple,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = "Neural Active",
                            tint = NeuralDeepPurple,
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    Icon(
                        imageVector = AppIcons.Psychology,
                        contentDescription = "Neural Standby",
                        tint = NeuralDeepPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Master 1-Button Activation Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (isSyncing) {
                        Brush.horizontalGradient(listOf(NeuralDeepPurple, Color(0xFF1E1035)))
                    } else {
                        Brush.horizontalGradient(listOf(NeuralSurface, Color(0xFF140D24)))
                    }
                )
                .border(
                    1.dp,
                    if (isSyncing) NeuralAccent.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.1f),
                    RoundedCornerShape(18.dp)
                )
                .clickable { onMasterToggleClick() }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSyncing) NeuralAccent else Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isSyncing) AppIcons.CheckCircle else AppIcons.PlayCircle,
                            contentDescription = "Master Toggle",
                            tint = if (isSyncing) NeuralDeepPurple else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = if (isSyncing) "⚡ ყველაფერი აქტიურია (MASTER ON)" else "⚡ ერთი ღილაკით ყველაფრის ჩართვა",
                            color = if (isSyncing) NeuralAccent else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "სენსორები • კამერა • აუდიო • BCI • Buds 2 • ფონური რეჟიმი",
                            color = NeuralTextSecondary,
                            fontSize = 9.5.sp
                        )
                    }
                }

                Switch(
                    checked = isSyncing,
                    onCheckedChange = { onMasterToggleClick() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeuralDeepPurple,
                        checkedTrackColor = NeuralAccent,
                        uncheckedThumbColor = Color.LightGray,
                        uncheckedTrackColor = Color.DarkGray
                    )
                )
            }
        }
    }
}
