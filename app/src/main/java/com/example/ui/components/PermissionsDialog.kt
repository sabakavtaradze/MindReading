package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

@Composable
fun PermissionsDialog(
    micGranted: Boolean,
    usageStatsGranted: Boolean,
    accessibilityGranted: Boolean,
    overlayGranted: Boolean,
    onMicToggle: (Boolean) -> Unit,
    onUsageStatsToggle: (Boolean) -> Unit,
    onAccessibilityToggle: (Boolean) -> Unit,
    onOverlayToggle: (Boolean) -> Unit,
    onOpenExplanationClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.3f), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Permissions",
                            tint = NeuralAccent,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "System Permissions",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NeuralTextSecondary
                        )
                    }
                }

                Text(
                    text = "Explicit user consent is required for background context tracking and intent prediction.",
                    color = NeuralTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                PermissionRowItem(
                    icon = Icons.Default.Mic,
                    title = "Microphone Audio Sampling",
                    description = "Analyze ambient sound decibels for activity context",
                    isChecked = micGranted,
                    onCheckedChange = onMicToggle
                )

                PermissionRowItem(
                    icon = Icons.Default.TouchApp,
                    title = "Accessibility Touch Events",
                    description = "Detect tap gestures & screen interactions",
                    isChecked = accessibilityGranted,
                    onCheckedChange = onAccessibilityToggle
                )

                PermissionRowItem(
                    icon = Icons.Default.Visibility,
                    title = "Usage Stats & App Context",
                    description = "Observe active foreground application type",
                    isChecked = usageStatsGranted,
                    onCheckedChange = onUsageStatsToggle
                )

                PermissionRowItem(
                    icon = Icons.Default.Lock,
                    title = "System Overlay Floating UI",
                    description = "Display real-time intent HUD over apps",
                    isChecked = overlayGranted,
                    onCheckedChange = onOverlayToggle
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeuralDeepPurple)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Privacy",
                        tint = NeuralAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "Permissions are strictly transparent and manageable at any time.",
                        color = NeuralTextPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onOpenExplanationClick()
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Technical Limits & Privacy", fontSize = 12.sp, color = Color.White)
                    }

                    Button(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                        modifier = Modifier.weight(0.8f)
                    ) {
                        Text("Done", fontSize = 12.sp, color = NeuralDeepPurple, fontWeight = FontWeight.Bold)
                    }
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
            .background(NeuralDeepPurple.copy(alpha = 0.4f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(NeuralAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeuralAccent,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                color = NeuralTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeuralDeepPurple,
                checkedTrackColor = NeuralAccent,
                uncheckedThumbColor = NeuralTextSecondary,
                uncheckedTrackColor = NeuralSurface
            )
        )
    }
}
