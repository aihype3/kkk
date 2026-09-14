package com.example.ui.components

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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppRole
import com.example.data.model.Language
import com.example.ui.AppScreen
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.util.AppStrings

@Composable
fun KabadiWalaTopBar(
    currentLanguage: Language,
    currentRole: AppRole,
    isOffline: Boolean,
    isSyncing: Boolean,
    onLanguageSelect: (Language) -> Unit,
    onRoleSelect: (AppRole) -> Unit,
    onToggleOffline: () -> Unit,
    onProfileClick: () -> Unit,
    currentScreen: AppScreen = AppScreen.HOME
) {
    var showLangMenu by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Offline / Sync banner indicator
            if (isOffline || isSyncing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isOffline) AmberAccent.copy(alpha = 0.15f) else GreenContainer)
                        .clickable { onToggleOffline() }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isSyncing) Icons.Default.Sync else Icons.Default.CloudOff,
                        contentDescription = "Sync Status",
                        modifier = Modifier.size(16.dp),
                        tint = if (isOffline) AmberAccent else GreenPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSyncing) {
                            AppStrings.get("syncing", currentLanguage)
                        } else {
                            AppStrings.get("offline_mode", currentLanguage) + " (Tap to toggle)"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOffline) AmberAccent else GreenPrimary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Main Bar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo + Title on LEFT
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.25f))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_app_icon),
                            contentDescription = "KabadiWala Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "KabadiWala",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = AppStrings.get("tagline", currentLanguage),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // If on Home Page: KEEP RIGHT CLEAR!
                if (currentScreen == AppScreen.HOME) {
                    Spacer(modifier = Modifier.width(1.dp))
                } else {
                    // Other screens: actions
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Role Switcher Chip
                        Box {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = when (currentRole) {
                                    AppRole.COLLECTOR -> GreenContainer
                                    AppRole.RECYCLER -> Color(0xFFE1F5FE)
                                    AppRole.ADMIN -> Color(0xFFFFF3E0)
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { showRoleMenu = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .testTag("role_switcher_chip")
                            ) {
                                Text(
                                    text = when (currentRole) {
                                        AppRole.COLLECTOR -> "♻️ Kabadi"
                                        AppRole.RECYCLER -> "🏭 Recycler"
                                        AppRole.ADMIN -> "📊 Admin"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (currentRole) {
                                        AppRole.COLLECTOR -> GreenPrimaryDark
                                        AppRole.RECYCLER -> Color(0xFF0277BD)
                                        AppRole.ADMIN -> Color(0xFFE65100)
                                    }
                                )
                            }

                            DropdownMenu(
                                expanded = showRoleMenu,
                                onDismissRequest = { showRoleMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("♻️ Collector / Kabadiwala") },
                                    onClick = {
                                        onRoleSelect(AppRole.COLLECTOR)
                                        showRoleMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("🏭 Recycler Dashboard") },
                                    onClick = {
                                        onRoleSelect(AppRole.RECYCLER)
                                        showRoleMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("📊 Admin & Unit Economics") },
                                    onClick = {
                                        onRoleSelect(AppRole.ADMIN)
                                        showRoleMenu = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Language Selector
                        Box {
                            Surface(
                                shape = CircleShape,
                                color = GreenContainer,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .clickable { showLangMenu = true }
                                    .testTag("language_toggle_btn"),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.HINDI -> "हि"
                                            Language.MARATHI -> "म"
                                            Language.ENGLISH -> "EN"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenPrimary
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showLangMenu,
                                onDismissRequest = { showLangMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("हिन्दी (Hindi)") },
                                    onClick = {
                                        onLanguageSelect(Language.HINDI)
                                        showLangMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("मराठी (Marathi)") },
                                    onClick = {
                                        onLanguageSelect(Language.MARATHI)
                                        showLangMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("English") },
                                    onClick = {
                                        onLanguageSelect(Language.ENGLISH)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Profile Icon
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .clickable { onProfileClick() }
                                .testTag("profile_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.size(18.dp),
                                    tint = GreenPrimaryDark
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
