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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay

@Composable
fun VerifyOtpScreen(
    currentLanguage: Language = Language.ENGLISH,
    phoneNumber: String = "+91 98765 43210",
    initialOtp: String = "123456",
    onBack: () -> Unit,
    onVerify: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf(initialOtp) }
    var countdownSeconds by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) {
        while (countdownSeconds > 0) {
            delay(1000)
            countdownSeconds--
        }
    }

    val displayPhone = if (phoneNumber.startsWith("+")) phoneNumber else "+91 $phoneNumber"

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
                .testTag("btn_back_otp")
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
                Language.HINDI -> "OTP सत्यापित करें"
                Language.MARATHI -> "OTP सत्यापित करा"
                Language.ENGLISH -> "Verify OTP"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle with Phone Number
        Text(
            text = when (currentLanguage) {
                Language.HINDI -> "$displayPhone पर भेजा गया 6 अंकों का कोड दर्ज करें"
                Language.MARATHI -> "$displayPhone वर पाठवलेला 6 अंकी कोड प्रविष्ट करा"
                Language.ENGLISH -> "Enter the 6 digit code sent to\n$displayPhone"
            },
            fontSize = 14.sp,
            color = Color(0xFF4B5563),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // 6-digit OTP boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0 until 6) {
                val digit = if (i < otpCode.length) otpCode[i].toString() else ""
                val isFilled = digit.isNotEmpty()

                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("otp_box_$i"),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(
                        1.dp,
                        if (isFilled) Color(0xFF2E7D32) else Color(0xFFD1D5DB)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }
                }
            }
        }

        // Hidden / accessible field for editing OTP
        BasicTextField(
            value = otpCode,
            onValueChange = {
                if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                    otpCode = it
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Color.Transparent),
            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .testTag("input_otp_hidden")
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Verify button
        Button(
            onClick = { onVerify(otpCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_verify_otp"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "सत्यापित करें (Verify)"
                    Language.MARATHI -> "सत्यापित करा (Verify)"
                    Language.ENGLISH -> "Verify"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend OTP countdown
        val resendLabel = when (currentLanguage) {
            Language.HINDI -> "पुनः OTP भेजें"
            Language.MARATHI -> "पुन्हा OTP पाठवा"
            Language.ENGLISH -> "Resend OTP"
        }
        val resendText = if (countdownSeconds > 0) {
            "$resendLabel (0:${if (countdownSeconds < 10) "0$countdownSeconds" else countdownSeconds})"
        } else {
            resendLabel
        }

        Text(
            text = resendText,
            fontSize = 13.sp,
            color = if (countdownSeconds > 0) Color(0xFF6B7280) else Color(0xFF2E7D32),
            fontWeight = if (countdownSeconds > 0) FontWeight.Normal else FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = countdownSeconds == 0) {
                    countdownSeconds = 30
                    otpCode = "123456"
                }
        )
    }
}
