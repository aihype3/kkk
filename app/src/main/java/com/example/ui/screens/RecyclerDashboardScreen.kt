package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun RecyclerDashboardScreen(
    collectorName: String = "",
    incomingLots: List<LotEntity>,
    currentLanguage: Language,
    onConfirmHandover: (String, Double, Int, String, String) -> Unit,
    onBackToCollector: () -> Unit
) {
    var lotForHandoverDialog by remember { mutableStateOf<LotEntity?>(null) }
    var actionMessage by remember { mutableStateOf<String?>(null) }

    val activeLotsCount = incomingLots.size
    val pendingPickupsCount = incomingLots.count { it.pickupRequired }
    val totalPurchasesToday = incomingLots.filter { it.paymentStatus.equals("Paid", ignoreCase = true) }.sumOf { it.finalPrice }
    val totalWeightReceived = incomingLots.sumOf { it.approximateWeightKg }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("recycler_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            // Recycler Dashboard Header
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F3820)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "XYZ E-Waste Recycling",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "MPPCB/E-WASTE/2024/091 • Authorized",
                                fontSize = 11.sp,
                                color = StatusAuthorizedGreen
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.clickable { onBackToCollector() }
                        ) {
                            Text(
                                text = "Switch to Kabadi",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(14.dp))

                    // 5 KPI Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RecyclerKpiItem("Active Lots", "$activeLotsCount")
                        RecyclerKpiItem("Pending Pickups", "$pendingPickupsCount")
                        RecyclerKpiItem("Weight Received", "${totalWeightReceived.toInt()} kg")
                        RecyclerKpiItem("Purchases", "₹$totalPurchasesToday")
                    }
                }
            }

            if (actionMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = GreenContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✓ $actionMessage",
                        color = GreenPrimaryDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Incoming Scrap Lot Requests",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Text(
                text = "Collectors waiting for acceptance, offer pricing, or physical handover",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        items(incomingLots) { lot ->
            val cat = MaterialCategory.fromId(lot.materialCategoryId)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = lot.lotId,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = GreenPrimaryDark
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (lot.status == "CONFIRMED") GreenContainer else AmberContainer
                        ) {
                            Text(
                                text = lot.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (lot.status == "CONFIRMED") GreenPrimaryDark else SaffronAccent,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = GreenContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = cat.iconEmoji, fontSize = 20.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "${cat.nameEn} • ${lot.subCategory}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Approx Weight: ${lot.approximateWeightKg} kg • Condition: ${lot.condition}",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                            val dispCollector = if (collectorName.isNotBlank()) collectorName else "Collector"
                            Text(
                                text = "Collector: $dispCollector (Indore) • Pickup: ${if (lot.pickupRequired) "Required 🚚" else "Direct Drop-off"}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Expected Rate: ₹${lot.quotedRatePerKg}/kg",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SaffronAccent
                        )
                        Text(
                            text = "Estimated: ₹${lot.estimatedMinPrice}–₹${lot.estimatedMaxPrice}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimaryDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = OutlineWarm)
                    Spacer(modifier = Modifier.height(12.dp))

                    // 3 Actions: Accept, Make Offer, Physical Handover
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { actionMessage = "Offer of ₹${lot.quotedRatePerKg + 10}/kg sent to collector." },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text("Make Offer", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { lotForHandoverDialog = lot },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            modifier = Modifier.weight(1.3f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = "Weigh",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Confirm Handover", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Handover Confirmation Modal (Spec: Actual weight, Final price, Payment mode, Receiving photo, Confirm)
    lotForHandoverDialog?.let { lot ->
        var actualWeightInput by remember { mutableStateOf("${lot.approximateWeightKg}") }
        var finalPriceInput by remember { mutableStateOf("${lot.finalPrice.takeIf { it > 0 } ?: (lot.quotedRatePerKg * lot.approximateWeightKg).toInt()}") }
        var paymentMethod by remember { mutableStateOf("Cash") }
        var paymentStatus by remember { mutableStateOf("Paid") }
        var photoAttached by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { lotForHandoverDialog = null },
            confirmButton = {
                Button(
                    onClick = {
                        val weight = actualWeightInput.toDoubleOrNull() ?: lot.approximateWeightKg
                        val price = finalPriceInput.toIntOrNull() ?: lot.finalPrice
                        onConfirmHandover(lot.lotId, weight, price, paymentMethod, paymentStatus)
                        actionMessage = "Handover confirmed for ${lot.lotId}. Traceability record created!"
                        lotForHandoverDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verify",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Complete Handover")
                }
            },
            dismissButton = {
                TextButton(onClick = { lotForHandoverDialog = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            title = {
                Text(
                    text = "Confirm Handover & Inspection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = GreenPrimaryDark
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Lot ID: ${lot.lotId} • Material: ${lot.materialCategoryId.uppercase()}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    OutlinedTextField(
                        value = actualWeightInput,
                        onValueChange = { actualWeightInput = it },
                        label = { Text("Actual Weigh-Bridge Weight (kg)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = finalPriceInput,
                        onValueChange = { finalPriceInput = it },
                        label = { Text("Final Settled Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Payment Method:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Cash", "Digital").forEach { method ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (paymentMethod == method) GreenContainer else Color(0xFFF0F0F0),
                                border = BorderStroke(1.dp, if (paymentMethod == method) GreenPrimary else Color.Transparent),
                                modifier = Modifier
                                    .clickable { paymentMethod = method }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(method, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Photo",
                            tint = StatusAuthorizedGreen
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Weigh-bridge slip & scrap photo captured ✓", fontSize = 11.sp, color = GreenPrimaryDark)
                    }
                }
            }
        )
    }
}

@Composable
private fun RecyclerKpiItem(label: String, value: String) {
    Column {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.75f)
        )
    }
}
