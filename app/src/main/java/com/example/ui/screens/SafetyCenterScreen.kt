package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.local.SafetyTipEntity
import com.example.data.model.Language
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAlertRed
import com.example.util.AppStrings

@Composable
fun SafetyCenterScreen(
    tips: List<SafetyTipEntity>,
    currentLanguage: Language,
    onSpeakTip: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("safety_center_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            // Hero Safety Banner with Generated Asset
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_safe_recycling),
                            contentDescription = "Safe Recycling Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Safety",
                                tint = GreenPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.get("safety_title", currentLanguage),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = GreenPrimaryDark
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "सुरक्षित काम, बेहतर स्वास्थ्य और पूरी कमाई। इन नियमों का पालन करें:"
                                Language.MARATHI -> "सुरक्षित काम, उत्तम आरोग्य आणि पूर्ण कमाई. या नियमांचे पालन करा:"
                                Language.ENGLISH -> "Safe handling protects your lungs, skin and family. Follow these guidelines:"
                            },
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        items(tips) { tip ->
            val title = when (currentLanguage) {
                Language.HINDI -> tip.titleHi
                Language.MARATHI -> tip.titleMr
                Language.ENGLISH -> tip.titleEn
            }
            val desc = when (currentLanguage) {
                Language.HINDI -> tip.descHi
                Language.MARATHI -> tip.descMr
                Language.ENGLISH -> tip.descEn
            }
            val audioText = when (currentLanguage) {
                Language.HINDI -> tip.audioHi
                Language.MARATHI -> tip.audioMr
                Language.ENGLISH -> tip.audioEn
            }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (tip.isDanger) Color(0xFFFFF7F7) else Color.White
                ),
                border = BorderStroke(
                    width = 1.2.dp,
                    color = if (tip.isDanger) StatusAlertRed.copy(alpha = 0.4f) else OutlineWarm
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("safety_tip_card_${tip.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = tip.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tip.isDanger) StatusAlertRed else GreenPrimaryDark
                            )
                        }

                        // 🔊 Listen Audio Button
                        Button(
                            onClick = { onSpeakTip(audioText) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.padding(start = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "सुनें 🔊",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = desc,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
