package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.SaffronAccent

data class OnboardingItem(
    val imageRes: Int,
    val emoji: String,
    val titleHi: String,
    val titleMr: String,
    val titleEn: String,
    val descHi: String,
    val descMr: String,
    val descEn: String
)

@Composable
fun OnboardingScreen(
    currentLanguage: Language,
    onFinish: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    val pages = listOf(
        OnboardingItem(
            imageRes = R.drawable.img_hero_collector,
            emoji = "📷",
            titleHi = "अपना ई-कचरा बेचें",
            titleMr = "तुमचा ई-कचरा विका",
            titleEn = "Sell Your E-Waste",
            descHi = "अपने इलेक्ट्रॉनिक स्क्रैप की फोटो लें और तुरंत उसका सही बाज़ार मूल्य जानें।",
            descMr = "तुमच्या इलेक्ट्रॉनिक स्क्रॅपचा फोटो घ्या आणि अचूक बाजारभाव जाणून घ्या.",
            descEn = "Take a photo of your electronic scrap and know its fair market value."
        ),
        OnboardingItem(
            imageRes = R.drawable.img_safe_recycling,
            emoji = "♻️",
            titleHi = "सही रिसाइक्लर ढूंढें",
            titleMr = "योग्य रिसायकलर शोधा",
            titleEn = "Find the Right Recycler",
            descHi = "अपने पास के अधिकृत (Authorized) रिसाइक्लर्स के रेट, दूरी व पिकअप सुविधा की तुलना करें।",
            descMr = "जवळील अधिकृत रिसायकलरचे दर, अंतर आणि पिकअप सुविधा तपासा.",
            descEn = "Compare nearby government authorized recyclers by price, distance & pickup."
        ),
        OnboardingItem(
            imageRes = R.drawable.img_hero_collector,
            emoji = "💰",
            titleHi = "कमाई का हिसाब रखें",
            titleMr = "कमाईचा हिशोब ठेवा",
            titleEn = "Track Your Earnings",
            descHi = "हर लेन-देन, नकद व डिजिटल भुगतान और डिजिटल रसीद एक ही जगह सुरक्षित देखें।",
            descMr = "प्रत्येक व्यवहार, रोख किंवा डिजिटल पेमेंट आणि पावती एकाच ठिकाणी पहा.",
            descEn = "View every transaction, cash or digital payment, and digital receipt in one place."
        )
    )

    val item = pages[currentPage]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Skip Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (currentPage < 2) {
                TextButton(onClick = onFinish) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "छोड़ें (Skip)"
                            Language.MARATHI -> "वगळा"
                            Language.ENGLISH -> "Skip"
                        },
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }

        // Center Content with Hero Image
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.titleEn,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "${item.emoji} " + when (currentLanguage) {
                    Language.HINDI -> item.titleHi
                    Language.MARATHI -> item.titleMr
                    Language.ENGLISH -> item.titleEn
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> item.descHi
                    Language.MARATHI -> item.descMr
                    Language.ENGLISH -> item.descEn
                },
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Page Indicator Dots
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..2) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (i == currentPage) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (i == currentPage) GreenPrimary else Color.LightGray)
                    )
                }
            }
        }

        // Bottom CTA
        Button(
            onClick = {
                if (currentPage < 2) {
                    currentPage++
                } else {
                    onFinish()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("onboarding_cta_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
        ) {
            Text(
                text = if (currentPage < 2) {
                    when (currentLanguage) {
                        Language.HINDI -> "आगे बढ़ें ➔"
                        Language.MARATHI -> "पुढे जा ➔"
                        Language.ENGLISH -> "Next ➔"
                    }
                } else {
                    when (currentLanguage) {
                        Language.HINDI -> "शुरू करें (Shuru Karein) 🚀"
                        Language.MARATHI -> "सुरू करा 🚀"
                        Language.ENGLISH -> "Get Started 🚀"
                    }
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
