package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.theme.GreenPrimary

@Composable
fun UserDetailsScreen(
    currentLanguage: Language = Language.ENGLISH,
    initialFirstName: String = "",
    initialLastName: String = "",
    onBack: () -> Unit,
    onContinue: (String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(40.dp)
                .testTag("btn_back_details")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1F2937),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Title
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "आपकी जानकारी"
                Language.MARATHI -> "तुमची माहिती"
                Language.ENGLISH -> "Your Details"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "आगे बढ़ने के लिए कृपया अपना नाम दर्ज करें"
                Language.MARATHI -> "पुढे जाण्यासाठी कृपया आपले नाव टाका"
                Language.ENGLISH -> "Please enter your name to continue"
            },
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // First Name Label
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "पहला नाम (First Name)"
                Language.MARATHI -> "पहिले नाव (First Name)"
                Language.ENGLISH -> "First Name"
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // First Name Input
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFD1D5DB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (firstName.isEmpty()) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "उदा. रमेश"
                            Language.MARATHI -> "उदा. रमेश"
                            Language.ENGLISH -> "e.g. Ramesh"
                        },
                        fontSize = 15.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                BasicTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_first_name"),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF111827)
                    ),
                    cursorBrush = SolidColor(GreenPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Last Name Label
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "अंतिम नाम / उपनाम (Last Name)"
                Language.MARATHI -> "आडनाव (Last Name)"
                Language.ENGLISH -> "Last Name"
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Input
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFD1D5DB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (lastName.isEmpty()) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "उदा. शर्मा"
                            Language.MARATHI -> "उदा. शर्मा"
                            Language.ENGLISH -> "e.g. Sharma"
                        },
                        fontSize = 15.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                BasicTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_last_name"),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF111827)
                    ),
                    cursorBrush = SolidColor(GreenPrimary)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Continue Button
        Button(
            onClick = {
                val finalFirst = if (firstName.isBlank()) "Ramesh" else firstName.trim()
                val finalLast = if (lastName.isBlank()) "" else lastName.trim()
                onContinue(finalFirst, finalLast)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_details_continue"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "जारी रखें"
                    Language.MARATHI -> "पुढे जा"
                    Language.ENGLISH -> "Continue"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
