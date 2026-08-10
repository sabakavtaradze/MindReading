package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ExplanationModal
import com.example.ui.components.HeaderView
import com.example.ui.components.HeroNeuralOverlay
import com.example.ui.components.IntentPredictionCard
import com.example.ui.components.InteractiveTouchPad
import com.example.ui.components.MindLabView
import com.example.ui.components.PermissionsDialog
import com.example.ui.components.SynapticHistoryView
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.viewmodel.NeuroSyncViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class NeuroTab { INFERENCE, MIND_LAB, SENSORS, LOGS }

@Composable
fun NeuroSyncApp(
    viewModel: NeuroSyncViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.history.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(NeuroTab.INFERENCE) }

    Scaffold(
        containerColor = NeuralBackground,
        bottomBar = {
            FooterBar(
                isSyncing = uiState.isSyncing,
                onPermissionsClick = { viewModel.setPermissionsModalOpen(true) },
                onSyncToggleClick = { viewModel.toggleSyncing() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Header
                HeaderView(isSyncing = uiState.isSyncing)

                // Hero Synaptic Overlay Visualizer
                HeroNeuralOverlay(
                    matchPercentage = uiState.matchPercentage,
                    statusText = uiState.statusText,
                    touchValue = uiState.telemetry.touchValue,
                    audioValue = uiState.telemetry.audioValue,
                    visualValue = uiState.telemetry.visualValue,
                    motionValue = uiState.telemetry.motionValue,
                    biometricsValue = uiState.telemetry.biometricsValue,
                    neuralValue = uiState.telemetry.neuralValue,
                    isSyncing = uiState.isSyncing,
                    onCenterCoreClick = { viewModel.runNeuralPredictionInference() },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                // Section Navigation Tabs
                TabSelectorBar(
                    activeTab = activeTab,
                    onTabSelected = { activeTab = it },
                    onInfoClick = { viewModel.setExplanationModalOpen(true) },
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                // Tab Content Switcher
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    when (activeTab) {
                        NeuroTab.INFERENCE -> {
                            IntentPredictionCard(
                                title = uiState.currentPredictionTitle,
                                summary = uiState.currentPredictionText,
                                actionPlan = uiState.currentActionPlan,
                                isGenerating = uiState.isGeneratingPrediction,
                                onRunInferenceClick = { viewModel.runNeuralPredictionInference() },
                                subconsciousFocusLevel = uiState.subconsciousFocusLevel,
                                alphaBandHz = uiState.alphaBandHz,
                                betaBandHz = uiState.betaBandHz,
                                thetaBandHz = uiState.thetaBandHz,
                                gammaBandHz = uiState.gammaBandHz,
                                thoughtCognitiveLoadPct = uiState.thoughtCognitiveLoadPct,
                                onDecodeCustomThought = { viewModel.decodeCustomThought(it) }
                            )
                        }
                        NeuroTab.MIND_LAB -> {
                            MindLabView(
                                alphaBandHz = uiState.alphaBandHz,
                                betaBandHz = uiState.betaBandHz,
                                thetaBandHz = uiState.thetaBandHz,
                                gammaBandHz = uiState.gammaBandHz,
                                cognitiveLoadPct = uiState.thoughtCognitiveLoadPct,
                                subconsciousFocusLevel = uiState.subconsciousFocusLevel,
                                onInjectStimulus = { viewModel.injectStimulus(it) }
                            )
                        }
                        NeuroTab.SENSORS -> {
                            InteractiveTouchPad(
                                touchTapsCount = uiState.touchTapsCount,
                                lastTouchCoords = uiState.lastTouchCoords,
                                audioDb = uiState.audioDb,
                                activeAppContext = uiState.activeAppContext,
                                cameraGazeX = uiState.cameraGazeX,
                                cameraGazeY = uiState.cameraGazeY,
                                speakerOutputDb = uiState.speakerOutputDb,
                                motionTremor = uiState.motionTremor,
                                heartRateBpm = uiState.heartRateBpm,
                                onTapRegistered = { x, y -> viewModel.registerTouchTap(x, y) },
                                onContextSelect = { viewModel.setAppContext(it) },
                                onAudioDbChange = { viewModel.updateAudioDb(it) },
                                onHeartRateChange = { viewModel.updateHeartRate(it) },
                                onMotionTremorChange = { viewModel.updateMotionTremor(it) }
                            )
                        }
                        NeuroTab.LOGS -> {
                            SynapticHistoryView(
                                historyList = historyList,
                                onClearClick = { viewModel.clearHistory() }
                            )
                        }
                    }
                }
            }
        }

        // Dialog Modals
        if (uiState.isPermissionsModalOpen) {
            PermissionsDialog(
                micGranted = uiState.micPermissionGranted,
                usageStatsGranted = uiState.usageStatsPermissionGranted,
                accessibilityGranted = uiState.accessibilityPermissionGranted,
                overlayGranted = uiState.overlayPermissionGranted,
                onMicToggle = { viewModel.setMicPermission(it) },
                onUsageStatsToggle = { viewModel.setUsageStatsPermission(it) },
                onAccessibilityToggle = { viewModel.setAccessibilityPermission(it) },
                onOverlayToggle = { viewModel.setOverlayPermission(it) },
                onOpenExplanationClick = { viewModel.setExplanationModalOpen(true) },
                onDismissRequest = { viewModel.setPermissionsModalOpen(false) }
            )
        }

        if (uiState.isExplanationModalOpen) {
            ExplanationModal(
                onDismissRequest = { viewModel.setExplanationModalOpen(false) }
            )
        }
    }
}

@Composable
private fun TabSelectorBar(
    activeTab: NeuroTab,
    onTabSelected: (NeuroTab) -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NeuralSurface)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabPillItem("Predict", activeTab == NeuroTab.INFERENCE, modifier = Modifier.weight(1f)) {
            onTabSelected(NeuroTab.INFERENCE)
        }
        TabPillItem("Mind Lab", activeTab == NeuroTab.MIND_LAB, modifier = Modifier.weight(1f)) {
            onTabSelected(NeuroTab.MIND_LAB)
        }
        TabPillItem("Sensors", activeTab == NeuroTab.SENSORS, modifier = Modifier.weight(1f)) {
            onTabSelected(NeuroTab.SENSORS)
        }
        TabPillItem("Logs", activeTab == NeuroTab.LOGS, modifier = Modifier.weight(1f)) {
            onTabSelected(NeuroTab.LOGS)
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NeuralDeepPurple)
                .clickable { onInfoClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Limits Info",
                tint = NeuralAccent,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun TabPillItem(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) NeuralAccent else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) NeuralDeepPurple else Color.White
        )
    }
}

@Composable
private fun FooterBar(
    isSyncing: Boolean,
    onPermissionsClick: () -> Unit,
    onSyncToggleClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Permissions Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(CircleShape)
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, CircleShape)
                .clickable { onPermissionsClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = NeuralAccent,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Permissions",
                    color = NeuralAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Right Syncing Button
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(CircleShape)
                .background(NeuralAccent)
                .clickable { onSyncToggleClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isSyncing) Icons.Default.StopCircle else Icons.Default.PlayCircle,
                    contentDescription = "Syncing",
                    tint = NeuralDeepPurple,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isSyncing) "Syncing" else "Paused",
                    color = NeuralDeepPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
