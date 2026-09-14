package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.AppScreen
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm

@Composable
fun KabadiWalaBottomNav(
    currentScreen: AppScreen,
    currentLanguage: Language,
    onNavigate: (AppScreen) -> Unit,
    onNewLotClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            NavItem(
                icon = Icons.Default.Home,
                label = when (currentLanguage) {
                    Language.HINDI -> "होम"
                    Language.MARATHI -> "मुख्य"
                    Language.ENGLISH -> "Home"
                },
                isSelected = currentScreen == AppScreen.HOME,
                testTag = "nav_home",
                onClick = { onNavigate(AppScreen.HOME) }
            )

            // 2. Rate List
            NavItem(
                icon = Icons.Default.ReceiptLong,
                label = when (currentLanguage) {
                    Language.HINDI -> "रेट लिस्ट"
                    Language.MARATHI -> "दर यादी"
                    Language.ENGLISH -> "Rate List"
                },
                isSelected = currentScreen == AppScreen.RATES_BOARD,
                testTag = "nav_rates",
                onClick = { onNavigate(AppScreen.RATES_BOARD) }
            )

            // 3. Center Prominent Sell Option
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .clickable { onNewLotClick() }
                    .testTag("nav_sell")
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF2E7D32)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Sell",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "बेचें"
                        Language.MARATHI -> "विका"
                        Language.ENGLISH -> "Sell"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            // 4. History
            NavItem(
                icon = Icons.Default.History,
                label = when (currentLanguage) {
                    Language.HINDI -> "इतिहास"
                    Language.MARATHI -> "इतिहास"
                    Language.ENGLISH -> "History"
                },
                isSelected = currentScreen == AppScreen.HISTORY,
                testTag = "nav_history",
                onClick = { onNavigate(AppScreen.HISTORY) }
            )

            // 5. Profile
            NavItem(
                icon = Icons.Default.Person,
                label = when (currentLanguage) {
                    Language.HINDI -> "प्रोफाइल"
                    Language.MARATHI -> "प्रोफाइल"
                    Language.ENGLISH -> "Profile"
                },
                isSelected = currentScreen == AppScreen.PROFILE,
                testTag = "nav_profile",
                onClick = { onNavigate(AppScreen.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) GreenPrimary else Color.Gray,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) GreenPrimary else Color.Gray
        )
    }
}
