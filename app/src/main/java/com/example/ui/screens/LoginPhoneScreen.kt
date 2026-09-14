package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.theme.GreenPrimary

@Composable
fun LoginPhoneScreen(
    currentLanguage: Language = Language.ENGLISH,
    initialPhoneNumber: String = "98765 43210",
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf(initialPhoneNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("btn_back_login")
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
                    Language.HINDI -> "लॉगिन / साइन अप"
                    Language.MARATHI -> "लॉगिन / साइन अप"
                    Language.ENGLISH -> "Login / Signup"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "आगे बढ़ने के लिए अपना फ़ोन नंबर दर्ज करें"
                    Language.MARATHI -> "पुढे जाण्यासाठी आपला फोन नंबर प्रविष्ट करा"
                    Language.ENGLISH -> "Enter your phone number to continue"
                },
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Phone number input row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country code dropdown pill
                Surface(
                    modifier = Modifier
                        .height(52.dp)
                        .width(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFD1D5DB))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "+91",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Country Code",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Phone number text field
                Surface(
                    modifier = Modifier
                        .weight(1f)
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
                        if (phoneNumber.isEmpty()) {
                            Text(
                                text = when (currentLanguage) {
                                    Language.HINDI -> "फ़ोन नंबर"
                                    Language.MARATHI -> "फोन नंबर"
                                    Language.ENGLISH -> "Phone Number"
                                },
                                fontSize = 15.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                        BasicTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_phone_number"),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF111827)
                            ),
                            cursorBrush = SolidColor(GreenPrimary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Continue button
            Button(
                onClick = { onContinue(phoneNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_phone_continue"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "आगे बढ़ें (Continue)"
                        Language.MARATHI -> "पुढे जा (Continue)"
                        Language.ENGLISH -> "Continue"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Footer terms
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "आगे बढ़कर, आप हमारी सेवा की शर्तों और गोपनीयता नीति से सहमत होते हैं"
                Language.MARATHI -> "पुढे जाऊन, आपण आमच्या अटी व गोपनीयता धोरणाशी सहमत आहात"
                Language.ENGLISH -> "By continuing, you agree to our Terms & Privacy Policy"
            },
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
    }
}
