package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.PersonProfile
import com.example.viewmodel.SubjectRecognitionState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SubjectRecognitionCard(
    subjectState: SubjectRecognitionState,
    onSelectActiveSubject: (String) -> Unit,
    onToggleTargetLock: () -> Unit,
    onToggleContaminationShield: () -> Unit,
    onToggleAutoSwitch: () -> Unit,
    onSimulateDetectedChange: (String) -> Unit,
    onAddNewSubject: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeProfile = subjectState.profiles.find { it.id == subjectState.activePersonId }
    val detectedProfile = subjectState.profiles.find { it.id == subjectState.detectedPersonId }
    val isMatched = subjectState.activePersonId == subjectState.detectedPersonId

    var showAddDialog by remember { mutableStateOf(false) }
    var showComparisonMatrix by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newTitle by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NeuralSurface,
                        Color(0xFF141226)
                    )
                )
            )
            .border(
                1.5.dp,
                if (isMatched) NeuralAccent.copy(alpha = 0.6f) else Color(0xFFFF9800).copy(alpha = 0.8f),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isMatched) Color(0xFF00FFB2) else Color(0xFFFF9800))
                    )
                    Text(
                        text = "პერსონის ამომცნობი & ბიომეტრიული ფარი",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isMatched) Color(0xFF00FFB2).copy(alpha = 0.15f) else Color(0xFFFF9800).copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isMatched) "🔒 LOCKED: ${activeProfile?.name?.take(14)}" else "⚠️ გაფრთხილება: უცხო სუბიექტი",
                        color = if (isMatched) Color(0xFF00FFB2) else Color(0xFFFF9800),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Status Banner if Mismatch & Shield Active
            if (!isMatched && subjectState.isContaminationShieldActive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF9800).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFFF9800).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "🛡️", fontSize = 20.sp)
                        Column {
                            Text(
                                text = "მონაცემთა კარანტინი ჩართულია",
                                color = Color(0xFFFFB74D),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ტელეფონს უჭირავს: ${detectedProfile?.name ?: "უცნობი"}. ${activeProfile?.name}-ის ციფრული ორეული დაცულია გაფუჭებისგან.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            // Profile Switcher Selector Horizontal Row
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "აირჩიეთ ფოკუს-სუბიექტი (ვის მონაცემებზე ვფოკუსირდეთ):",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (showComparisonMatrix) "დახურვა ▲" else "შედარება 📊",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { showComparisonMatrix = !showComparisonMatrix }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subjectState.profiles.forEach { profile ->
                        val isSelected = profile.id == subjectState.activePersonId
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeuralAccent else Color.White.copy(alpha = 0.06f))
                                .border(
                                    1.dp,
                                    if (isSelected) NeuralAccent else Color.White.copy(alpha = 0.12f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectActiveSubject(profile.id) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = profile.avatarEmoji, fontSize = 16.sp)
                                Column {
                                    Text(
                                        text = profile.name,
                                        color = if (isSelected) NeuralDeepPurple else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${profile.baseEmgFrequencyHz.toInt()} Hz • ${profile.totalWordsDecoded} სიტყვა",
                                        color = if (isSelected) NeuralDeepPurple.copy(alpha = 0.8f) else NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }

                    // Add Profile Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.04f))
                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .clickable { showAddDialog = !showAddDialog }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "➕ დამატება", color = Color(0xFF00FFB2), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Add Subject Dialog Bar
            AnimatedVisibility(visible = showAddDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "ახალი სუბიექტის რეგისტრაცია:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                placeholder = { Text("სახელი (მაგ. Person 4)", color = NeuralTextSecondary, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeuralAccent,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            Button(
                                onClick = {
                                    if (newName.isNotBlank()) {
                                        onAddNewSubject(newName.trim(), newTitle.trim())
                                        newName = ""
                                        newTitle = ""
                                        showAddDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("შენახვა", color = NeuralDeepPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Comparative Deep Metrics Matrix (Expandable)
            AnimatedVisibility(visible = showComparisonMatrix) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "📊 პერსონების ბიომეტრიული შედარებითი ანალიზი",
                            color = Color(0xFF00FFB2),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        subjectState.profiles.forEach { p ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (p.id == subjectState.activePersonId) Color(0xFF8A2BE2).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.03f))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(text = p.avatarEmoji, fontSize = 14.sp)
                                    Column {
                                        Text(text = p.name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(text = p.title, color = NeuralTextSecondary, fontSize = 9.sp)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "VPU: ${p.baseEmgFrequencyHz.toInt()}Hz | α/β: ${p.alphaBetaRatio}",
                                        color = Color(0xFF00FFB2),
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "დაჭერა: ${p.touchPressureBaselineGrams}g | ხამხამი: ${p.gazeBlinkRatePerMin}/წთ",
                                        color = NeuralTextSecondary,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Biometric Multi-Sensor Cross Match Matrix
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ბიომეტრიული იდენტიფიკატორები (Match Score):",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "თანხვედრა: ${subjectState.recognitionConfidencePct}%",
                            color = if (isMatched) Color(0xFF00FFB2) else Color(0xFFFF9800),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BiometricMatchBadge("🖐️ ხელის დაჭერა", "${subjectState.biometricGripMatchPct}%")
                        BiometricMatchBadge("📷 სახე & მზერა", "${subjectState.faceGazeMatchPct}%")
                        BiometricMatchBadge("🎧 ყურის წინაღობა", "${subjectState.inEarImpedanceMatchPct}%")
                        BiometricMatchBadge("⚡ სუბვოკალური VPU", "${subjectState.vocalTractResonanceMatchPct}%")
                    }
                }
            }

            // Protection Shields & Auto-Switch Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🤖 ავტომატური ბიომეტრიული ამოცნობა (Auto-Detect)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subjectState.detectionStatusLabel,
                        color = Color(0xFF00FFB2),
                        fontSize = 9.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF00FFB2).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("ავტომატური", color = Color(0xFF00FFB2), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Protection Shields & Auto-Switch Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🛡️ მონაცემთა იზოლაციის ფარი (Data Shield)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "თუ ტელეფონი სხვას უკავია, მონაცემები არ შეერევა",
                        color = NeuralTextSecondary,
                        fontSize = 9.sp
                    )
                }

                Switch(
                    checked = subjectState.isContaminationShieldActive,
                    onCheckedChange = { onToggleContaminationShield() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeuralAccent,
                        checkedTrackColor = NeuralDeepPurple
                    )
                )
            }

            // Quick Simulation Switcher for Testing (Person 1 vs Person 2 vs Person 3)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "🧪 სიმულატორი: ვინ უჭირავს ტელეფონს ამ წამს?",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjectState.profiles.forEach { profile ->
                        val isCurrentDetected = profile.id == subjectState.detectedPersonId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCurrentDetected) Color(0xFF8A2BE2).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.05f))
                                .border(1.dp, if (isCurrentDetected) Color(0xFFD4B2FF) else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { onSimulateDetectedChange(profile.id) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${profile.avatarEmoji} ${profile.name.take(10)}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = if (isCurrentDetected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BiometricMatchBadge(title: String, score: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = score, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(text = title, color = NeuralTextSecondary, fontSize = 8.sp)
    }
}
