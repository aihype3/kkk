package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.MaterialCategory

@Composable
fun AiMaterialCategorizerScreen(
    currentLanguage: Language = Language.ENGLISH,
    category: MaterialCategory = MaterialCategory.PCB,
    capturedBitmap: Bitmap? = null,
    confidencePercent: Int = 92,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Header Row with [Offline] badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("btn_back_categorizer")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = when (currentLanguage) {
                            Language.HINDI -> "AI मटेरियल कैटेगराइज़र"
                            Language.MARATHI -> "AI मटेरियल वर्गीकरण"
                            Language.ENGLISH -> "AI Material Categorizer"
                        },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                // Offline pill badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(1.dp, Color(0xFF81C784))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E7D32))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "ऑफलाइन"
                                Language.MARATHI -> "ऑफलाइन"
                                Language.ENGLISH -> "Offline"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // AI Brain Center Graphic
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    border = BorderStroke(1.dp, Color(0xFFA5D6A7))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Brain",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "AI मटेरियल कैटेगराइज़र"
                        Language.MARATHI -> "AI मटेरियल वर्गीकरण"
                        Language.ENGLISH -> "AI Material Categorizer"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "ऑफलाइन काम करता है • तेज़ • सटीक"
                        Language.MARATHI -> "ऑफलाइन चालते • जलद • अचूक"
                        Language.ENGLISH -> "Works offline • Fast • Accurate"
                    },
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Detection Result Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_ai_result"),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Photo Preview
                    Surface(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        if (capturedBitmap != null) {
                            Image(
                                bitmap = capturedBitmap.asImageBitmap(),
                                contentDescription = "Captured Scrap",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.img_scrap_pcb),
                                contentDescription = "PCB Scrap",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(18.dp))

                    // Labels & Category
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "पहचाना गया ई-कचरा"
                                Language.MARATHI -> "ओळखलेला ई-कचरा"
                                Language.ENGLISH -> "Detected Category"
                            },
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> category.nameHi
                                Language.MARATHI -> category.nameMr
                                Language.ENGLISH -> if (category == MaterialCategory.PCB) "PCBs" else category.nameEn
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "विश्वसनीयता"
                                Language.MARATHI -> "विश्वासार्हता"
                                Language.ENGLISH -> "Confidence"
                            },
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "$confidencePercent%",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Confirm Button
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 8.dp)
                .testTag("btn_confirm_category"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2E7D32)
            )
        ) {
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "श्रेणी की पुष्टि करें (Confirm)"
                    Language.MARATHI -> "प्रकार निश्चित करा (Confirm)"
                    Language.ENGLISH -> "Confirm Category"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}
