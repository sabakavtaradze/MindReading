package com.example.ui.components

import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sensor.RealAudioState
import com.example.sensor.RealCameraGazeState
import com.example.sensor.RealHardwareSensorState
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralCardPurple
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

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
    realSensors: RealHardwareSensorState = RealHardwareSensorState(),
    realAudio: RealAudioState = RealAudioState(),
    cameraGaze: RealCameraGazeState = RealCameraGazeState(),
    onTapRegistered: (Float, Float) -> Unit,
    onContextSelect: (String) -> Unit,
    onAudioDbChange: (Float) -> Unit = {},
    onHeartRateChange: (Int) -> Unit = {},
    onMotionTremorChange: (Float) -> Unit = {},
    onToggleCamera: () -> Unit = {},
    onRequestMasterPermissions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val touchPoints = remember { mutableStateListOf<TouchPoint>() }
    var isLiveCameraPreviewExpanded by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. MASTER SENSOR ACTIVATION HEADER BANNER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1E1035),
                            Color(0xFF0F172A),
                            Color(0xFF052E2B)
                        )
                    )
                )
                .border(1.dp, NeuralAccent.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                .padding(16.dp)
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
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (realSensors.isTracking) Color(0xFF00E676) else Color(0xFFFFAB00))
                        )
                        Text(
                            text = "ტექნიკური სენსორების ცენტრი",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeuralAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (realSensors.isTracking) "სენსორები აქტიურია (${realSensors.totalActiveHardwareSensors} REAL)" else "მოლოდინის რეჟიმი",
                            color = NeuralAccent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "სისტემაში ჩართულია: აქსელერომეტრი, გიროსკოპი, მაგნიტომეტრი, ბარომეტრი, განათება, სიახლოვე, ნაბიჯები, ტემპერატურა, მიკროფონი და კამერა.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onRequestMasterPermissions,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.CheckCircle,
                            contentDescription = "Activate",
                            tint = NeuralDeepPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ყველას გააქტიურება",
                            color = NeuralDeepPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onToggleCamera,
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralCardPurple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = AppIcons.CameraFront,
                            contentDescription = "Camera",
                            tint = NeuralAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (cameraGaze.isCameraActive) "კამერა ჩართულია" else "კამერის ჩართვა",
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. LIVE CAMERA GAZE & OCULAR TELEMETRY
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.CameraFront,
                            contentDescription = "Camera Gaze",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "კამერა & თვალის მზერის ტრეკერი (HUD)",
                            color = Color(0xFF00E5FF),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (cameraGaze.isCameraActive) "ცოცხალი კადრი" else "არხი მზადაა",
                            color = if (cameraGaze.isCameraActive) Color(0xFF00E676) else NeuralTextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (cameraGaze.isCameraActive) Color(0xFF00E676) else Color.Gray)
                        )
                    }
                }

                // Interactive Eye Position & Metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Eye Tracking Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(95.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeuralDeepPurple)
                            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(16.dp))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val pupilX = size.width * cameraGazeX
                            val pupilY = size.height * cameraGazeY
                            val pupilRad = 6f + (cameraGaze.opticalPupilDiameterMm * 1.5f)

                            // Crosshair grid
                            drawLine(
                                color = Color.White.copy(alpha = 0.1f),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 1f
                            )
                            drawLine(
                                color = Color.White.copy(alpha = 0.1f),
                                start = Offset(size.width / 2, 0f),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = 1f
                            )

                            // Eye sclera
                            drawCircle(
                                color = Color(0xFF00E5FF).copy(alpha = 0.15f),
                                radius = 28f,
                                center = Offset(pupilX, pupilY)
                            )

                            // Pupil
                            drawCircle(
                                color = Color(0xFF00E5FF),
                                radius = pupilRad,
                                center = Offset(pupilX, pupilY)
                            )
                        }

                        Text(
                            text = "მზერა: ${String.format(Locale.US, "X:%.2f Y:%.2f", cameraGazeX, cameraGazeY)}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
                    }

                    // Right Ocular Data Column
                    Column(
                        modifier = Modifier.weight(1.2f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MetricPillRow("გუგის დიამეტრი", "${String.format(Locale.US, "%.2f", cameraGaze.opticalPupilDiameterMm)} mm", Color(0xFF00E5FF))
                        MetricPillRow("ხამხამი წუთში", "${cameraGaze.eyeBlinkRatePerMin} /წთ", NeuralAccent)
                        MetricPillRow("ოპტიკური პულსი (rPPG)", "${cameraGaze.opticalRadiancePulseBpm} BPM", Color(0xFFFF5252))
                        MetricPillRow("განათების დონე", "${cameraGaze.lightingLevelLux.toInt()} Lux", Color(0xFFFFD54F))
                    }
                }

                Text(
                    text = "ფოკუსის ზონა: ${cameraGaze.fixationZone}",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 3. REAL ACOUSTIC FREQUENCY & MICROPHONE ANALYZER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Mic,
                            contentDescription = "Microphone",
                            tint = NeuralAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "მიკროფონი & აკუსტიკური სპექტრი",
                            color = NeuralAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${String.format(Locale.US, "%.1f", realAudio.decibels)} dB",
                        color = if (realAudio.decibels > 65f) Color(0xFFFF5252) else NeuralAccent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 8-Band Live Spectrum Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeuralDeepPurple)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val samples = realAudio.waveformSamples
                        val count = if (samples.isNotEmpty()) samples.size.coerceAtMost(16) else 8
                        val barWidth = (size.width - (count - 1) * 6f) / count

                        for (i in 0 until count) {
                            val rawVal = samples.getOrElse(i) { (i + 1) * 0.1f }
                            val heightFrac = rawVal.coerceIn(0.08f, 1.0f)
                            val barHeight = size.height * heightFrac
                            val x = i * (barWidth + 6f)
                            val y = size.height - barHeight

                            val barColor = when (i % 4) {
                                0 -> Color(0xFF9C27B0)
                                1 -> Color(0xFF673AB7)
                                2 -> Color(0xFF00E5FF)
                                else -> Color(0xFF00E676)
                            }

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(x, y),
                                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "პიკური დეციბელი: ${String.format(Locale.US, "%.1f", realAudio.peakDecibels)} dB",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "დომინანტური სიხშირე: ${realAudio.dominantFrequencyHz.toInt()} Hz",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // 4. KINEMATIC MOTION & 3-AXIS ACCELEROMETER / GYROSCOPE
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.ScreenRotation,
                            contentDescription = "Motion",
                            tint = Color(0xFFFF9100),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "მოძრაობა, აქსელერომეტრი & გიროსკოპი",
                            color = Color(0xFFFF9100),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${String.format(Locale.US, "%.2f", realSensors.microTremorMagnitude)} m/s²",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // 3D Axis Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AxisDisplayCard("Accel X", "${String.format(Locale.US, "%.2f", realSensors.accelX)}", Modifier.weight(1f))
                    AxisDisplayCard("Accel Y", "${String.format(Locale.US, "%.2f", realSensors.accelY)}", Modifier.weight(1f))
                    AxisDisplayCard("Accel Z", "${String.format(Locale.US, "%.2f", realSensors.accelZ)}", Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AxisDisplayCard("Gyro Pitch", "${String.format(Locale.US, "%.1f", realSensors.pitchDeg)}°", Modifier.weight(1f))
                    AxisDisplayCard("Gyro Roll", "${String.format(Locale.US, "%.1f", realSensors.rollDeg)}°", Modifier.weight(1f))
                    AxisDisplayCard("სტაბილურობა", "${realSensors.neuromuscularStabilityPct}%", Modifier.weight(1f), Color(0xFF00E676))
                }
            }
        }

        // 5. 3D MAGNETOMETER & COMPASS HEADING
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = "Compass",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "მაგნიტომეტრი & სივრცითი კომპასი",
                            color = Color(0xFF00E5FF),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${realSensors.compassHeadingDeg.toInt()}° ${realSensors.compassCardinal}",
                        color = Color(0xFF00E5FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Compass Rose Canvas & Magnetic Vectors
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Compass dial
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(NeuralDeepPurple)
                            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f), CircleShape)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2, size.height / 2)
                            val rad = size.width / 2 - 8f
                            val angleRad = Math.toRadians((realSensors.compassHeadingDeg - 90).toDouble())

                            val needleEnd = Offset(
                                (center.x + rad * cos(angleRad)).toFloat(),
                                (center.y + rad * sin(angleRad)).toFloat()
                            )
                            val needleTail = Offset(
                                (center.x - rad * 0.5f * cos(angleRad)).toFloat(),
                                (center.y - rad * 0.5f * sin(angleRad)).toFloat()
                            )

                            // North Needle
                            drawLine(
                                color = Color(0xFFFF5252),
                                start = center,
                                end = needleEnd,
                                strokeWidth = 3f,
                                cap = StrokeCap.Round
                            )

                            // South Needle
                            drawLine(
                                color = Color.White.copy(alpha = 0.5f),
                                start = center,
                                end = needleTail,
                                strokeWidth = 2f,
                                cap = StrokeCap.Round
                            )

                            drawCircle(color = Color.White, radius = 3f, center = center)
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MetricPillRow("Mag X", "${String.format(Locale.US, "%.1f", realSensors.magX)} μT", Color.White)
                        MetricPillRow("Mag Y", "${String.format(Locale.US, "%.1f", realSensors.magY)} μT", Color.White)
                        MetricPillRow("Mag Z", "${String.format(Locale.US, "%.1f", realSensors.magZ)} μT", Color.White)
                    }
                }
            }
        }

        // 6. ATMOSPHERIC BAROMETER, AMBIENT LIGHT & ENVIRONMENT
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = AppIcons.WaterDrop,
                        contentDescription = "Environment",
                        tint = Color(0xFF64B5F6),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ატმოსფერო & გარემო პირობები",
                        color = Color(0xFF64B5F6),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EnvironmentCard(
                        title = "ბარომეტრი",
                        value = "${String.format(Locale.US, "%.1f", realSensors.atmosphericPressureHpa)} hPa",
                        subtitle = "სიმაღლე: ${realSensors.estimatedAltitudeMeters.toInt()} მ",
                        modifier = Modifier.weight(1f)
                    )
                    EnvironmentCard(
                        title = "განათება",
                        value = "${realSensors.ambientLightLux.toInt()} Lux",
                        subtitle = realSensors.lightCondition,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EnvironmentCard(
                        title = "ტემპერატურა",
                        value = "${String.format(Locale.US, "%.1f", realSensors.ambientTemperatureC)}°C",
                        subtitle = "გარემო თერმული",
                        modifier = Modifier.weight(1f)
                    )
                    EnvironmentCard(
                        title = "ტენიანობა",
                        value = "${realSensors.relativeHumidityPct.toInt()}%",
                        subtitle = "ფარდობითი",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 7. PEDOMETER & PROXIMITY SENSOR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Steps Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "პედომეტრი / ნაბიჯები",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${realSensors.totalStepsDetected} ნაბიჯი",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (realSensors.isUserMoving) "🏃 მოძრაობა დაფიქსირებულია" else "🧘 უძრავი პოზიცია",
                        color = if (realSensors.isUserMoving) Color(0xFF00E676) else NeuralTextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            // Proximity Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(NeuralSurface)
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "სიახლოვის სენსორი",
                        color = Color(0xFF00E5FF),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.1f", realSensors.proximityDistanceCm)} cm",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (realSensors.isNearEarOrFace) "⚠️ ობიექტი ახლოსაა (Near)" else "✓ თავისუფალი არე (Far)",
                        color = if (realSensors.isNearEarOrFace) Color(0xFFFFAB00) else Color(0xFF00E676),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 8. TOUCH SCREEN TELEMETRY & TAP HEATMAP
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
                        text = "შეხების ტელემეტრია & ჰიტმაპი",
                        color = NeuralAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "შეხებები: $touchTapsCount | პოზიცია: $lastTouchCoords",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // 9. SYSTEM HEALTH, BATTERY & RAM MEMORY METRICS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "აპარატურული სისტემის რესურსები & ბატარეა",
                    color = NeuralAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ბატარეა: ${realSensors.batteryPct}% ${if (realSensors.isCharging) "(იტენება)" else ""}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "RAM: ${realSensors.totalRamMb - realSensors.availRamMb}MB / ${realSensors.totalRamMb}MB (${realSensors.ramUsagePct}%)",
                        color = Color(0xFF00E5FF),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Progress Bar for RAM
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(NeuralDeepPurple)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((realSensors.ramUsagePct / 100f).coerceIn(0.05f, 1f))
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(if (realSensors.ramUsagePct > 80) Color(0xFFFF5252) else Color(0xFF00E5FF))
                    )
                }
            }
        }

        // 10. Context Switcher
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
                    text = "აქტიური აპლიკაციის კონტექსტი",
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
    }
}

@Composable
private fun MetricPillRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = NeuralTextSecondary, fontSize = 10.sp)
        Text(text = value, color = valueColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun AxisDisplayCard(label: String, value: String, modifier: Modifier = Modifier, valueColor: Color = Color.White) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NeuralDeepPurple)
            .padding(vertical = 6.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, color = NeuralTextSecondary, fontSize = 9.sp)
            Text(text = value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun EnvironmentCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(NeuralDeepPurple)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, color = NeuralTextSecondary, fontSize = 10.sp)
            Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
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


