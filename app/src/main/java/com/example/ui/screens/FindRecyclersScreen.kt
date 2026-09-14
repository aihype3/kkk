package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.RecyclerEntity
import com.example.data.model.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.util.AppStrings

@Composable
fun FindRecyclersScreen(
    recyclers: List<RecyclerEntity>,
    currentLanguage: Language,
    onRecyclerSelected: (RecyclerEntity) -> Unit = {}
) {
    var isMapView by remember { mutableStateOf(false) }
    var filterAuthorizedOnly by remember { mutableStateOf(false) }
    var filterPickupOnly by remember { mutableStateOf(false) }
    var sortByHighestPrice by remember { mutableStateOf(false) }
    var selectedRecyclerForModal by remember { mutableStateOf<RecyclerEntity?>(null) }

    val context = LocalContext.current

    val filteredList = remember(recyclers, filterAuthorizedOnly, filterPickupOnly, sortByHighestPrice) {
        var list = recyclers
        if (filterAuthorizedOnly) {
            list = list.filter { it.isAuthorized }
        }
        if (filterPickupOnly) {
            list = list.filter { it.pickupAvailable }
        }
        if (sortByHighestPrice) {
            list = list.sortedByDescending { it.offeredRatePerKg }
        } else {
            list = list.sortedBy { it.distanceKm }
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("find_recyclers_screen")
    ) {
        // Top Bar with List / Map Toggle
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("authorized_recyclers", currentLanguage),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "Indore Regional Aggregators & Refiners",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    // View Toggle Button (List vs Map)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GreenContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { isMapView = !isMapView }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isMapView) Icons.Default.ViewList else Icons.Default.Map,
                                contentDescription = "Toggle View",
                                tint = GreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isMapView) "List View" else "Map View",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = filterAuthorizedOnly,
                            onClick = { filterAuthorizedOnly = !filterAuthorizedOnly },
                            label = { Text("🟢 Authorized Only", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenContainer,
                                selectedLabelColor = GreenPrimaryDark
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = filterPickupOnly,
                            onClick = { filterPickupOnly = !filterPickupOnly },
                            label = { Text("🚚 Pickup Available", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenContainer,
                                selectedLabelColor = GreenPrimaryDark
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = sortByHighestPrice,
                            onClick = { sortByHighestPrice = !sortByHighestPrice },
                            label = { Text("💰 Highest Price", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenContainer,
                                selectedLabelColor = GreenPrimaryDark
                            )
                        )
                    }
                }
            }
        }

        // Body: Map or List
        if (isMapView) {
            SimulatedRadarMapView(
                recyclers = filteredList,
                onRecyclerClick = { selectedRecyclerForModal = it }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
            ) {
                items(filteredList) { rec ->
                    RecyclerCard(
                        recycler = rec,
                        onClick = { selectedRecyclerForModal = rec }
                    )
                }
            }
        }
    }

    // Detail Modal Dialog (Screen 10 in spec)
    selectedRecyclerForModal?.let { recycler ->
        AlertDialog(
            onDismissRequest = { selectedRecyclerForModal = null },
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${recycler.contactPhone}")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Recycler")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedRecyclerForModal = null }) {
                    Text("Close", color = Color.Gray)
                }
            },
            title = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = recycler.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = GreenPrimaryDark
                        )
                    }
                    Text(
                        text = if (recycler.isAuthorized) "🟢 Authorized (${recycler.authorizationRegNo})" else "⚠️ Pending Verification",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (recycler.isAuthorized) StatusAuthorizedGreen else StatusAlertRed
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "📍 Address: ${recycler.address}",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "🕒 Hours: ${recycler.operatingHours}",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "📦 Materials Accepted: ${recycler.materialsAccepted}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "💰 Top Offer: ₹${recycler.offeredRatePerKg}/kg",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = SaffronAccent
                    )
                    Text(
                        text = "🚚 Pickup: ${if (recycler.pickupAvailable) "Available across Indore area" else "Scrap collector drop-off only"}",
                        fontSize = 13.sp,
                        color = if (recycler.pickupAvailable) GreenPrimary else Color.Gray
                    )
                    Text(
                        text = "⭐ Rating: ${recycler.rating}/5.0 (${recycler.reviewsCount} collector handovers)",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        )
    }
}

@Composable
private fun RecyclerCard(
    recycler: RecyclerEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
            .testTag("recycler_card_${recycler.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recycler.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (recycler.isAuthorized) GreenContainer else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = if (recycler.isAuthorized) "🟢 Authorized" else "⚠️ Pending",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (recycler.isAuthorized) StatusAuthorizedGreen else StatusAlertRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Distance",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${recycler.distanceKm} km away",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = SaffronAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${recycler.rating} (${recycler.reviewsCount})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Accepts: ${recycler.materialsAccepted}",
                fontSize = 13.sp,
                color = GreenPrimaryDark,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = OutlineWarm)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Pickup",
                        tint = if (recycler.pickupAvailable) GreenPrimary else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (recycler.pickupAvailable) "Pickup Available 🚚" else "Drop-off Only",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (recycler.pickupAvailable) GreenPrimary else Color.Gray
                    )
                }

                Text(
                    text = "Offer: ₹${recycler.offeredRatePerKg}/kg",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaffronAccent
                )
            }
        }
    }
}

@Composable
fun SimulatedRadarMapView(
    recyclers: List<RecyclerEntity>,
    onRecyclerClick: (RecyclerEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8ECE9))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "📍 Live Recycler Map — Indore Metropolitan Hub",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GreenPrimaryDark,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            // Interactive Radar Map Pins
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFD7E2D8))
                    .border(2.dp, OutlineWarm, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Center Collector Pin
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📍", fontSize = 12.sp)
                    }
                    Text("You (Indore)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Surrounding Recycler Pins
                recyclers.forEachIndexed { i, rec ->
                    val offsetX = when (i % 4) {
                        0 -> (-90).dp
                        1 -> 95.dp
                        2 -> (-60).dp
                        else -> 80.dp
                    }
                    val offsetY = when (i % 4) {
                        0 -> (-110).dp
                        1 -> (-70).dp
                        2 -> 110.dp
                        else -> 90.dp
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = if (offsetX > 0.dp) offsetX else 0.dp, end = if (offsetX < 0.dp) -offsetX else 0.dp)
                            .padding(top = if (offsetY > 0.dp) offsetY else 0.dp, bottom = if (offsetY < 0.dp) -offsetY else 0.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .border(1.dp, GreenPrimary, RoundedCornerShape(10.dp))
                            .clickable { onRecyclerClick(rec) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏭", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = rec.name.take(14),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimaryDark
                                )
                                Text(
                                    text = "${rec.distanceKm} km • ₹${rec.offeredRatePerKg}",
                                    fontSize = 10.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
