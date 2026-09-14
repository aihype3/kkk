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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.data.model.Language

data class RecyclerOption(
    val name: String,
    val rating: String,
    val reviewsCount: String,
    val offeredPrice: String
)

@Composable
fun SelectRecyclerLotScreen(
    currentLanguage: Language = Language.ENGLISH,
    selectedIndex: Int = 0,
    onBack: () -> Unit,
    onProceed: (Int, String) -> Unit = { _, _ -> }
) {
    var selectedOption by remember { mutableIntStateOf(selectedIndex) }
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }

    val options = listOf(
        RecyclerOption("GreenCycle Recyclers", "4.8", "120+ reviews", "₹ 2,500"),
        RecyclerOption("EcoReclaim Ltd.", "4.5", "85+ reviews", "₹ 2,450"),
        RecyclerOption("ReGen Resources", "4.3", "60+ reviews", "₹ 2,400")
    )

    val paymentMethods = listOf(
        Pair("UPI", "UPI (GPay / PhonePe / Paytm)"),
        Pair(
            "Cash",
            when (currentLanguage) {
                Language.HINDI -> "नकद (Cash on Pickup)"
                Language.MARATHI -> "रोख (Cash on Pickup)"
                Language.ENGLISH -> "Cash on Pickup"
            }
        ),
        Pair(
            "Bank",
            when (currentLanguage) {
                Language.HINDI -> "बैंक खाते में (Direct Bank Transfer)"
                Language.MARATHI -> "बँक खात्यात (Direct Bank Transfer)"
                Language.ENGLISH -> "Direct Bank Transfer"
            }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
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
                        .testTag("btn_back_recyclers")
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
                        Language.HINDI -> "रीसाइक्लर लॉट चुनें"
                        Language.MARATHI -> "रीसायकलर लॉट निवडा"
                        Language.ENGLISH -> "Select Recycler Lot"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Select Recycler
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "1. अधिकृत रीसाइक्लर चुनें"
                    Language.MARATHI -> "1. अधिकृत रीसायकलर निवडा"
                    Language.ENGLISH -> "1. Choose Authorized Recycler"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Options list
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == index

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedOption = index }
                        .testTag("recycler_option_$index"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFF0FDF4) else Color.White,
                    border = BorderStroke(
                        1.2.dp,
                        if (isSelected) Color(0xFF2E7D32) else Color(0xFFE5E7EB)
                    ),
                    shadowElevation = if (isSelected) 2.dp else 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedOption = index },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF2E7D32),
                                    unselectedColor = Color(0xFF9CA3AF)
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = option.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${option.rating} (${option.reviewsCount})",
                                        fontSize = 12.sp,
                                        color = Color(0xFF6B7280)
                                    )
                                }
                            }
                        }

                        Text(
                            text = option.offeredPrice,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Section 2: Select Payment Method
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "2. भुगतान का तरीका चुनें"
                    Language.MARATHI -> "2. पेमेंट पद्धत निवडा"
                    Language.ENGLISH -> "2. Select Payment Method"
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            paymentMethods.forEach { (key, label) ->
                val isPaymentSelected = selectedPaymentMethod == key
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { selectedPaymentMethod = key }
                        .testTag("payment_method_$key"),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isPaymentSelected) Color(0xFFF0FDF4) else Color(0xFFF9FAFB),
                    border = BorderStroke(
                        1.dp,
                        if (isPaymentSelected) Color(0xFF2E7D32) else Color(0xFFE5E7EB)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isPaymentSelected,
                            onClick = { selectedPaymentMethod = key },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF2E7D32),
                                unselectedColor = Color(0xFF9CA3AF)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = if (isPaymentSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isPaymentSelected) Color(0xFF1B5E20) else Color(0xFF374151)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Confirm Lot Button
        Button(
            onClick = { onProceed(selectedOption, selectedPaymentMethod) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(vertical = 4.dp)
                .testTag("btn_proceed_recycler"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "लॉट कन्फर्म करें (Confirm Lot)"
                    Language.MARATHI -> "लॉट निश्चित करा (Confirm Lot)"
                    Language.ENGLISH -> "Confirm Lot"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
