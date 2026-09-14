package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.data.model.Language

@Composable
fun LotConfirmedScreen(
    currentLanguage: Language = Language.ENGLISH,
    lotId: String = "KW202508150001",
    categoryName: String = "PCBs",
    weightKg: Double = 5.0,
    estimatedPrice: Int = 2500,
    recyclerName: String = "GreenCycle Recyclers",
    paymentMethod: String = "UPI",
    onBack: () -> Unit,
    onViewHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("btn_back_confirmed")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1F2937),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "लॉट कन्फर्म हो गया"
                        Language.MARATHI -> "लॉट निश्चित झाला"
                        Language.ENGLISH -> "Lot Confirmed"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Checkmark Success Hero
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(1.5.dp, Color(0xFF81C784))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "आपका लॉट सफलतापूर्वक दर्ज हो गया है!"
                        Language.MARATHI -> "तुमचा लॉट यशस्वीरित्या निश्चित झाला आहे!"
                        Language.ENGLISH -> "Your lot has been confirmed!"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Lot ID Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("box_lot_id"),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF9FAFB),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "लॉट आईडी (Lot ID)"
                            Language.MARATHI -> "लॉट आयडी (Lot ID)"
                            Language.ENGLISH -> "Lot ID"
                        },
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lotId,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_lot_details"),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    val lblCategory = when (currentLanguage) {
                        Language.HINDI -> "श्रेणी"
                        Language.MARATHI -> "प्रकार"
                        Language.ENGLISH -> "Category"
                    }
                    val lblQuantity = when (currentLanguage) {
                        Language.HINDI -> "मात्रा"
                        Language.MARATHI -> "प्रमाण"
                        Language.ENGLISH -> "Quantity"
                    }
                    val lblEstPrice = when (currentLanguage) {
                        Language.HINDI -> "अनुमानित मूल्य"
                        Language.MARATHI -> "अंदाजे मूल्य"
                        Language.ENGLISH -> "Estimated Price"
                    }
                    val lblRecycler = when (currentLanguage) {
                        Language.HINDI -> "रीसाइक्लर"
                        Language.MARATHI -> "रीसायकलर"
                        Language.ENGLISH -> "Recycler"
                    }
                    val lblPayment = when (currentLanguage) {
                        Language.HINDI -> "भुगतान का तरीका"
                        Language.MARATHI -> "पेमेंट पद्धत"
                        Language.ENGLISH -> "Payment Method"
                    }

                    ConfirmedDetailRow(label = lblCategory, value = categoryName)
                    Spacer(modifier = Modifier.height(12.dp))
                    ConfirmedDetailRow(label = lblQuantity, value = "${weightKg.toInt()} kg")
                    Spacer(modifier = Modifier.height(12.dp))
                    ConfirmedDetailRow(label = lblEstPrice, value = "₹ %,d".format(estimatedPrice))
                    Spacer(modifier = Modifier.height(12.dp))
                    ConfirmedDetailRow(label = lblRecycler, value = recyclerName)
                    Spacer(modifier = Modifier.height(12.dp))
                    ConfirmedDetailRow(label = lblPayment, value = paymentMethod)
                }
            }
        }

        // View in History Button
        Button(
            onClick = onViewHistory,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 8.dp)
                .testTag("btn_view_history"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "इतिहास में देखें (View in History)"
                    Language.MARATHI -> "इतिहास पहा (View in History)"
                    Language.ENGLISH -> "View in History"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ConfirmedDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827)
        )
    }
}
