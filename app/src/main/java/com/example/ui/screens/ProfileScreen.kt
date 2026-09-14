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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppRole
import com.example.data.model.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.util.AppStrings

@Composable
fun ProfileScreen(
    userFirstName: String = "",
    userLastName: String = "",
    currentLanguage: Language,
    currentRole: AppRole,
    isOffline: Boolean,
    isSyncing: Boolean,
    onLanguageChange: (Language) -> Unit,
    onRoleChange: (AppRole) -> Unit,
    onToggleOffline: () -> Unit,
    onSyncNow: () -> Unit
) {
    var voiceAssistanceEnabled by remember { mutableStateOf(true) }

    val displayName = if (userFirstName.isNotBlank()) {
        "$userFirstName $userLastName".trim()
    } else {
        when (currentLanguage) {
            Language.HINDI -> "रमेश शर्मा"
            Language.MARATHI -> "रमेश शर्मा"
            Language.ENGLISH -> "Ramesh Sharma"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            // Collector Profile Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(GreenContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👷", fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = displayName,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "Collector ID: KW-IND-8821",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "📍 Indore, Madhya Pradesh",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = GreenContainer
                        ) {
                            Text(
                                text = "✓ Verified Scrap Collector",
                                color = StatusAuthorizedGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Section Header
            Text(
                text = "Preferences & Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Language Selector Row
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = GreenPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "App Language",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Language.entries.forEach { lang ->
                            val isSelected = lang == currentLanguage
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) GreenPrimary else Color(0xFFF5F5F5),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onLanguageChange(lang) }
                            ) {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Voice Assistance Toggle
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Voice",
                            tint = SaffronAccent
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Voice Assistance (ऑडियो सहायता)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Speaks out rates, prices & safety tips",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Switch(
                        checked = voiceAssistanceEnabled,
                        onCheckedChange = { voiceAssistanceEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = GreenPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Offline & Cloud Sync Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isOffline) Icons.Default.Sync else Icons.Default.CloudDone,
                                contentDescription = "Sync",
                                tint = if (isOffline) AmberAccent else StatusAuthorizedGreen
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Offline Sync Status",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isOffline) "Offline mode active (Local Room DB)" else "All local records synchronized with cloud",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Button(
                            onClick = onSyncNow,
                            enabled = !isSyncing,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isSyncing) "Syncing..." else "Sync Now",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onToggleOffline,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isOffline) "Switch to Online Mode" else "Simulate Offline Mode (Low Connectivity)",
                            fontSize = 12.sp,
                            color = GreenPrimaryDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Role Switcher Cards (Collector, Recycler, Admin)
            Text(
                text = "Switch User Experience Role",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppRole.entries.forEach { role ->
                    val isCurrent = role == currentRole
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCurrent) GreenContainer else Color.White,
                        border = BorderStroke(1.dp, if (isCurrent) GreenPrimary else OutlineWarm),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRoleChange(role) }
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (role) {
                                    AppRole.COLLECTOR -> "♻️"
                                    AppRole.RECYCLER -> "🏭"
                                    AppRole.ADMIN -> "📊"
                                },
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = role.displayName.split(" ").first(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) GreenPrimaryDark else Color.DarkGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Help & About
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF5F5F5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KabadiWala v1.0.0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "Bridging Informal Collectors & Formal E-Waste Recyclers",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Helpline: 1800-2026-KABADI (Toll Free)",
                        fontSize = 11.sp,
                        color = GreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
