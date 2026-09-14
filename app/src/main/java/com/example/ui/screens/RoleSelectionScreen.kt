package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.ui.theme.GreenPrimary

@Composable
fun RoleSelectionScreen(
    currentLanguage: Language = Language.ENGLISH,
    onRoleSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                // Logo icon
                Surface(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.2f))
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_app_icon),
                        contentDescription = "KabadiWala Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "KabadiWala",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF165B27)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "अपनी भूमिका चुनें"
                        Language.MARATHI -> "तुमची भूमिका निवडा"
                        Language.ENGLISH -> "Choose Your Role"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF374151)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Role Card 1: Seller
                RoleCardItem(
                    title = when (currentLanguage) {
                        Language.HINDI -> "विक्रेता (Seller)"
                        Language.MARATHI -> "विक्रेता (Seller)"
                        Language.ENGLISH -> "Seller"
                    },
                    subtitle = when (currentLanguage) {
                        Language.HINDI -> "अपना ई-कचरा बेचें और सर्वोत्तम मूल्य पाएं"
                        Language.MARATHI -> "तुमचा ई-कचरा विका आणि सर्वोत्तम मूल्य मिळवा"
                        Language.ENGLISH -> "Sell your e-waste and get the best value"
                    },
                    icon = Icons.Default.Person,
                    accentColor = Color(0xFF2E7D32),
                    iconBgColor = Color(0xFFE8F5E9),
                    cardBgColor = Color(0xFFF4FBF4),
                    borderColor = Color(0xFF81C784),
                    testTag = "role_seller",
                    onClick = { onRoleSelected("Seller") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Role Card 2: Recycler
                RoleCardItem(
                    title = when (currentLanguage) {
                        Language.HINDI -> "रीसाइक्लर (Recycler)"
                        Language.MARATHI -> "रीसायकलर (Recycler)"
                        Language.ENGLISH -> "Recycler"
                    },
                    subtitle = when (currentLanguage) {
                        Language.HINDI -> "रीसायकल करें और हरित कल में योगदान दें"
                        Language.MARATHI -> "रीसायकल करा आणि हरित उद्यासाठी योगदान द्या"
                        Language.ENGLISH -> "Recycle and contribute to a greener tomorrow"
                    },
                    icon = Icons.Default.Recycling,
                    accentColor = Color(0xFF1976D2),
                    iconBgColor = Color(0xFFE3F2FD),
                    cardBgColor = Color(0xFFF5F9FF),
                    borderColor = Color(0xFF90CAF9),
                    testTag = "role_recycler",
                    onClick = { onRoleSelected("Recycler") }
                )
            }

            // Bottom subtle eco graphic
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img_home_eco_banner),
                    contentDescription = "Eco Banner",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }
        }
    }
}

@Composable
private fun RoleCardItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    iconBgColor: Color,
    cardBgColor: Color,
    borderColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = cardBgColor,
        border = BorderStroke(1.2.dp, borderColor),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = iconBgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Select $title",
                tint = accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
