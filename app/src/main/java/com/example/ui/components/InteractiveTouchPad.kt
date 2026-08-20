package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary

data class TouchPoint(val offset: Offset, val timestamp: Long)

@Composable
fun InteractiveTouchPad(
    touchTapsCount: Int,
    lastTouchCoords: String,
    audioDb: Float,
    activeAppContext: String,
    cameraGazeX: Float = 0.48f,
    cameraGazeY: Float = 0.32f,
    speakerOutputDb: Float = 68.0f,
    motionTremor: Float = 1.2f,
    heartRateBpm: Int = 74,
    onTapRegistered: (Float, Float) -> Unit,
    onContextSelect: (String) -> Unit,
    onAudioDbChange: (Float) -> Unit = {},
    onHeartRateChange: (Int) -> Unit = {},
    onMotionTremorChange: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val touchPoints = remember { mutableStateListOf<TouchPoint>() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Touch Input Test Grid Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onTapRegistered(offset.x, offset.y)
                        touchPoints.add(TouchPoint(offset, System.currentTimeMillis()))
                        if (touchPoints.size > 15) {
                            touchPoints.removeAt(0)
                        }
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val currentTime = System.currentTimeMillis()
                touchPoints.forEach { point ->
                    val age = (currentTime - point.timestamp) / 1000f
                    if (age < 3f) {
                        val radius = (1f - age / 3f) * 45f + 10f
                        val alpha = (1f - age / 3f).coerceIn(0f, 1f)
                        drawCircle(
                            color = NeuralAccent.copy(alpha = alpha * 0.7f),
                            radius = radius,
                            center = point.offset
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = alpha),
                            radius = 4f,
                            center = point.offset
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = AppIcons.TouchApp,
                        contentDescription = "Touch Stream",
                        tint = NeuralAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "TOUCH TELEMETRY & TAP HEATMAP",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Taps Captured: $touchTapsCount | Pos: $lastTouchCoords",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Camera Gaze + Motion Accelerometer Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Camera Gaze Eye Position Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.CameraFront,
                            contentDescription = "Camera Gaze",
                            tint = NeuralAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "CAMERA GAZE",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeuralDeepPurple)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val pupilX = size.width * cameraGazeX
                            val pupilY = size.height * cameraGazeY
                            // Eye Outline
                            drawCircle(
                                color = NeuralAccent.copy(alpha = 0.3f),
                                radius = 22f,
                                center = Offset(pupilX, pupilY)
                            )
                            // Pupil
                            drawCircle(
                                color = NeuralAccent,
                                radius = 8f,
                                center = Offset(pupilX, pupilY)
                            )
                        }
                    }
                }
            }

            // Motion & Gyroscope Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.ScreenRotation,
                            contentDescription = "Motion Gyro",
                            tint = NeuralAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "MOTION TREMOR",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${String.format(java.util.Locale.US, "%.1f", motionTremor)} m/s²",
                        color = NeuralTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                    ) {
                        val path = Path()
                        val w = size.width
                        val h = size.height
                        path.moveTo(0f, h * 0.5f)
                        path.lineTo(w * 0.2f, h * 0.5f - motionTremor * 3)
                        path.lineTo(w * 0.4f, h * 0.5f + motionTremor * 4)
                        path.lineTo(w * 0.6f, h * 0.5f - motionTremor * 2)
                        path.lineTo(w * 0.8f, h * 0.5f + motionTremor * 3)
                        path.lineTo(w, h * 0.5f)

                        drawPath(
                            path = path,
                            color = NeuralAccent,
                            style = Stroke(width = 3f)
                        )
                    }
                }
            }
        }

        // Audio Dual Spectrum + Wearable Heart Rate Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Audio Mic & Speaker Spectrum Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Mic,
                            contentDescription = "Audio Level",
                            tint = NeuralAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "MIC & SPEAKER AUDIO",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Mic In: ${audioDb.toInt()} dB", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        Icon(imageVector = AppIcons.VolumeUp, contentDescription = "Speaker", tint = NeuralTextSecondary, modifier = Modifier.size(14.dp))
                        Text(text = "Speaker: ${speakerOutputDb.toInt()} dB", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(NeuralDeepPurple)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth((audioDb / 100f).coerceIn(0.05f, 1f))
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(NeuralAccent)
                        )
                    }
                }
            }

            // Biometrics Heart Rate Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.WaterDrop,
                            contentDescription = "Heart Rate",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "WEARABLE PULSE",
                            color = Color(0xFFFF5252),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "$heartRateBpm BPM",
                        color = NeuralTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Active App Context Selector Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "ACTIVE FOREGROUND CONTEXT",
                    color = NeuralAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activeAppContext,
                    color = NeuralTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    ContextPill("IDE Code", activeAppContext) { onContextSelect("Developer IDE & Code Analysis") }
                    ContextPill("Social", activeAppContext) { onContextSelect("Messaging & Social Stream") }
                    ContextPill("Gaming", activeAppContext) { onContextSelect("High Frame Rate Mobile Gaming") }
                }
            }
        }

        // Live Sensor Parameter Tuning Sliders Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "LIVE SENSOR SIMULATION TUNING",
                    color = NeuralAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                // Mic dB Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ambient Audio Noise", color = NeuralTextSecondary, fontSize = 11.sp)
                        Text("${audioDb.toInt()} dB", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = audioDb,
                        onValueChange = onAudioDbChange,
                        valueRange = 10f..95f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeuralAccent,
                            activeTrackColor = NeuralAccent,
                            inactiveTrackColor = NeuralDeepPurple
                        )
                    )
                }

                // Heart Rate BPM Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Wearable Heart Rate", color = NeuralTextSecondary, fontSize = 11.sp)
                        Text("$heartRateBpm BPM", color = Color(0xFFFF5252), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = heartRateBpm.toFloat(),
                        onValueChange = { onHeartRateChange(it.toInt()) },
                        valueRange = 50f..140f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF5252),
                            activeTrackColor = Color(0xFFFF5252),
                            inactiveTrackColor = NeuralDeepPurple
                        )
                    )
                }

                // Motion Tremor Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Accelerometer Tremor", color = NeuralTextSecondary, fontSize = 11.sp)
                        Text("${String.format(java.util.Locale.US, "%.1f", motionTremor)} m/s²", color = NeuralTextPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = motionTremor,
                        onValueChange = onMotionTremorChange,
                        valueRange = 0.1f..5f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeuralAccent,
                            activeTrackColor = NeuralAccent,
                            inactiveTrackColor = NeuralDeepPurple
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ContextPill(
    label: String,
    current: String,
    onClick: () -> Unit
) {
    val isSelected = current.contains(label, ignoreCase = true)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) NeuralAccent else NeuralDeepPurple)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) NeuralDeepPurple else NeuralTextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

