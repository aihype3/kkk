package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.repository.DashboardStats
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAuthorizedGreen

@Composable
fun EarningsScreen(
    stats: DashboardStats,
    currentLanguage: Language
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("earnings_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "कमाई एवं लेन-देन हिसाब"
                    Language.MARATHI -> "कमाई आणि व्यवहार हिशोब"
                    Language.ENGLISH -> "Earnings & Analytics"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = GreenPrimaryDark
            )
            Text(
                text = "Track all formal e-waste payouts & settlement history",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Total Earnings Hero Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Text(
                        text = "Total Lifetime Earnings (कुल कमाई)",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "₹${stats.totalEarnings}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "This Month",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "₹${stats.thisMonthEarnings}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Column {
                            Text(
                                text = "Pending Settlements",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "₹${stats.pendingPayment}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFE082)
                            )
                        }

                        Column {
                            Text(
                                text = "Lots Sold",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "${stats.lotsSold} lots",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Monthly Earnings Bar Chart
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Monthly E-Waste Revenue (मासिक आय)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GreenPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        EarningsMonthBar("May", "₹4.2k", 45)
                        EarningsMonthBar("June", "₹5.8k", 60)
                        EarningsMonthBar("July", "₹6.4k", 75)
                        EarningsMonthBar("Aug", "₹7.9k", 90)
                        EarningsMonthBar("Sept", "₹8.5k", 100, isCurrent = true)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Material Breakdown
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Earnings by Scrap Category",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GreenPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    MaterialProgressRow("🔌 PCB & Circuit Boards", "₹12,400 (50%)", 0.50f, GreenPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    MaterialProgressRow("🔗 Copper Cables", "₹6,200 (25%)", 0.25f, SaffronAccent)
                    Spacer(modifier = Modifier.height(10.dp))
                    MaterialProgressRow("🔋 Lead & Li-ion Batteries", "₹3,850 (15%)", 0.15f, Color(0xFF0288D1))
                    Spacer(modifier = Modifier.height(10.dp))
                    MaterialProgressRow("⚙️ Motors & Displays", "₹2,400 (10%)", 0.10f, Color(0xFF7B1FA2))
                }
            }
        }
    }
}

@Composable
private fun EarningsMonthBar(
    month: String,
    amount: String,
    heightDp: Int,
    isCurrent: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = amount,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isCurrent) GreenPrimary else Color.Gray
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(if (isCurrent) GreenPrimary else Color(0xFFC8E6C9))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = month,
            fontSize = 12.sp,
            color = if (isCurrent) GreenPrimaryDark else Color.DarkGray,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun MaterialProgressRow(
    label: String,
    amount: String,
    progress: Float,
    barColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = amount, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = barColor,
            trackColor = Color(0xFFEEEEEE)
        )
    }
}
