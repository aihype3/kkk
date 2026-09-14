package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.LotEntity
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.data.repository.DashboardStats
import com.example.ui.AppScreen
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.GreenPrimaryLight
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMedium
import com.example.util.AppStrings

data class HomeCategoryItem(
    val category: MaterialCategory,
    val titleEn: String,
    val titleHi: String,
    val titleMr: String,
    val imageRes: Int,
    val rateText: String
)

@Composable
fun CollectorHomeScreen(
    userFirstName: String = "",
    stats: DashboardStats,
    recentLots: List<LotEntity>,
    currentLanguage: Language,
    onNewLotClick: () -> Unit,
    onVoiceBotClick: () -> Unit = {},
    onSelectCategory: (MaterialCategory) -> Unit = {},
    onNavigate: (AppScreen) -> Unit,
    onLotClick: (LotEntity) -> Unit,
    onSpeakGreeting: () -> Unit
) {
    val categoryItems = listOf(
        HomeCategoryItem(MaterialCategory.CRT, "CRTs", "सीआरटी (CRTs)", "सीआरटी", R.drawable.img_cat_crt, "₹25/kg"),
        HomeCategoryItem(MaterialCategory.LCD, "LCD/LED panels", "एलसीडी/एलईडी पैनल", "एलसीडी/एलईडी", R.drawable.img_cat_lcd, "₹60/kg"),
        HomeCategoryItem(MaterialCategory.PCB, "PCBs", "पीसीबी (PCBs)", "पीसीबी", R.drawable.img_scrap_pcb, "₹350/kg"),
        HomeCategoryItem(MaterialCategory.CABLE, "Cables", "केबल्स और तार", "केबल्स आणि वायर", R.drawable.img_cat_cables, "₹180/kg"),
        HomeCategoryItem(MaterialCategory.BATTERY, "Batteries", "बैटरी", "बॅटरी", R.drawable.img_cat_batteries, "₹120/kg"),
        HomeCategoryItem(MaterialCategory.MOTOR, "Motors", "इलेक्ट्रिक मोटर", "इलेक्ट्रिक मोटर", R.drawable.img_cat_motor, "₹75/kg"),
        HomeCategoryItem(MaterialCategory.MAGNET, "Magnet-Bearing assemblies", "मैग्नेट-बेयरिंग असेंबली", "मॅग्नेट-बेअरिंग असेंब्ली", R.drawable.img_cat_magnet, "₹140/kg"),
        HomeCategoryItem(MaterialCategory.PLASTIC, "Mixed plastic and other E-waste", "मिक्स्ड प्लास्टिक व अन्य ई-कचरा", "मिक्स्ड प्लास्टिक", R.drawable.img_cat_plastic, "₹30/kg")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("collector_home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Exact Hero matching user requirements:
        // Namaste -> What do you want to sell? -> Sell Now -> Sahayak (Sell by Speaking) -> 2-in-a-row category boxes
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // "Namaste, [Name]!"
                val greeting = when (currentLanguage) {
                    Language.HINDI -> if (userFirstName.isNotBlank()) "नमस्ते, $userFirstName!" else "नमस्ते!"
                    Language.MARATHI -> if (userFirstName.isNotBlank()) "नमस्ते, $userFirstName!" else "नमस्ते!"
                    Language.ENGLISH -> if (userFirstName.isNotBlank()) "Namaste, $userFirstName!" else "Namaste!"
                }
                Text(
                    text = greeting,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // "What do you want to sell?"
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "आप क्या बेचना चाहते हैं?"
                        Language.MARATHI -> "तुम्ही काय विकू इच्छिता?"
                        Language.ENGLISH -> "What do you want to sell?"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Big Green Button: "Sell Now"
                Button(
                    onClick = { onNewLotClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_sell_now"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "अभी बेचें (Sell Now)"
                            Language.MARATHI -> "आता विका (Sell Now)"
                            Language.ENGLISH -> "Sell Now"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Secondary Button: "Sahayak (Sell by Speaking)"
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onVoiceBotClick() }
                        .testTag("btn_sahayak_voice"),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFF81C784)),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = Color(0xFF2E7D32)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Microphone",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Sahayak",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "(बोलकर बेचें)"
                                Language.MARATHI -> "(बोलून विका)"
                                Language.ENGLISH -> "(Sell by Speaking)"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section Header for Categories
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "सामग्री चुनें (Select to Sell)"
                            Language.MARATHI -> "वस्तू निवडा (Select to Sell)"
                            Language.ENGLISH -> "Select Material to Sell"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    Text(
                        text = "8 Categories",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2 in a row Material Category Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    categoryItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    MaterialGridCard(
                                        item = item,
                                        currentLanguage = currentLanguage,
                                        onClick = { onSelectCategory(item.category) }
                                    )
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Eco Graphic Banner with "Reduce • Reuse • Recycle"
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_home_eco_banner),
                            contentDescription = "Reduce Reuse Recycle Banner",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        )

                        Text(
                            text = "Reduce  •  Reuse  •  Recycle",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }

        // 3. Dashboard Statistics: Today's Earnings, Pending, Lots Sold
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Today's Earnings
                    StatCard(
                        modifier = Modifier.weight(1f),
                        emoji = "💰",
                        label = AppStrings.get("today_earnings", currentLanguage),
                        value = "₹${stats.todayEarnings}",
                        valueColor = StatusAuthorizedGreen,
                        bgColor = GreenContainer,
                        onClick = { onNavigate(AppScreen.EARNINGS) }
                    )

                    // Pending Payment
                    StatCard(
                        modifier = Modifier.weight(1f),
                        emoji = "⏳",
                        label = AppStrings.get("pending_payment", currentLanguage),
                        value = "₹${stats.pendingPayment}",
                        valueColor = SaffronAccent,
                        bgColor = AmberContainer,
                        onClick = { onNavigate(AppScreen.EARNINGS) }
                    )

                    // Lots Sold
                    StatCard(
                        modifier = Modifier.weight(0.9f),
                        emoji = "📦",
                        label = AppStrings.get("lots_sold", currentLanguage),
                        value = "${stats.lotsSold}",
                        valueColor = GreenPrimaryDark,
                        bgColor = Color(0xFFF1F6F1),
                        onClick = { onNavigate(AppScreen.HISTORY) }
                    )
                }
            }
        }

        // 4. Secondary Quick Actions as Large Accessible Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Aaj ke Rates
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "💰",
                        title = AppStrings.get("today_rates", currentLanguage),
                        subtitle = "PCB ₹350 • Cable ₹180",
                        badge = "📈 Live",
                        testTag = "quick_rates_btn",
                        onClick = { onNavigate(AppScreen.RATES_BOARD) }
                    )

                    // Recycler Dhoondein
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "♻️",
                        title = AppStrings.get("find_recyclers", currentLanguage),
                        subtitle = "4 Authorized near you",
                        badge = "🟢 Indore",
                        testTag = "quick_recyclers_btn",
                        onClick = { onNavigate(AppScreen.FIND_RECYCLERS) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Mere Transactions
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "📋",
                        title = AppStrings.get("my_transactions", currentLanguage),
                        subtitle = "History & Digital receipts",
                        badge = "${recentLots.size} lots",
                        testTag = "quick_history_btn",
                        onClick = { onNavigate(AppScreen.HISTORY) }
                    )

                    // Safety Tips
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        emoji = "🛡️",
                        title = AppStrings.get("safety_tips", currentLanguage),
                        subtitle = "Audio instructions & tips",
                        badge = "Safe Work",
                        testTag = "quick_safety_btn",
                        onClick = { onNavigate(AppScreen.SAFETY) }
                    )
                }
            }
        }

        // 5. Recent Activity / Transactions Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.get("recent_activity", currentLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimaryDark
                )

                Text(
                    text = "See All ➔",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GreenPrimary,
                    modifier = Modifier.clickable { onNavigate(AppScreen.HISTORY) }
                )
            }
        }

        // Recent items list
        items(recentLots.take(4)) { lot ->
            RecentTransactionItem(
                lot = lot,
                currentLanguage = currentLanguage,
                onClick = { onLotClick(lot) }
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    emoji: String,
    label: String,
    value: String,
    valueColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, valueColor.copy(alpha = 0.25f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = emoji, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = valueColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    badge: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = GreenContainer
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimaryDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun RecentTransactionItem(
    lot: LotEntity,
    currentLanguage: Language,
    onClick: () -> Unit
) {
    val category = MaterialCategory.fromId(lot.materialCategoryId)
    val isPaid = lot.paymentStatus.equals("Paid", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = GreenContainer,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = category.iconEmoji, fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = category.getLocalizedName(currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "${lot.approximateWeightKg} kg • ${lot.recyclerName.take(18)}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${lot.finalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPaid) StatusAuthorizedGreen else SaffronAccent
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isPaid) GreenContainer else AmberContainer
                ) {
                    Text(
                        text = if (isPaid) "✓ Paid" else "⏳ Pending",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPaid) StatusAuthorizedGreen else SaffronAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialGridCard(
    item: HomeCategoryItem,
    currentLanguage: Language,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("cat_card_${item.category.id}"),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            ) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = item.titleEn,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    color = Color(0xFF1B5E20),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = item.rateText,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> item.titleHi
                        Language.MARATHI -> item.titleMr
                        Language.ENGLISH -> item.titleEn
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    maxLines = 2,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Tap to Sell",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
