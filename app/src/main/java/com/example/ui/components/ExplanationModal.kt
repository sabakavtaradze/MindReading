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
                .fillMaxHeight(0.88f)
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
                            contentDescription = "AI განმარტება",
                            tint = NeuralAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "AI განზრახვის პროგნოზი & Ear-EEG",
                            color = Color.White,
                            fontSize = 15.sp,
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Galaxy Buds 2 Specific Section
                    ExplanationSectionCard(
                        title = "🎧 Galaxy Buds 2 & Ear-EEG (შეიძლება თუ არა ტვინის იმპულსების გაზომვა?)",
                        content = """
                            ტექნიკური და მეცნიერული ანალიზი:
                            
                            1. კომერციული Galaxy Buds 2 აპარატურა:
                            • Galaxy Buds 2 აღჭურვილია: 3 მიკროფონით, VPU (Voice Pickup Unit - ძვლოვანი გამტარობის სენსორი), აქსელერომეტრით და გიროსკოპით.
                            • აქვს თუ არა პირდაპირი EEG ელექტროდები? არა. სტანდარტულ Galaxy Buds 2-ს არ გააჩნია გამტარი მშრალი ელექტროდები (Conductive Dry Electrodes), რომლებიც საჭიროა მიკროვოლტების (1–50 μV) დონის ელექტროენცეფალოგრამის (EEG) პირდაპირ გასაზომად.
                            
                            2. რას აკეთებენ კვლევითი კომპანიები (Ear-EEG)?
                            • კომპანიები, როგორიცაა IDUN Technologies, NextMind და Ear-EEG ლაბორატორიები, სტანდარტულ სილიკონის ბალიშებს ანაცვლებენ ელექტრო-გამტარი პოლიმერით/ვერცხლის საფარით, რომელიც ყურის არხის კედელს ეხება და კითხულობს ტემფორალური წილის ტვინის ტალღებს.
                            
                            3. როგორ გვეხმარება Galaxy Buds 2 აზრების პროგნოზირებაში NeuroSync-ში?
                            • VPU ძვლოვანი გამტარობა: აფიქსირებს ყბის მიკრო-მოძრაობებსა და სუბვოკალურ (შინაგანი მეტყველების) ვიბრაციებს მაშინაც კი, როცა ხმას არ იღებთ!
                            • IMU თავის კინემატიკა: თავის მიკრო-დახრა, დათანხმების რიტმი და ყურადღების ვექტორი.
                            • ყურის არხის აკუსტიკური წნევა: სუნთქვის სიხშირე და გადაყლაპვის რიტმი.
                        """.trimIndent()
                    )

                    // How to improve thought predictions
                    ExplanationSectionCard(
                        title = "💡 რით გავაუმჯობესოთ აპლიკაცია, რომ უკეთ ამოვიცნოთ ადამიანის აზრები?",
                        content = """
                            აზრებისა და განზრახვის მაქსიმალური სიზუსტით გამოსაცნობად ყველაზე ეფექტური მეთოდებია:
                            
                            1. მულტიმოდალური სენსორული შერწყმა (Multimodal Sensor Fusion):
                            • შეხების მიკრო-დაყოვნება (Inter-tap Latency & Hesitation) - სანამ ადამიანი გადაწყვეტილებას მიიღებს, შეხების ინტერვალი იცვლება 180ms-დან 600ms-მდე.
                            • ნეირომუსკულარული ტრემორი (Micro-Tremor) - ტელეფონის აქსელერომეტრის მიკრო-რყევა პირდაპირ ასახავს კოგნიტურ დაძაბულობას.
                            • თვალის გუგის დილატაცია & rPPG - წინა კამერით სახის მიკრო-ნათებისა და პულსის (BPM) ტრეკინგი.
                            
                            2. სუბვოკალური ენის მოდელი (Inner Speech):
                            • ადამიანთა 90% ფიქრისას "შინაგანი ხმით" აყალიბებს ფრაზებს. მიკროფონის ულტრა-დაბალი სიხშირეებისა და ყურსასმენის VPU-ს სინთეზი იძლევა ნააზრევი სიტყვების გამოცნობის შესაძლებლობას.
                            
                            3. ასოციაციური სემანტიკური გრაფი:
                            • აპლიკაცია აკვირდება აქტიურ კონტექსტს (რა აპლიკაციაში ხართ, დღის რა მონაკვეთია) და ტვინის ტალღებს (Alpha, Beta, Theta, Gamma), რათა წინასწარ განჭვრიტოს შემდეგი ლოგიკური ნაბიჯი.
                            
                            4. უწყვეტი ფონური მონიტორინგი:
                            • მუდმივი Background Service (WakeLock) უზრუნველყოფს, რომ ტელეფონის ჩაკეტვის ან სხვა აპზე გადასვლის დროსაც პროგნოზი არ შეწყდეს.
                        """.trimIndent()
                    )

                    ExplanationSectionCard(
                        title = "🔒 უსაფრთხოება და ნებართვების მუდმივი შენახვა",
                        content = """
                            • მუდმივი მეხსიერება: თქვენ მიერ გაცემული ნებართვები ინახება SharedPreferences-ში და აპლიკაცია მათ ავტომატურად ინარჩუნებს.
                            • ერთიანი გააქტიურება: ერთი ღილაკით აქტიურდება ყველა სენსორი, BCI სიმულაცია, აუდიო და ფონური სერვისი.
                            • 100% ლოკალური დაცვა: მონაცემები ინახება თქვენს მოწყობილობაში და დაცულია.
                        """.trimIndent()
                    )
                }

                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeuralAccent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "გავიგე, მატრიცაზე დაბრუნება",
                        color = NeuralDeepPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NeuralCardPurple)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = NeuralAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content,
            color = NeuralTextPrimary,
            fontSize = 11.sp,
            lineHeight = 17.sp
        )
    }
}
