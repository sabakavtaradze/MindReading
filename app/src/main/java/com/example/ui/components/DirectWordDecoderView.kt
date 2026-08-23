package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.GeorgianNeuroLinguisticEngine
import com.example.ui.theme.AppIcons
import com.example.ui.theme.NeuralAccent
import com.example.ui.theme.NeuralBackground
import com.example.ui.theme.NeuralBorder
import com.example.ui.theme.NeuralDeepPurple
import com.example.ui.theme.NeuralSurface
import com.example.ui.theme.NeuralTextPrimary
import com.example.ui.theme.NeuralTextSecondary
import com.example.viewmodel.DecodedWordCandidate
import com.example.viewmodel.DecodedWordHistoryItem
import com.example.viewmodel.DirectWordDecoderState
import com.example.viewmodel.SubjectRecognitionState
import com.example.viewmodel.WordPredictionAnalyticsState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DirectWordDecoderView(
    wordDecoderState: DirectWordDecoderState,
    subjectState: SubjectRecognitionState? = null,
    predictionState: WordPredictionAnalyticsState? = null,
    onToggleLiveDecoding: () -> Unit,
    onSetCategory: (String) -> Unit,
    onInjectWord: (String, String) -> Unit,
    onClearSentence: () -> Unit,
    onCycleNextWord: () -> Unit,
    onSelectActiveSubject: ((String) -> Unit)? = null,
    onToggleTargetLock: (() -> Unit)? = null,
    onToggleContaminationShield: (() -> Unit)? = null,
    onToggleAutoSwitch: (() -> Unit)? = null,
    onSimulateDetectedChange: ((String) -> Unit)? = null,
    onAddNewSubject: ((String, String) -> Unit)? = null,
    onTogglePreMotorPredictor: (() -> Unit)? = null,
    onApplyBranchPrediction: ((String) -> Unit)? = null,
    onRegeneratePredictionBranches: (() -> Unit)? = null,
    onToggleMarkovContext: (() -> Unit)? = null,
    onToggleGazeDwell: (() -> Unit)? = null,
    onToggleBilingual: (() -> Unit)? = null,
    onLearnMarkovPair: ((String, String) -> Unit)? = null,
    onCycleScreenContext: (() -> Unit)? = null,
    onToggleHrvCompensation: (() -> Unit)? = null,
    onTogglePhoneticSnap: (() -> Unit)? = null,
    onToggleMicroSaccade: (() -> Unit)? = null,
    onToggleNeuroGrammar: (() -> Unit)? = null,
    onToggleEnergyPreserver: (() -> Unit)? = null,
    onTogglePhonemeCompression: (() -> Unit)? = null,
    onToggle3DNeuroSpatial: (() -> Unit)? = null,
    onToggleAffectiveTone: (() -> Unit)? = null,
    onCycleAffectiveTone: (() -> Unit)? = null,
    onToggleUnifiedEngine: (() -> Unit)? = null,
    onSynthesizeUnifiedThought: (() -> Unit)? = null,
    onSimulateBioStress: (() -> Unit)? = null,
    onUpdateWeights: ((Float, Float, Float, Float) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var customWordInput by remember { mutableStateOf("") }
    var lexiconSearchQuery by remember { mutableStateOf("") }
    var isMorphologyExplorerExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 0. SUBJECT RECOGNITION & BIOMETRIC SHIELD CARD ---
        if (subjectState != null && onSelectActiveSubject != null && onToggleTargetLock != null && onToggleContaminationShield != null && onToggleAutoSwitch != null && onSimulateDetectedChange != null && onAddNewSubject != null) {
            SubjectRecognitionCard(
                subjectState = subjectState,
                onSelectActiveSubject = onSelectActiveSubject,
                onToggleTargetLock = onToggleTargetLock,
                onToggleContaminationShield = onToggleContaminationShield,
                onToggleAutoSwitch = onToggleAutoSwitch,
                onSimulateDetectedChange = onSimulateDetectedChange,
                onAddNewSubject = onAddNewSubject
            )
        }

        // --- 0.5. PRE-MOTOR WORD PREDICTOR & DATA ANALYTICS CARD ---
        if (predictionState != null && onTogglePreMotorPredictor != null && onApplyBranchPrediction != null && onRegeneratePredictionBranches != null) {
            WordPredictorAnalyticsCard(
                predictionState = predictionState,
                onTogglePreMotorPredictor = onTogglePreMotorPredictor,
                onApplyBranch = onApplyBranchPrediction,
                onRegenerateBranches = onRegeneratePredictionBranches,
                onToggleMarkovContext = onToggleMarkovContext,
                onToggleGazeDwell = onToggleGazeDwell,
                onToggleBilingual = onToggleBilingual,
                onLearnMarkovPair = onLearnMarkovPair,
                onCycleScreenContext = onCycleScreenContext,
                onToggleHrvCompensation = onToggleHrvCompensation,
                onTogglePhoneticSnap = onTogglePhoneticSnap,
                onToggleMicroSaccade = onToggleMicroSaccade,
                onToggleNeuroGrammar = onToggleNeuroGrammar,
                onToggleEnergyPreserver = onToggleEnergyPreserver,
                onTogglePhonemeCompression = onTogglePhonemeCompression,
                onToggle3DNeuroSpatial = onToggle3DNeuroSpatial,
                onToggleAffectiveTone = onToggleAffectiveTone,
                onCycleAffectiveTone = onCycleAffectiveTone,
                onToggleUnifiedEngine = onToggleUnifiedEngine,
                onSynthesizeUnifiedThought = onSynthesizeUnifiedThought,
                onSimulateBioStress = onSimulateBioStress,
                onUpdateWeights = onUpdateWeights
            )
        }

        // --- 1. HERO DECODED WORD HUD ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NeuralSurface,
                            NeuralDeepPurple.copy(alpha = 0.85f)
                        )
                    )
                )
                .border(1.5.dp, NeuralAccent.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Header Status Bar
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
                                .background(if (wordDecoderState.isLiveDecoding) Color(0xFF00FFB2) else Color(0xFFFF5252))
                        )
                        Text(
                            text = if (wordDecoderState.isLiveDecoding) "პირდაპირი სიტყვების დეკოდერი • LIVE" else "დეკოდერი დაპაუზებულია",
                            color = if (wordDecoderState.isLiveDecoding) Color(0xFF00FFB2) else Color(0xFFFF5252),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralAccent.copy(alpha = 0.15f))
                            .clickable { onToggleLiveDecoding() }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (wordDecoderState.isLiveDecoding) "დაპაუზება" else "გააქტიურება",
                            color = NeuralAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Main Decoded Word Showcase
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "დეკოდირებული აზრი / შინაგანი სიტყვა",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "„${wordDecoderState.currentDecodedWord}“",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF00FFB2).copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "სიზუსტე: ${wordDecoderState.confidencePct}%",
                                color = Color(0xFF00FFB2),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF8A2BE2).copy(alpha = 0.3f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "VPU: ${String.format(java.util.Locale.US, "%.1f", wordDecoderState.internalSpeechVpuFrequencyHz)} Hz",
                                color = Color(0xFFD4B2FF),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Phoneme Sequence Bar
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "სუბვოკალური ფონემური ტრასირება (EMG / Phonation):",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        wordDecoderState.currentPhonemes.forEach { phoneme ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.08f))
                                    .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = phoneme,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. CANDIDATE WORDS RANKING (TOP 4 CANDIDATES) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "სავარაუდო კანდიდატი სიტყვები (ნეირონული ალბათობა)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap ასარჩევად",
                        color = NeuralTextSecondary,
                        fontSize = 10.sp
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    wordDecoderState.candidateWords.forEachIndexed { index, candidate ->
                        CandidateWordRow(
                            rank = index + 1,
                            candidate = candidate,
                            onClick = { onInjectWord(candidate.word, candidate.category) }
                        )
                    }
                }
            }
        }

        // --- 3. ACCUMULATED THOUGHT SENTENCE BUILDER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, Color(0xFF9D00FF).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            imageVector = AppIcons.RecordVoiceOver,
                            contentDescription = null,
                            tint = Color(0xFF00FFB2),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "აწყობილი შინაგანი წინადადება / Monologue",
                            color = Color(0xFF00FFB2),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (wordDecoderState.accumulatedSentence.isNotBlank()) {
                            wordDecoderState.accumulatedSentence
                        } else {
                            "ნაკადი ცარიელია. აირჩიეთ ან წარმოიდგინეთ სიტყვები ქვემოთ..."
                        },
                        color = if (wordDecoderState.accumulatedSentence.isNotBlank()) Color.White else NeuralTextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Control Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralDeepPurple)
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Decoded Sentence", wordDecoderState.accumulatedSentence)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "წინადადება დაკოპირდა!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📋 კოპირება", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeuralAccent)
                            .clickable { onCycleNextWord() }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡ შემდეგი სიტყვა", color = NeuralDeepPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFF5252).copy(alpha = 0.2f))
                            .clickable { onClearSentence() }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧹 გასუფთავება", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- 4. INTERACTIVE EXPANDED LEXICON & MORPHOLOGY MATRIX ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ქართული ნეირო-სემანტიკური ლექსიკონი",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "130+ სიტყვა • 33 ფონემა • მორფოლოგიური ზმნები",
                            color = NeuralTextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF00FFB2).copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.size} სიტყვა",
                            color = Color(0xFF00FFB2),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Lexicon Real-Time Search Bar
                OutlinedTextField(
                    value = lexiconSearchQuery,
                    onValueChange = { lexiconSearchQuery = it },
                    placeholder = { Text("მოძებნეთ სიტყვა ან ძირი (მაგ. შევამოწმოთ, წვრთნ, კოდი)...", color = NeuralTextSecondary, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeuralAccent,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // Category Chips Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val categories = listOf(
                        "ALL" to "ყველა (${GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.size})",
                        "COMMON" to "🇬🇪 ყოველდღიური",
                        "DEV" to "💻 IT & Dev",
                        "COMMANDS" to "⚡ ბრძანებები",
                        "EMOTIONS" to "🧠 ემოციები",
                        "NEURO_SCIENCE" to "🔬 ნეირო-ტექნოლოგია",
                        "MORPHOLOGY_VERBS" to "🧬 ზმნები & კლასტერები",
                        "ENGLISH" to "🇺🇸 English"
                    )

                    categories.forEach { (key, title) ->
                        val isSelected = wordDecoderState.activeLexiconCategory == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NeuralAccent else Color.White.copy(alpha = 0.06f))
                                .clickable { onSetCategory(key) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) NeuralDeepPurple else Color.White,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                // Morphological Root Explorer Expandable Accordion
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF19102B))
                        .border(1.dp, Color(0xFF9D00FF).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable { isMorphologyExplorerExpanded = !isMorphologyExplorerExpanded }
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(text = "🧬", fontSize = 13.sp)
                                Text(
                                    text = "ქართული მორფოლოგიური ზმნური ძირების გენერატორი",
                                    color = Color(0xFF00FFB2),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (isMorphologyExplorerExpanded) "დაკეცვა ▲" else "გაშლა ▼",
                                color = NeuralAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (isMorphologyExplorerExpanded) {
                            Text(
                                text = "პოლისინთეზური ზმნისწინები (შე-, გა-, და-, მო-, გადა-) და ძირები (-მოწმ-, -კომიტ-, -წვრთნ-, -სინთეზ-) ავტომატურად წარმოქმნიან აზრის ფორმებს:",
                                color = NeuralTextSecondary,
                                fontSize = 10.sp
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                GeorgianNeuroLinguisticEngine.GEORGIAN_VERB_ROOTS.forEach { verbRoot ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black.copy(alpha = 0.35f))
                                            .padding(8.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "ძირი: -${verbRoot.root}- (${verbRoot.meaning})",
                                                    color = Color.White,
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .horizontalScroll(rememberScrollState()),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                verbRoot.forms.forEach { form ->
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Color(0xFF2A1B4E))
                                                            .border(0.8.dp, NeuralAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                            .clickable { onInjectWord(form, "MORPHOLOGY_VERBS") }
                                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = form,
                                                            color = Color.White,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Lexicon Word Grid
                val currentCategory = wordDecoderState.activeLexiconCategory
                val filteredLexicon = GeorgianNeuroLinguisticEngine.MIND_LEXICON_DATABASE.filter { entry ->
                    val categoryMatches = currentCategory == "ALL" || entry.category == currentCategory
                    val searchMatches = lexiconSearchQuery.isBlank() ||
                            entry.word.contains(lexiconSearchQuery.trim(), ignoreCase = true) ||
                            entry.description.contains(lexiconSearchQuery.trim(), ignoreCase = true) ||
                            entry.rootStem.contains(lexiconSearchQuery.trim(), ignoreCase = true)
                    categoryMatches && searchMatches
                }

                if (filteredLexicon.isEmpty()) {
                    Text(
                        text = "სიტყვა ვერ მოიძებნა. სცადეთ სხვა ძიება ან გამოიყენეთ ქვემოთ მოცემული შეყვანის ველი.",
                        color = NeuralTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        filteredLexicon.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E1E2E))
                                    .border(1.dp, Color(0xFF00FFB2).copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                    .clickable { onInjectWord(item.word, item.category) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = item.word,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "(${item.emgFrequencyHz.toInt()}Hz)",
                                        color = Color(0xFF00FFB2),
                                        fontSize = 9.sp
                                    )
                                    if (item.clusterSpeedupGainPct > 50) {
                                        Text(
                                            text = "⚡+${item.clusterSpeedupGainPct}%",
                                            color = NeuralAccent,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 5. CUSTOM WORD THOUGHT-TO-SPEECH DECODER INPUT ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "საკუთარი სიტყვის ან ფრაზის დეკოდირება",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "შეიყვანეთ ნებისმიერი ქართული ან ინგლისური სიტყვა და სისტემა გამოთვლის მის სუბვოკალურ ფონემურ მატრიცას.",
                    color = NeuralTextSecondary,
                    fontSize = 11.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customWordInput,
                        onValueChange = { customWordInput = it },
                        placeholder = { Text("ჩაწერეთ სიტყვა...", color = NeuralTextSecondary, fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeuralAccent,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (customWordInput.isNotBlank()) {
                                onInjectWord(customWordInput.trim(), "CUSTOM")
                                customWordInput = ""
                            }
                        })
                    )

                    Button(
                        onClick = {
                            if (customWordInput.isNotBlank()) {
                                onInjectWord(customWordInput.trim(), "CUSTOM")
                                customWordInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(54.dp)
                    ) {
                        Text(text = "დეკოდირება", color = NeuralDeepPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // --- 6. RECENT DECODED WORDS CHRONOLOGY ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "დეკოდირებული სიტყვების ქრონოლოგია",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    wordDecoderState.recentWords.take(8).forEach { item ->
                        RecentWordRow(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun CandidateWordRow(
    rank: Int,
    candidate: DecodedWordCandidate,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (rank == 1) NeuralAccent else Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    color = if (rank == 1) NeuralDeepPurple else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = candidate.word,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "ფონემები: ${candidate.phonemes}",
                    color = NeuralTextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(
                    if (candidate.probabilityPct > 70) Color(0xFF00FFB2).copy(alpha = 0.18f)
                    else Color.White.copy(alpha = 0.1f)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "${candidate.probabilityPct}%",
                color = if (candidate.probabilityPct > 70) Color(0xFF00FFB2) else Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RecentWordRow(item: DecodedWordHistoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.timestamp,
                color = NeuralTextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = item.word,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF00FFB2).copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${item.confidencePct}%",
                    color = Color(0xFF00FFB2),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
