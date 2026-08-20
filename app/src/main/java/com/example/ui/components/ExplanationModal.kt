package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun ExplanationModal(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(28.dp))
                .background(NeuralSurface)
                .border(1.dp, NeuralAccent.copy(alpha = 0.35f), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = AppIcons.Psychology,
                            contentDescription = "AI & OS Reality",
                            tint = NeuralAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "AI Intent Prediction & OS Reality",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = AppIcons.CloseIcon,
                            contentDescription = "Close",
                            tint = NeuralTextSecondary
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Georgian Section
                    ExplanationSectionCard(
                        title = "1. შესაძლებელია თუ არა ტექნიკურად? (Georgian)",
                        content = """
                            დიახ, ტექნიკურად Android OS-ზე შესაძლებელია ყველა ამ სიგნალის შეგროვება, თუ მომხმარებელი აპლიკაციას მისცემს შესაბამის უფლებებს (Permissions):
                            
                            • ეკრანზე შეხება (Touches): Accessibility Service-ის (ხელმისაწვდომობის სერვისის) მეშვეობით აპლიკაციას შეუძლია დააფიქსიროს შეხებები და ტექსტის ცვლილებები.
                            • მიკროფონით ხმა: RECORD_AUDIO და Foreground Service-ის მეშვეობით აპლიკაციას შეუძლია ფონურ რეჟიმში ჩაიწეროს/გაანალიზოს აუდიო.
                            • ეკრანისა და გამომავალი ხმის დაკვირვება: MediaProjection API (Screen Capture) და AudioPlaybackCapture API-ის საშუალებით.
                            • AI Intent Prediction: მულტიმოდალური ხელოვნური ინტელექტი (როგორიცაა Gemini / Neural Networks) ამ სიგნალების საფუძველზე პროგნოზირებს მომხმარებლის განზრახვას.
                        """.trimIndent()
                    )

                    ExplanationSectionCard(
                        title = "2. Android OS-ის უსაფრთხოება და შეზღუდვები",
                        content = """
                            Android ოპერაციულ სისტემას აქვს მკაცრი უსაფრთხოების მექანიზმები:
                            
                            1. ფარულად ეს ვერ მოხდება - OS აჩვენებს მწვანე ინდიკატორს (Green Mic/Camera Dot) და მუდმივ შეტყობინებას (Notification Bar).
                            2. პრივატულობა და სენსიტიური მონაცემები - ბანკის და პაროლების ველებზე შეხება იბლოკება უსაფრთხოების ფენით (FLAG_SECURE).
                            3. ტვინის ფიქრების პირდაპირი კითხვა შეუძლებელია - AI პროგნოზირებს ქცევით პატერნებს (Intent Inference) და არა პირდაპირ აზრებს.
                        """.trimIndent()
                    )

                    ExplanationSectionCard(
                        title = "3. How NeuroSync Works (Technical Pipeline)",
                        content = """
                            NeuroSync correlates 4 sensor data channels into unified multimodal embeddings:
                            
                            [Touch Cadence + Audio Spectrum + Foreground Screen Context] ➔ Multimodal AI Engine ➔ Real-Time Intent Prediction
                            
                            This creates proactive assistant actions (e.g. automatically reducing brightness during breaks or pre-loading drafting tools when typing).
                        """.trimIndent()
                    )
                }

                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "I Understand",
                        color = NeuralDeepPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ExplanationSectionCard(
    title: String,
    content: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NeuralDeepPurple.copy(alpha = 0.5f))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = title,
                color = NeuralAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = content,
                color = NeuralTextPrimary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}
