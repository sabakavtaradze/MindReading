package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DigitalTwinCheckpointEntity
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.DigitalTwinState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigitalTwinView(
    twinState: DigitalTwinState,
    savedCheckpoints: List<DigitalTwinCheckpointEntity>,
    onAdvanceDay: (Int) -> Unit,
    onInjectSample: (String) -> Unit,
    onSaveCheckpoint: () -> Unit,
    onTriggerDeepFineTuning: () -> Unit,
    onDeleteCheckpoint: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var sampleInputText by remember { mutableStateOf("") }
    var isInputExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // =========================================================================
        // 🌟 1. MASTER 90-DAY PROGRESS & DIGITAL TWIN STATUS HERO CARD
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E1038),
                            NeuralCardPurple,
                            Color(0xFF0F071D)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            NeuralAccent,
                            Color(0xFF00E5FF),
                            Color(0xFF9D4EDD),
                            NeuralAccent
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header row
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
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(NeuralAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = AppIcons.Psychology,
                                contentDescription = "Digital Twin Core",
                                tint = NeuralDeepPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "90-DAY DIGITAL TWIN ENGINE",
                                color = NeuralAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "პერსონალური კოგნიტური ორეულის ფორმირება",
                                color = NeuralTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "დღე ${twinState.currentDay} / 90",
                            color = Color(0xFF00E5FF),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 90-Day Progress Bar
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "სრული კოგნიტური ციკლის პროგრესი",
                            color = NeuralTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(twinState.currentDay.toFloat() / twinState.maxDays * 100).toInt()}% დასრულებული",
                            color = NeuralAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (twinState.currentDay.toFloat() / twinState.maxDays).coerceIn(0.01f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = NeuralAccent,
                        trackColor = NeuralDeepPurple
                    )
                }

                // Telemetry Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Metric 1: Total Points
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.8f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("შეგროვებული ბიო-მონაცემი", color = NeuralTextSecondary, fontSize = 10.sp)
                            Text(
                                "${twinState.totalDataPointsCollected} წერტილი",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Metric 2: Accuracy
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.8f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("პროგნოზის სიზუსტე", color = NeuralTextSecondary, fontSize = 10.sp)
                            Text(
                                "${String.format(Locale.US, "%.1f", twinState.currentAccuracyPct)}%",
                                color = NeuralAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Metric 3: Convergence
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.8f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("ნეირო-კონვერგენცია", color = NeuralTextSecondary, fontSize = 10.sp)
                            Text(
                                "${String.format(Locale.US, "%.1f", twinState.neuralConvergencePct)}%",
                                color = Color(0xFF00E5FF),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Active Phase Summary Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeuralSurface)
                        .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = twinState.activePhaseDescription,
                        color = NeuralTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // =========================================================================
        // 🧪 2. INTERACTIVE CONTROLS & TRAINING TOOLS
        // =========================================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "⚙️ ტრენინგის მართვა & სწრაფი სიმულაცია",
                    color = NeuralAccent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                // Advance Days simulation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralDeepPurple)
                            .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable { onAdvanceDay(1) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+1 დღის პროგრესი", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralDeepPurple)
                            .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable { onAdvanceDay(7) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+7 დღე (1 კვირა)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralDeepPurple)
                            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable { onAdvanceDay(30) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+30 დღე (1 თვე)", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Action buttons row: Save Checkpoint & Deep Fine-Tuning
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralAccent)
                            .clickable { onSaveCheckpoint() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("💾 Room DB Checkpoint შენახვა", color = NeuralDeepPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF00E5FF).copy(alpha = 0.2f))
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(12.dp))
                            .clickable { onTriggerDeepFineTuning() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨ Deep Gemini Profile ანალიზი", color = Color(0xFF00E5FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Custom Sample Injection
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isInputExpanded = !isInputExpanded },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "✍️ აზრის / ქცევის ნიმუშის დამატება (Sample Injection)",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (isInputExpanded) "დახურვა ▲" else "გახსნა ▼",
                            color = NeuralAccent,
                            fontSize = 11.sp
                        )
                    }

                    AnimatedVisibility(visible = isInputExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = sampleInputText,
                                onValueChange = { sampleInputText = it },
                                placeholder = {
                                    Text(
                                        "მაგ: „საღამოს ვარჯიშის შემდეგ დავწერ ახალ მოდულს და მოვუსმენ მუსიკას...“",
                                        color = NeuralTextSecondary.copy(alpha = 0.6f),
                                        fontSize = 11.sp
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = NeuralDeepPurple,
                                    unfocusedContainerColor = NeuralDeepPurple,
                                    focusedIndicatorColor = NeuralAccent,
                                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.15f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NeuralAccent)
                                    .clickable {
                                        if (sampleInputText.isNotBlank()) {
                                            onInjectSample(sampleInputText)
                                            sampleInputText = ""
                                            isInputExpanded = false
                                        }
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("დატრენინგება (+1 Sample)", color = NeuralDeepPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 🗺️ 3. 90-DAY MILESTONES ROADMAP (5 PHASES)
        // =========================================================================
        Text(
            text = "📅 90-დღიანი ევოლუციის ეტაპები (Milestones Roadmap)",
            color = NeuralTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        twinState.milestones.forEachIndexed { index, milestone ->
            val isCurrent = milestone.isCurrent
            val isCompleted = milestone.isCompleted

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (isCurrent) NeuralCardPurple
                        else NeuralSurface
                    )
                    .border(
                        width = if (isCurrent) 1.5.dp else 1.dp,
                        color = when {
                            isCurrent -> NeuralAccent
                            isCompleted -> Color(0xFF00E5FF).copy(alpha = 0.6f)
                            else -> Color.White.copy(alpha = 0.06f)
                        },
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCompleted -> Color(0xFF00E5FF)
                                            isCurrent -> NeuralAccent
                                            else -> Color.Gray.copy(alpha = 0.3f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = NeuralDeepPurple,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = milestone.dayRange,
                                color = if (isCurrent) NeuralAccent else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isCompleted -> Color(0xFF00E5FF).copy(alpha = 0.2f)
                                        isCurrent -> NeuralAccent.copy(alpha = 0.2f)
                                        else -> Color.White.copy(alpha = 0.05f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "სიზუსტე: ${milestone.targetAccuracy}",
                                color = when {
                                    isCompleted -> Color(0xFF00E5FF)
                                    isCurrent -> NeuralAccent
                                    else -> NeuralTextSecondary
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = milestone.phaseTitle,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = milestone.description,
                        color = NeuralTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    // Key markers tags
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        milestone.keyMarkers.forEach { marker ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeuralDeepPurple)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(marker, color = NeuralTextSecondary, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 💾 4. SAVED ROOM DATABASE CHECKPOINTS
        // =========================================================================
        if (savedCheckpoints.isNotEmpty()) {
            Text(
                text = "📁 შენახული Checkpoint-ები (Room SQLite DB)",
                color = NeuralTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            savedCheckpoints.take(5).forEach { cp ->
                val dateStr = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(cp.savedTimestamp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeuralSurface)
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("დღე ${cp.dayNumber} • ${cp.phaseName}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("სიზუსტე: ${String.format(Locale.US, "%.1f", cp.accuracyPct)}% • ${cp.dataPointsCount} წერტილი • $dateStr", color = NeuralTextSecondary, fontSize = 10.sp)
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Red.copy(alpha = 0.15f))
                                .clickable { onDeleteCheckpoint(cp.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = AppIcons.CloseIcon, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}
