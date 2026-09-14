package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.MaterialCategory

@Composable
fun PriceAndTrendsScreen(
    currentLanguage: Language = Language.ENGLISH,
    category: MaterialCategory = MaterialCategory.PCB,
    initialWeightKg: Double = 5.0,
    ratePerKg: Int = 500,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var weightKg by remember { mutableDoubleStateOf(initialWeightKg) }
    val totalEstimatedPrice = (weightKg * ratePerKg).toInt()

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
                        .testTag("btn_back_price_trends")
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
                        Language.HINDI -> "मूल्य और बाज़ार रुझान"
                        Language.MARATHI -> "किंमत आणि बाजार कल"
                        Language.ENGLISH -> "Price & Trends"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Material Item Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_price_material"),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.img_scrap_pcb),
                            contentDescription = "Scrap Item",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = when (currentLanguage) {
                                    Language.HINDI -> category.nameHi
                                    Language.MARATHI -> category.nameMr
                                    Language.ENGLISH -> if (category == MaterialCategory.PCB) "PCBs" else category.nameEn
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.HINDI -> "मात्रा: ${weightKg.toInt()} किलो"
                                    Language.MARATHI -> "प्रमाण: ${weightKg.toInt()} किलो"
                                    Language.ENGLISH -> "Quantity: ${weightKg.toInt()} kg"
                                },
                                fontSize = 13.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    // Edit button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                // Toggle between 5kg and 10kg
                                weightKg = if (weightKg == 5.0) 10.0 else 5.0
                            }
                            .padding(4.dp)
                            .testTag("btn_edit_weight")
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "बदलें"
                                Language.MARATHI -> "बदला"
                                Language.ENGLISH -> "Edit"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Estimated Price Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_estimated_price"),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF9FAFB),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "अनुमानित मूल्य"
                            Language.MARATHI -> "अंदाजे मूल्य"
                            Language.ENGLISH -> "Estimated Price"
                        },
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹ %,d".format(totalEstimatedPrice),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "(₹ $ratePerKg/kg)",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Price Trends Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_price_trends"),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "मूल्य रुझान (स्थानीय बाज़ार)"
                            Language.MARATHI -> "किंमतीचा कल (स्थानिक बाजार)"
                            Language.ENGLISH -> "Price Trends (This Location)"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Trend Canvas Graph
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        val strokeColor = Color(0xFF2E7D32)
                        val w = size.width
                        val h = size.height

                        // Coordinates for upward trend: 30 days ago, 7 days ago, today
                        val p0 = Offset(0f, h * 0.85f)
                        val p1 = Offset(w * 0.35f, h * 0.60f)
                        val p2 = Offset(w * 0.65f, h * 0.40f)
                        val p3 = Offset(w, h * 0.10f)

                        val path = Path().apply {
                            moveTo(p0.x, p0.y)
                            cubicTo(
                                w * 0.15f, h * 0.75f,
                                w * 0.25f, h * 0.65f,
                                p1.x, p1.y
                            )
                            cubicTo(
                                w * 0.45f, h * 0.55f,
                                w * 0.55f, h * 0.45f,
                                p2.x, p2.y
                            )
                            cubicTo(
                                w * 0.80f, h * 0.30f,
                                w * 0.90f, h * 0.15f,
                                p3.x, p3.y
                            )
                        }

                        drawPath(
                            path = path,
                            color = strokeColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Points
                        drawCircle(color = strokeColor, radius = 5.dp.toPx(), center = p0)
                        drawCircle(color = strokeColor, radius = 5.dp.toPx(), center = p1)
                        drawCircle(color = strokeColor, radius = 5.dp.toPx(), center = p2)
                        drawCircle(color = strokeColor, radius = 6.dp.toPx(), center = p3)
                        drawCircle(color = Color.White, radius = 3.dp.toPx(), center = p3)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Legend Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val lblToday = when (currentLanguage) {
                            Language.HINDI -> "आज"
                            Language.MARATHI -> "आज"
                            Language.ENGLISH -> "Today"
                        }
                        val lbl7Days = when (currentLanguage) {
                            Language.HINDI -> "7 दिन"
                            Language.MARATHI -> "7 दिवस"
                            Language.ENGLISH -> "7 Days"
                        }
                        val lbl30Days = when (currentLanguage) {
                            Language.HINDI -> "30 दिन"
                            Language.MARATHI -> "30 दिवस"
                            Language.ENGLISH -> "30 Days"
                        }
                        TrendLegendItem(label = lblToday, value = "₹500/kg", isHighlight = true)
                        TrendLegendItem(label = lbl7Days, value = "₹480/kg", isHighlight = false)
                        TrendLegendItem(label = lbl30Days, value = "₹460/kg", isHighlight = false)
                    }
                }
            }
        }

        // Next Button
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 8.dp)
                .testTag("btn_price_next"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "आगे बढ़ें (Next)"
                    Language.MARATHI -> "पुढे जा (Next)"
                    Language.ENGLISH -> "Next"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun TrendLegendItem(
    label: String,
    value: String,
    isHighlight: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(if (isHighlight) Color(0xFF2E7D32) else Color(0xFF9CA3AF))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: $value",
            fontSize = 12.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) Color(0xFF2E7D32) else Color(0xFF4B5563)
        )
    }
}
