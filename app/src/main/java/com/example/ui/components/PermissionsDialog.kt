package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.util.ApiKeyManager
import com.example.util.PermissionHelper

@Composable
fun PermissionsDialog(
    micGranted: Boolean,
    cameraGranted: Boolean = false,
    notificationsGranted: Boolean = true,
    usageStatsGranted: Boolean,
    accessibilityGranted: Boolean,
    overlayGranted: Boolean,
    onMicToggle: (Boolean) -> Unit,
    onCameraToggle: (Boolean) -> Unit = {},
    onNotificationsToggle: (Boolean) -> Unit = {},
    onUsageStatsToggle: (Boolean) -> Unit,
    onAccessibilityToggle: (Boolean) -> Unit,
    onOverlayToggle: (Boolean) -> Unit,
    onOpenExplanationClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Security,
                            contentDescription = "ნებართვები",
                            tint = NeuralAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "სისტემური ნებართვები",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = AppIcons.CloseIcon,
                            contentDescription = "დახურვა",
                            tint = NeuralTextSecondary
                        )
                    }
                }

                // Master Grant All Persistently Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple)
                        .border(1.dp, NeuralAccent, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.CheckCircle,
                                contentDescription = "Persistent",
                                tint = NeuralAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "მუდმივი ნებართვების დამახსოვრება",
                                color = NeuralAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "ერთხელ გაცემული ნებართვები ინახება მუდმივ მეხსიერებაში და აღარასოდეს გაითიშება აპლიკაციის გადატვირთვისას.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                        Button(
                            onClick = {
                                PermissionHelper.setMicGranted(context, true)
                                PermissionHelper.setCameraGranted(context, true)
                                PermissionHelper.setNotificationsGranted(context, true)
                                PermissionHelper.setUsageStatsGranted(context, true)
                                PermissionHelper.setAccessibilityGranted(context, true)
                                PermissionHelper.setOverlayGranted(context, true)
                                onMicToggle(true)
                                onCameraToggle(true)
                                onNotificationsToggle(true)
                                onUsageStatsToggle(true)
                                onAccessibilityToggle(true)
                                onOverlayToggle(true)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "⚡ ყველა ნებართვის მიღება და დამახსოვრება",
                                color = NeuralDeepPurple,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "სრული ფონური ნეირო-ანალიზისა და აზრების პროგნოზირებისთვის აქტიურია შემდეგი ნებართვები:",
                    color = NeuralTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                // 1. Microphone Permission
                PermissionRowItem(
                    icon = AppIcons.Mic,
                    title = "მიკროფონი (აკუსტიკური ფონი)",
                    description = "გარემოს ხმაურის დეციბელებისა და სუბვოკალური სიხშირის ანალიზი",
                    isChecked = micGranted,
                    onCheckedChange = onMicToggle
                )

                // 2. Camera Permission
                PermissionRowItem(
                    icon = AppIcons.CameraFront,
                    title = "წინა კამერა (მზერის & სახის HUD)",
                    description = "თვალის მზერის ვექტორების, პულსისა (rPPG) და ყურადღების ტრეკინგი",
                    isChecked = cameraGranted,
                    onCheckedChange = onCameraToggle
                )

                // 3. Notifications Permission
                PermissionRowItem(
                    icon = AppIcons.Notifications,
                    title = "შეტყობინებები (ფონური სერვისი)",
                    description = "უწყვეტი ფონური სინქრონიზაცია და აზრების პროგნოზის ჩვენება შეტყობინებების პანელში",
                    isChecked = notificationsGranted,
                    onCheckedChange = onNotificationsToggle
                )

                // 4. Accessibility Touch Events
                PermissionRowItem(
                    icon = AppIcons.TouchApp,
                    title = "შეხების ჟესტები (Accessibility)",
                    description = "ეკრანზე შეხების რიტმისა და მიკრო-დაყოვნების რეგისტრაცია",
                    isChecked = accessibilityGranted,
                    onCheckedChange = {
                        onAccessibilityToggle(it)
                        if (it) PermissionHelper.openAccessibilitySettings(context)
                    }
                )

                // 5. Usage Stats & App Context
                PermissionRowItem(
                    icon = AppIcons.Visibility,
                    title = "აპლიკაციების გამოყენების სტატისტიკა",
                    description = "აქტიური აპლიკაციის კონტექსტის განსაზღვრა აზრის კონტექსტუალიზაციისთვის",
                    isChecked = usageStatsGranted,
                    onCheckedChange = {
                        onUsageStatsToggle(it)
                        if (it) PermissionHelper.openUsageAccessSettings(context)
                    }
                )

                // 6. System Overlay Floating UI
                PermissionRowItem(
                    icon = AppIcons.Layers,
                    title = "სხვა აპებზე ჩვენება (Overlay HUD)",
                    description = "აზრების პროგნოზის მცურავი ვიჯეტი ნებისმიერ აპლიკაციაში",
                    isChecked = overlayGranted,
                    onCheckedChange = {
                        onOverlayToggle(it)
                        if (it) PermissionHelper.openOverlaySettings(context)
                    }
                )

                // 7. Battery Optimization Exemption for Months-Long Background Run
                val isBatteryIgnored = remember {
                    mutableStateOf<Boolean>(PermissionHelper.isBatteryOptimizationIgnored(context))
                }
                PermissionRowItem(
                    icon = AppIcons.Bolt,
                    title = "ბატარეის შეუზღუდავი რეჟიმი (24/7 თვეობით)",
                    description = "Doze Mode-ის და ენერგოდამზოგის გამორთვა თვეობით უწყვეტი ფონური მუშაობისთვის",
                    isChecked = isBatteryIgnored.value,
                    onCheckedChange = {
                        isBatteryIgnored.value = it
                        PermissionHelper.setBatteryOptimizationIgnored(context, it)
                        if (it) {
                            PermissionHelper.requestIgnoreBatteryOptimization(context)
                            com.example.receiver.BootReceiver.schedulePerpetualWatchdog(context)
                            com.example.receiver.BootReceiver.startNeuralContextService(context)
                        }
                    }
                )

                // Samsung Galaxy One UI "Never auto sleeping apps" dedicated Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple)
                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Bolt,
                                contentDescription = "Samsung Never Sleeping",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Samsung One UI: Never auto sleeping apps",
                                color = Color(0xFF00E5FF),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "როგორ დავამატოთ აპლიკაცია Samsung-ის 'Never auto sleeping apps'-ში:\n1. დააჭირეთ 'Never auto sleeping apps' ხაზს.\n2. ზედა მარჯვენა კუთხეში დააჭირეთ '+' (პლუს) ღილაკს.\n3. მონიშნეთ 'AI Intent Predictor' და დააჭირეთ 'Add'.\nან აპლიკაციის პარამეტრებში (Battery) აირჩიეთ 'Unrestricted'.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    PermissionHelper.openSamsungBackgroundUsageLimits(context)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "🔋 Device Care გახსნა",
                                    color = NeuralDeepPurple,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = {
                                    PermissionHelper.openAppSettings(context)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "⚙️ App Battery გახსნა",
                                    color = NeuralDeepPurple,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 🌐 GEMINI CLOUD AI CONFIGURATION CARD
                var apiKeyInput by remember { mutableStateOf(ApiKeyManager.getActiveGeminiApiKey(context)) }
                var showKeySavedFeedback by remember { mutableStateOf(false) }
                val hasKey = ApiKeyManager.hasValidApiKey(context)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0D1B2A))
                        .border(
                            1.dp,
                            if (hasKey) Color(0xFF00E5FF).copy(alpha = 0.6f) else Color(0xFFFF9100).copy(alpha = 0.5f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = AppIcons.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (hasKey) Color(0xFF00E5FF) else Color(0xFFFF9100),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "Gemini 2.5 Flash Cloud AI",
                                        color = NeuralTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (hasKey) "ინტერნეტ-სინთეზი აქტიურია" else "On-Device Offline რეჟიმი",
                                        color = if (hasKey) Color(0xFF00E5FF) else Color(0xFFFF9100),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (hasKey) Color(0xFF00E5FF).copy(alpha = 0.15f) else Color(0xFFFF9100).copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (hasKey) "CONNECTED" else "OFFLINE",
                                    color = if (hasKey) Color(0xFF00E5FF) else Color(0xFFFF9100),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "ინტერნეტიდან ცოცხალი AI აზრების გენერაციისთვის შეიყვანეთ Gemini API Key (ან გამოიყენება ავტომატური ინტეგრაცია):",
                            color = NeuralTextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )

                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = {
                                apiKeyInput = it
                                showKeySavedFeedback = false
                            },
                            placeholder = {
                                Text(
                                    text = "AIzaSy... (Gemini API Key)",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF00E5FF),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    ApiKeyManager.saveCustomApiKey(context, apiKeyInput)
                                    showKeySavedFeedback = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (showKeySavedFeedback) "✓ შენახულია" else "💾 გასაღების შენახვა",
                                    color = Color(0xFF050B14),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (apiKeyInput.isNotBlank()) {
                                OutlinedButton(
                                    onClick = {
                                        apiKeyInput = ""
                                        ApiKeyManager.saveCustomApiKey(context, "")
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "წაშლა",
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Direct Button to System App Settings
                OutlinedButton(
                    onClick = {
                        PermissionHelper.openAppSettings(context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Settings,
                        contentDescription = "პარამეტრები",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "⚙️ Android-ის სისტემური პარამეტრების გახსნა",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // AI Info & Technology Button
                Button(
                    onClick = {
                        onDismissRequest()
                        onOpenExplanationClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeuralDeepPurple
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = AppIcons.Psychology,
                        contentDescription = "AI განმარტება",
                        tint = NeuralAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "როგორ მუშაობს & Galaxy Buds 2 ანალიზი",
                        color = NeuralAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionRowItem(
    icon: ImageVector,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NeuralCardPurple)
            .border(
                1.dp,
                if (isChecked) NeuralAccent.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.05f),
                RoundedCornerShape(16.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isChecked) NeuralAccent.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isChecked) NeuralAccent else NeuralTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    color = NeuralTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    color = NeuralTextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.size(8.dp))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeuralDeepPurple,
                checkedTrackColor = NeuralAccent,
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}
