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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.MaterialCategory

data class CategoryGridItem(
    val title: String,
    val imageResId: Int,
    val category: MaterialCategory
)

@Composable
fun SelectMaterialCategoryScreen(
    currentLanguage: Language = Language.ENGLISH,
    onBack: () -> Unit,
    onCategorySelected: (MaterialCategory) -> Unit
) {
    val categoryItems = listOf(
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "CRT (कैथोड रे ट्यूब)"
                Language.MARATHI -> "CRT मॉनिटर्स"
                Language.ENGLISH -> "CRTs"
            },
            imageResId = R.drawable.img_cat_crt,
            category = MaterialCategory.CRT
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "LCD / LED पैनल"
                Language.MARATHI -> "LCD / LED पॅनेल"
                Language.ENGLISH -> "LCD/LED panels"
            },
            imageResId = R.drawable.img_cat_lcd,
            category = MaterialCategory.LCD
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "सर्किट बोर्ड्स (PCBs)"
                Language.MARATHI -> "सर्किट बोर्ड्स (PCBs)"
                Language.ENGLISH -> "PCBs"
            },
            imageResId = R.drawable.img_scrap_pcb,
            category = MaterialCategory.PCB
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "केबल्स और तारें"
                Language.MARATHI -> "केबल्स आणि वायर्स"
                Language.ENGLISH -> "Cables"
            },
            imageResId = R.drawable.img_cat_cables,
            category = MaterialCategory.CABLE
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "बैटरियां"
                Language.MARATHI -> "बॅटऱ्या"
                Language.ENGLISH -> "Batteries"
            },
            imageResId = R.drawable.img_cat_batteries,
            category = MaterialCategory.BATTERY
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "मोटर्स"
                Language.MARATHI -> "मोटारी"
                Language.ENGLISH -> "Motors"
            },
            imageResId = R.drawable.img_cat_motor,
            category = MaterialCategory.MOTOR
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "मैग्नेट असेंबली"
                Language.MARATHI -> "मॅग्नेट असेंब्ली"
                Language.ENGLISH -> "Magnet-Bearing assemblies"
            },
            imageResId = R.drawable.img_cat_magnet,
            category = MaterialCategory.MAGNET
        ),
        CategoryGridItem(
            title = when (currentLanguage) {
                Language.HINDI -> "मिश्रित प्लास्टिक व अन्य ई-कचरा"
                Language.MARATHI -> "मिश्र प्लास्टिक व इतर ई-कचरा"
                Language.ENGLISH -> "Mixed plastic and other E-waste"
            },
            imageResId = R.drawable.img_cat_plastic,
            category = MaterialCategory.PLASTIC
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("btn_back_category")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1F2937),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "मटेरियल श्रेणी चुनें"
                    Language.MARATHI -> "मटेरियल प्रकार निवडा"
                    Language.ENGLISH -> "Select Material Category"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        // 2-Column Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("category_grid")
        ) {
            items(categoryItems) { item ->
                CategoryCard(
                    item = item,
                    onClick = { onCategorySelected(item.category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    item: CategoryGridItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("category_item_${item.category.id}"),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(item.imageResId),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    lineHeight = 16.sp,
                    maxLines = 2
                )
            }
        }
    }
}
