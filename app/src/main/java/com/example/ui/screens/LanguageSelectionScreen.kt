package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun LanguageSelectionScreen(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                // Logo icon
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.2f))
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_app_icon),
                        contentDescription = "KabadiWala Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Brand Name
                Text(
                    text = "KabadiWala",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF165B27)
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Subtitle
                Text(
                    text = "Select Language",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF374151)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Language Option 1: Hindi
                LanguagePillButton(
                    flagEmoji = "🇮🇳",
                    label = "हिंदी",
                    isSelected = currentLanguage == Language.HINDI,
                    onClick = {
                        onLanguageSelected(Language.HINDI)
                        onContinue()
                    },
                    testTag = "lang_hindi"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Language Option 2: Marathi
                LanguagePillButton(
                    flagEmoji = "🚩",
                    label = "मराठी",
                    isSelected = currentLanguage == Language.MARATHI,
                    onClick = {
                        onLanguageSelected(Language.MARATHI)
                        onContinue()
                    },
                    testTag = "lang_marathi"
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Language Option 3: English
                LanguagePillButton(
                    flagEmoji = "🇬🇧",
                    label = "English",
                    isSelected = currentLanguage == Language.ENGLISH,
                    onClick = {
                        onLanguageSelected(Language.ENGLISH)
                        onContinue()
                    },
                    testTag = "lang_english"
                )
            }

            // Bottom decorative landscape banner
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_eco_banner),
                    contentDescription = "Eco Background",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }
        }
    }
}

@Composable
private fun LanguagePillButton(
    flagEmoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(26.dp),
        color = if (isSelected) GreenContainer else Color.White,
        border = BorderStroke(
            1.2.dp,
            if (isSelected) GreenPrimary else Color(0xFFD1D5DB)
        ),
        shadowElevation = if (isSelected) 2.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = Color(0xFFF3F4F6)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = flagEmoji,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                fontSize = 17.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) GreenPrimary else Color(0xFF1F2937)
            )
        }
    }
}
