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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusAuthorizedGreen

@Composable
fun AdminDashboardScreen(
    onBackToCollector: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Impact & Unit Economics",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "Platform Level Dataset & Financial Model",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onBackToCollector,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("Exit Admin", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dataset Overview 4 KPI Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatTile(modifier = Modifier.weight(1f), label = "Materials", value = "1,240")
                AdminStatTile(modifier = Modifier.weight(1f), label = "Transactions", value = "3,845")
                AdminStatTile(modifier = Modifier.weight(1f), label = "Recyclers", value = "86")
                AdminStatTile(modifier = Modifier.weight(1f), label = "Completed", value = "2,910")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Unit Economics Section: Traditional Route vs KabadiWala Route
            Text(
                text = "Unit Economics Comparison",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Text(
                text = "Demonstrating how formalization doubles the scrap collector's margin",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Route 1: Traditional Informal Scrap Route
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F7)),
                border = BorderStroke(1.dp, StatusAlertRed.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "❌ Current Traditional Route (3-Tier Leakage)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = StatusAlertRed
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RouteStepRow("Collector buys from household / shop", "₹1,500")
                    RouteStepRow("Sells to local middleman aggregator", "₹1,800")
                    RouteStepRow("Middleman markup sells to recycler", "₹2,600")

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = StatusAlertRed.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Collector Net Profit:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("₹300 (Low 20% margin)", fontWeight = FontWeight.Black, fontSize = 15.sp, color = StatusAlertRed)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Route 2: KabadiWala Direct Route
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GreenContainer),
                border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓ KabadiWala Direct Formal Route",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = GreenPrimaryDark
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GreenPrimary
                        ) {
                            Text(
                                text = "+160% PROFIT BOOST",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RouteStepRow("Collector buys from household / shop", "₹1,500")
                    RouteStepRow("Direct verified sale to Recycler via App", "₹2,400")
                    RouteStepRow("Platform service fee (5%)", "₹120")

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = GreenPrimary.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Collector Net Profit:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("₹780 / lot", fontWeight = FontWeight.Black, fontSize = 18.sp, color = GreenPrimaryDark)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🚀 Extra profit per lot for collector: +₹480 (+160% Increase)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusAuthorizedGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Platform Revenue Streams Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Platform Monetization & Revenue Streams",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GreenPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    RevenueItem(
                        title = "1. Recycler Transaction Fee (3–5%)",
                        desc = "Charged on each successful formal scrap settlement."
                    )
                    RevenueItem(
                        title = "2. Recycler Premium Subscription",
                        desc = "Priority scrap routing, direct bulk dispatch, and dispatch analytics."
                    )
                    RevenueItem(
                        title = "3. EPR Credit Partnerships",
                        desc = "Extended Producer Responsibility credits sold to authorized electronic brands (Samsung, Dell, HP) for verified traceability tokens."
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminStatTile(
    modifier: Modifier,
    label: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = GreenPrimaryDark
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun RouteStepRow(label: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.DarkGray)
        Text(text = amount, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RevenueItem(title: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenPrimaryDark)
        Text(text = desc, fontSize = 12.sp, color = Color.DarkGray)
    }
}
