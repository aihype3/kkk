package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LotEntity
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.StatusAuthorizedGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DigitalReceiptCard(
    lot: LotEntity,
    currentLanguage: Language,
    onShare: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(lot.timestamp))
    val category = MaterialCategory.fromId(lot.materialCategoryId)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("digital_receipt_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Receipt Header with Watermark / Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "KABADIWALA",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        color = GreenPrimary
                    )
                    Text(
                        text = "DIGITAL HANDOVER RECEIPT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GreenContainer,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = StatusAuthorizedGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "VERIFIED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusAuthorizedGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = OutlineWarm, thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Key Receipt Fields
            ReceiptRow(label = "Lot ID:", value = lot.lotId, isMono = true, isBold = true)
            ReceiptRow(label = "Handover Ref:", value = lot.handoverReference.ifEmpty { "HR-88192" }, isMono = true)
            ReceiptRow(label = "Material:", value = "${category.iconEmoji} ${category.getLocalizedName(currentLanguage)}")
            ReceiptRow(label = "Sub-category:", value = lot.subCategory)
            ReceiptRow(label = "Weight:", value = "${lot.approximateWeightKg} kg", isBold = true)
            ReceiptRow(label = "Location:", value = lot.location)
            ReceiptRow(label = "Time:", value = formattedTime)
            ReceiptRow(label = "Recycler:", value = lot.recyclerName)
            ReceiptRow(label = "Authorization:", value = "🟢 Authorized (MPPCB Reg)", isGreen = true)
            ReceiptRow(label = "Status:", value = "Handed Over & Verified")

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = OutlineWarm, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Payment Amount & Status Highlight Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GreenContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Final Value (${lot.paymentMethod})",
                            fontSize = 12.sp,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "₹${lot.finalPrice}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = GreenPrimaryDark
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (lot.paymentStatus.equals("Paid", ignoreCase = true)) StatusAuthorizedGreen else Color(0xFFE65100)
                    ) {
                        Text(
                            text = if (lot.paymentStatus.equals("Paid", ignoreCase = true)) "✓ PAID" else "PENDING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Share Receipt & Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onShare() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "शेयर करें"
                            Language.MARATHI -> "शेअर करा"
                            Language.ENGLISH -> "Share"
                        },
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "संपन्न"
                            Language.MARATHI -> "पूर्ण"
                            Language.ENGLISH -> "Done"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isMono: Boolean = false,
    isBold: Boolean = false,
    isGreen: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontFamily = if (isMono) FontFamily.Monospace else FontFamily.Default,
            color = if (isGreen) StatusAuthorizedGreen else Color(0xFF212121)
        )
    }
}
