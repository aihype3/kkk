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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.local.LotEntity
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.ui.components.DigitalReceiptCard
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.util.AppStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsHistoryScreen(
    lots: List<LotEntity>,
    currentLanguage: Language,
    selectedLot: LotEntity?,
    onSelectLot: (LotEntity?) -> Unit
) {
    var filterTab by remember { mutableStateOf("ALL") } // "ALL", "PAID", "PENDING"

    val filteredLots = remember(lots, filterTab) {
        when (filterTab) {
            "PAID" -> lots.filter { it.paymentStatus.equals("Paid", ignoreCase = true) }
            "PENDING" -> lots.filter { !it.paymentStatus.equals("Paid", ignoreCase = true) }
            else -> lots
        }
    }

    val totalPaid = remember(lots) {
        lots.filter { it.paymentStatus.equals("Paid", ignoreCase = true) }.sumOf { it.finalPrice }
    }
    val totalPending = remember(lots) {
        lots.filter { !it.paymentStatus.equals("Paid", ignoreCase = true) }.sumOf { it.finalPrice }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("transactions_history_screen")
    ) {
        // Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = AppStrings.get("my_transactions", currentLanguage),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimaryDark
                )
                Text(
                    text = "Summary: ₹$totalPaid Paid • ₹$totalPending Pending",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Tabs: All, Paid, Pending
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = filterTab == "ALL",
                        onClick = { filterTab = "ALL" },
                        label = { Text("All (${lots.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenContainer,
                            selectedLabelColor = GreenPrimaryDark
                        )
                    )
                    FilterChip(
                        selected = filterTab == "PAID",
                        onClick = { filterTab = "PAID" },
                        label = { Text("✓ Paid (${lots.count { it.paymentStatus.equals("Paid", ignoreCase = true) }})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenContainer,
                            selectedLabelColor = StatusAuthorizedGreen
                        )
                    )
                    FilterChip(
                        selected = filterTab == "PENDING",
                        onClick = { filterTab = "PENDING" },
                        label = { Text("⏳ Pending (${lots.count { !it.paymentStatus.equals("Paid", ignoreCase = true) }})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmberContainer,
                            selectedLabelColor = SaffronAccent
                        )
                    )
                }
            }
        }

        // Transactions List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
        ) {
            if (filteredLots.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transactions found in this filter.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredLots) { lot ->
                    TransactionItemRow(
                        lot = lot,
                        currentLanguage = currentLanguage,
                        onClick = { onSelectLot(lot) }
                    )
                }
            }
        }
    }

    // Modal Sheet: Transaction Detail & Digital Receipt Card (Screen 16 in spec)
    selectedLot?.let { lot ->
        AlertDialog(
            onDismissRequest = { onSelectLot(null) },
            confirmButton = {},
            dismissButton = {},
            text = {
                DigitalReceiptCard(
                    lot = lot,
                    currentLanguage = currentLanguage,
                    onDismiss = { onSelectLot(null) }
                )
            }
        )
    }
}

@Composable
private fun TransactionItemRow(
    lot: LotEntity,
    currentLanguage: Language,
    onClick: () -> Unit
) {
    val category = MaterialCategory.fromId(lot.materialCategoryId)
    val isPaid = lot.paymentStatus.equals("Paid", ignoreCase = true)
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(lot.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = GreenContainer,
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = category.iconEmoji, fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${category.getLocalizedName(currentLanguage)} • ${lot.approximateWeightKg} kg",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "${lot.recyclerName.take(18)} • ${lot.paymentMethod}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$dateString • ${lot.lotId}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${lot.finalPrice}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isPaid) StatusAuthorizedGreen else SaffronAccent
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isPaid) GreenContainer else AmberContainer
                ) {
                    Text(
                        text = if (isPaid) "✓ Paid" else "⏳ Pending",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPaid) StatusAuthorizedGreen else SaffronAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
