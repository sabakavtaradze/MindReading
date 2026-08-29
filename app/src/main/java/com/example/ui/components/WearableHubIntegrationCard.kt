package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sensor.WearableTelemetryHub
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralCyanAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralGreenActive
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.WearableDeviceItem
import com.example.viewmodel.WearablesSuiteState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WearableHubIntegrationCard(
    daFitState: WearableTelemetryHub.DaFitWatchState,
    buds2State: WearableTelemetryHub.GalaxyBuds2State,
    googleFitState: WearableTelemetryHub.GoogleFitBridgeState,
    wearablesSuite: WearablesSuiteState,
    onToggleDevice: (String) -> Unit,
    onToggleDaFit: () -> Unit,
    onToggleBuds2: () -> Unit,
    onCycleBuds2NoiseMode: () -> Unit,
    onSyncGoogleFit: () -> Unit,
    onScanBle: () -> Unit,
    onUpdateAccountCredentials: (String, String, Boolean, Boolean) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var isAuthExpanded by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf(googleFitState.linkedAccountEmail) }
    var passwordInput by remember { mutableStateOf("") }
    var isPermanentTokenEnabled by remember { mutableStateOf(googleFitState.isPermanentAuthActive) }
    var isDirectPhoneReadEnabled by remember { mutableStateOf(googleFitState.isDirectPhoneReadEnabled) }
    var showSuccessSavedBadge by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeuralSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeuralCyanAccent.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==========================================
            // 1. HEADER & GOOGLE FIT CLOUD STATUS
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NeuralDeepPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌚", fontSize = 20.sp)
                    }
                    Column {
                        Text(
                            text = "Wearables Hub & Google Fit ხიდი",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Da Fit (ZL02C Pro) • Galaxy Buds 2 • Google Fit Cloud",
                            fontSize = 11.sp,
                            color = NeuralTextSecondary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { isAuthExpanded = !isAuthExpanded },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isAuthExpanded) "დახურვა ✖" else "🔑 ავტორიზაცია",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = onScanBle,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralDeepPurple),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (wearablesSuite.isBleScanning) "სკანირება..." else "BLE ძებნა",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeuralAccent
                        )
                    }
                }
            }

            // ==========================================
            // EXPANDABLE PERMANENT AUTH & CREDENTIALS PANEL
            // ==========================================
            AnimatedVisibility(visible = isAuthExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(NeuralDeepPurple)
                        .border(1.dp, NeuralGreenActive.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔒 ანგარიშის ავტორიზაცია & სამუდამო ტოკენი",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeuralGreenActive.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Permanent Active", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeuralGreenActive)
                            }
                        }

                        Text(
                            text = "შეიყვანეთ ან დაადასტურეთ თქვენი ელ-ფოსტა და პაროლი/ტოკენი. ავტორიზაცია შეინახება სამუდამოდ (Never Expires) და მონაცემები პირდაპირ წაიკითხება ტელეფონის შიგნიდან (Direct Internal Sensor Read).",
                            fontSize = 10.sp,
                            color = NeuralTextSecondary
                        )

                        // Email Field
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Google Fit / Da Fit ელ-ფოსტა", fontSize = 11.sp, color = NeuralTextSecondary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = NeuralBackground,
                                unfocusedContainerColor = NeuralBackground,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = NeuralAccent,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
                            )
                        )

                        // Password Field
                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("პაროლი / API Secret ტოკენი", fontSize = 11.sp, color = NeuralTextSecondary) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••••••", color = Color.Gray, fontSize = 11.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = NeuralBackground,
                                unfocusedContainerColor = NeuralBackground,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = NeuralAccent,
                                unfocusedIndicatorColor = Color.White.copy(alpha = 0.2f)
                            )
                        )

                        // Switches for Permanent Auth & Direct Internal Access
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("♾️ სამუდამო ავტორიზაცია", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("მუდმივი Refresh Token, არასდროს ამოიწურება", fontSize = 9.sp, color = NeuralTextSecondary)
                            }
                            Switch(
                                checked = isPermanentTokenEnabled,
                                onCheckedChange = { isPermanentTokenEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = NeuralGreenActive
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("📲 პირდაპირ ტელეფონიდან მიღება", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("შიდა მოწყობილობიდან პირდაპირი ბიომეტრიული სინქი", fontSize = 9.sp, color = NeuralTextSecondary)
                            }
                            Switch(
                                checked = isDirectPhoneReadEnabled,
                                onCheckedChange = { isDirectPhoneReadEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = NeuralCyanAccent
                                )
                            )
                        }

                        // Save Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    onUpdateAccountCredentials(
                                        emailInput,
                                        passwordInput,
                                        isPermanentTokenEnabled,
                                        isDirectPhoneReadEnabled
                                    )
                                    showSuccessSavedBadge = true
                                    isAuthExpanded = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeuralGreenActive),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("შენახვა & სამუდამო დაკავშირება ✅", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }

                            Button(
                                onClick = {
                                    emailInput = "sabakavtaradzee@gmail.com"
                                    isPermanentTokenEnabled = true
                                    isDirectPhoneReadEnabled = true
                                    onUpdateAccountCredentials(
                                        "sabakavtaradzee@gmail.com",
                                        "default_permanent",
                                        true,
                                        true
                                    )
                                    isAuthExpanded = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple)
                            ) {
                                Text("პირდაპირი დაკავშირება ⚡", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Google Fit Bridge Status Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeuralBackground)
                    .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("☁️", fontSize = 14.sp)
                            Column {
                                Text(
                                    text = "Google Fit & Health Data Bridge",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "ავტორიზებული: ${googleFitState.linkedAccountEmail}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = NeuralGreenActive
                                )
                            }
                        }

                        Button(
                            onClick = onSyncGoogleFit,
                            enabled = !googleFitState.isSyncing,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralGreenActive),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            if (googleFitState.isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    color = Color.Black,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("სინქრონიზაცია...", fontSize = 10.sp, color = Color.Black)
                            } else {
                                Text("Google Fit სინქი 🔄", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }

                    // Token Status Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeuralDeepPurple)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "♾️ ტოკენი: ${googleFitState.tokenExpiry}",
                                fontSize = 9.sp,
                                color = NeuralCyanAccent
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeuralDeepPurple)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (googleFitState.isDirectPhoneReadEnabled) "📲 პირდაპირი შიდა სინქი: ჩართულია" else "Cloud Sync",
                                fontSize = 9.sp,
                                color = NeuralGreenActive
                            )
                        }
                    }

                    Text(
                        text = "${googleFitState.syncProvider} • ${googleFitState.lastSyncStatus}",
                        fontSize = 10.sp,
                        color = NeuralTextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MiniHealthMetricChip("აქტიური დრო", "${googleFitState.dailyActiveMinutes} წთ", NeuralGreenActive, Modifier.weight(1f))
                        MiniHealthMetricChip("RHR პულსი", "${googleFitState.restingHeartRateBpm} BPM", NeuralAccent, Modifier.weight(1f))
                        MiniHealthMetricChip("VO2 Max", "${googleFitState.cardioFitnessVo2Max}", Color.White, Modifier.weight(1f))
                        MiniHealthMetricChip("სტრესის ქულა", "${googleFitState.stressLevelScore}/100", NeuralCyanAccent, Modifier.weight(1f))
                    }
                }
            }

            // ==========================================
            // 2. DA FIT SMARTWATCH (ZL02C PRO) LIVE CARD
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NeuralBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (daFitState.isConnected) NeuralGreenActive.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⌚", fontSize = 22.sp)
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = daFitState.modelName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (daFitState.isConnected) NeuralGreenActive.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (daFitState.isConnected) "BLE აქტიურია" else "გათიშული",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (daFitState.isConnected) NeuralGreenActive else Color.Red
                                        )
                                    }
                                }
                                Text(
                                    text = "MAC: ${daFitState.macAddress} • ელემენტი: ${daFitState.batteryPct}% • მაჯაზეა",
                                    fontSize = 10.sp,
                                    color = NeuralTextSecondary
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onToggleDaFit,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(if (daFitState.isConnected) "გათიშვა" else "დაკავშირება", fontSize = 10.sp, color = Color.White)
                        }
                    }

                    // Biometrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TelemetryBadge("❤️ პულსი", "${daFitState.heartRateBpm} BPM", Color(0xFFFF5252), Modifier.weight(1f))
                        TelemetryBadge("🫁 SpO2", "${daFitState.spO2Pct}%", Color(0xFF00E5FF), Modifier.weight(1f))
                        TelemetryBadge("🩸 წნევა", "${daFitState.bloodPressureSystolic}/${daFitState.bloodPressureDiastolic}", Color(0xFFFFD700), Modifier.weight(1f))
                        TelemetryBadge("🌡️ ტემპერატურა", "${daFitState.skinTemperatureCelsius}°C", Color(0xFF69F0AE), Modifier.weight(1f))
                    }

                    // Step Tracker
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🏃‍♂️ დღიური ნაბიჯები: ${daFitState.stepsCount} / ${daFitState.targetSteps}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "${daFitState.distanceKm} კმ • ${daFitState.caloriesBurnedKcal} კკალ",
                                fontSize = 10.sp,
                                color = NeuralCyanAccent
                            )
                        }
                        val stepProgress = (daFitState.stepsCount.toFloat() / daFitState.targetSteps).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { stepProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = NeuralGreenActive,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )
                    }

                    // Sleep Architecture
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("😴 ძილის არქიტექტურა (Da Fit & Fit)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("ხანგრძლივობა: ${daFitState.totalSleepDurationFormatted} • ქულა: ${daFitState.sleepScore}/100", fontSize = 10.sp, color = NeuralTextSecondary)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                SleepPill("Deep", "${daFitState.deepSleepMinutes}წთ", NeuralAccent)
                                SleepPill("REM", "${daFitState.remSleepMinutes}წთ", NeuralCyanAccent)
                                SleepPill("Light", "${daFitState.lightSleepMinutes}წთ", Color.LightGray)
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 3. SAMSUNG GALAXY BUDS 2 WEARABLE CARD
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NeuralBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (buds2State.isConnected) NeuralAccent.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎧", fontSize = 22.sp)
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = buds2State.modelName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (buds2State.isConnected) NeuralGreenActive.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (buds2State.isConnected) "დაკავშირებულია" else "გათიშული",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (buds2State.isConnected) NeuralGreenActive else Color.Red
                                        )
                                    }
                                }
                                Text(
                                    text = "L: ${buds2State.leftEarbudBatteryPct}% • R: ${buds2State.rightEarbudBatteryPct}% • Case: ${buds2State.caseBatteryPct}%",
                                    fontSize = 10.sp,
                                    color = NeuralTextSecondary
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onToggleBuds2,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(if (buds2State.isConnected) "გათიშვა" else "დაკავშირება", fontSize = 10.sp, color = Color.White)
                        }
                    }

                    // Earbud Placement & ANC Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            InEarBadge("მარცხენა ყურსასმენი", buds2State.isLeftInEar)
                            InEarBadge("მარჯვენა ყურსასმენი", buds2State.isRightInEar)
                        }

                        Button(
                            onClick = onCycleBuds2NoiseMode,
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(buds2State.noiseControlMode, fontSize = 9.sp, color = Color.White)
                        }
                    }

                    // VPU Bone Conduction & Subvocal Tracking Stream
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralDeepPurple.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("🎙️ VPU ხმის ამომცნობი & ყბის აქსელერომეტრი", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeuralCyanAccent)
                                Text("Latency: ${buds2State.latencyMs} ms", fontSize = 9.sp, color = NeuralTextSecondary)
                            }
                            Text(
                                text = "ყბის მიკრო-მოძრაობის ამპლიტუდა: ${String.format(java.util.Locale.US, "%.1f", buds2State.jawMotionAmplitudeMicroG)} µg • ხმაურის მგრძნობელობა: ${String.format(java.util.Locale.US, "%.1f", buds2State.inEarMicrophoneSensibilityDb)} dB",
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 4. ALL CONNECTED SENSORS LIST
            // ==========================================
            Text(
                text = "დამატებითი ნეირო & ბიომეტრიული სენსორები",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeuralTextSecondary
            )

            wearablesSuite.devices.filter { it.id != "da_fit_zl02cpro" && it.id != "galaxy_buds2" && it.id != "google_fit_bridge" }.forEach { dev ->
                WearableDeviceRow(device = dev, onToggle = { onToggleDevice(dev.id) })
            }
        }
    }
}

@Composable
private fun MiniHealthMetricChip(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(NeuralDeepPurple.copy(alpha = 0.6f))
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 9.sp, color = NeuralTextSecondary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun TelemetryBadge(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(NeuralDeepPurple.copy(alpha = 0.6f))
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 9.sp, color = NeuralTextSecondary)
            Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun SleepPill(
    label: String,
    duration: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$label: $duration",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun InEarBadge(
    label: String,
    isInEar: Boolean
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isInEar) NeuralGreenActive.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = if (isInEar) "🟢 $label ჩადებულია" else "⚪ $label ამოღებულია",
            fontSize = 9.sp,
            color = if (isInEar) NeuralGreenActive else NeuralTextSecondary
        )
    }
}

@Composable
private fun WearableDeviceRow(
    device: WearableDeviceItem,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NeuralBackground)
            .border(1.dp, if (device.isConnected) NeuralGreenActive.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .clickable { onToggle() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(device.iconEmoji, fontSize = 20.sp)
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(device.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${device.batteryPct}%", fontSize = 10.sp, color = NeuralTextSecondary)
                }
                Text(device.primaryMetric, fontSize = 10.sp, color = NeuralCyanAccent)
                Text(device.secondaryMetric, fontSize = 9.sp, color = NeuralTextSecondary)
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (device.isConnected) NeuralGreenActive.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.05f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (device.isConnected) "დაკავშირებულია" else "გათიშული",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (device.isConnected) NeuralGreenActive else NeuralTextSecondary
            )
        }
    }
}
