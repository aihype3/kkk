package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.RecyclerEntity
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.ui.LotCreationDraft
import com.example.ui.components.DigitalReceiptCard
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.OutlineWarm
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.util.AiClassifierHelper
import com.example.util.AppStrings
import com.example.util.RecyclerScore

@Composable
fun CreateLotScreen(
    draft: LotCreationDraft,
    allRecyclers: List<RecyclerEntity>,
    currentLanguage: Language,
    onStepChange: (Int) -> Unit,
    onAttachPhoto: () -> Unit,
    onAttachPhotoBitmap: (Bitmap?, String) -> Unit = { _, _ -> },
    onRunAiClassification: (Bitmap?) -> Unit = {},
    onSelectMaterial: (MaterialCategory) -> Unit,
    onSelectSubCategory: (String) -> Unit,
    onUpdateWeight: (Double) -> Unit,
    onUpdateCondition: (String) -> Unit,
    onUpdateSource: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onSelectRecycler: (RecyclerEntity) -> Unit,
    onConfirmLot: () -> Unit,
    onCompleteHandoverPayment: (String, String) -> Unit,
    onSpeakPrice: () -> Unit,
    onSpeakText: (String) -> Unit = {},
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("create_lot_flow_screen")
    ) {
        // Step Header with Back button & Step Progress
        StepHeader(
            currentStep = draft.step,
            currentLanguage = currentLanguage,
            onBack = {
                if (draft.step > 1 && draft.step < 7) {
                    onStepChange(draft.step - 1)
                } else {
                    onDone()
                }
            }
        )

        // Step Contents
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (draft.step) {
                1 -> Step1Photo(
                    draft = draft,
                    currentLanguage = currentLanguage,
                    onAttachPhotoBitmap = onAttachPhotoBitmap,
                    onRunAiClassification = onRunAiClassification,
                    onSpeakText = onSpeakText,
                    onSelectMaterial = onSelectMaterial,
                    onSelectSubCategory = onSelectSubCategory,
                    onUpdateCondition = onUpdateCondition,
                    onUpdateWeight = onUpdateWeight,
                    onNext = { onStepChange(2) },
                    onSkipToDetails = { onStepChange(3) }
                )
                2 -> Step2Category(
                    selected = draft.material,
                    currentLanguage = currentLanguage,
                    onSelect = {
                        onSelectMaterial(it)
                        onStepChange(3)
                    }
                )
                3 -> Step3Details(
                    draft = draft,
                    currentLanguage = currentLanguage,
                    onSelectSubCategory = onSelectSubCategory,
                    onUpdateWeight = onUpdateWeight,
                    onUpdateCondition = onUpdateCondition,
                    onUpdateSource = onUpdateSource,
                    onUpdateNotes = onUpdateNotes,
                    onNext = { onStepChange(4) }
                )
                4 -> Step4PriceEstimation(
                    draft = draft,
                    currentLanguage = currentLanguage,
                    onSpeakPrice = onSpeakPrice,
                    onNext = { onStepChange(5) }
                )
                5 -> Step5SelectRecycler(
                    draft = draft,
                    allRecyclers = allRecyclers,
                    currentLanguage = currentLanguage,
                    onSelectRecycler = { rec ->
                        onSelectRecycler(rec)
                        onStepChange(6)
                    }
                )
                6 -> Step6LotSummary(
                    draft = draft,
                    currentLanguage = currentLanguage,
                    onConfirm = onConfirmLot
                )
                7 -> Step7HandoverAndReceipt(
                    draft = draft,
                    currentLanguage = currentLanguage,
                    onUpdatePayment = onCompleteHandoverPayment,
                    onDone = onDone
                )
            }
        }
    }
}

@Composable
private fun StepHeader(
    currentStep: Int,
    currentLanguage: Language,
    onBack: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = GreenPrimaryDark,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = when (currentStep) {
                            1 -> "Step 1: ${AppStrings.get("take_photo", currentLanguage)}"
                            2 -> "Step 2: ${AppStrings.get("select_category", currentLanguage)}"
                            3 -> "Step 3: ${AppStrings.get("condition", currentLanguage)} & ${AppStrings.get("approx_weight", currentLanguage)}"
                            4 -> "Step 4: ${AppStrings.get("estimated_value", currentLanguage)}"
                            5 -> "Step 5: ${AppStrings.get("find_recyclers", currentLanguage)}"
                            6 -> "Step 6: ${AppStrings.get("confirm_lot", currentLanguage)}"
                            else -> "Step 7: ${AppStrings.get("receipt_title", currentLanguage)}"
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "Step $currentStep of 7",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Step Counter Badge
            Surface(
                shape = CircleShape,
                color = GreenContainer,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "$currentStep/7",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// Helper to generate realistic visual scrap sample bitmaps for one-tap AI testing in emulators/offline
private fun createSampleScrapBitmap(category: MaterialCategory): Bitmap {
    val bitmap = Bitmap.createBitmap(480, 360, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    val (bgColor, accentColor, title) = when (category) {
        MaterialCategory.PCB -> Triple(
            android.graphics.Color.rgb(16, 75, 42),
            android.graphics.Color.rgb(212, 175, 55),
            "Motherboard PCB Board"
        )
        MaterialCategory.CABLE -> Triple(
            android.graphics.Color.rgb(140, 50, 20),
            android.graphics.Color.rgb(255, 140, 0),
            "Copper Cables Bundle"
        )
        MaterialCategory.BATTERY -> Triple(
            android.graphics.Color.rgb(35, 45, 60),
            android.graphics.Color.rgb(220, 53, 69),
            "Lithium-ion Battery Pack"
        )
        MaterialCategory.MOTOR -> Triple(
            android.graphics.Color.rgb(55, 70, 85),
            android.graphics.Color.rgb(0, 180, 216),
            "Electric Stator / Motor"
        )
        MaterialCategory.LCD -> Triple(
            android.graphics.Color.rgb(20, 35, 70),
            android.graphics.Color.rgb(100, 200, 255),
            "TFT LCD Display Screen"
        )
        else -> Triple(
            android.graphics.Color.rgb(40, 50, 45),
            android.graphics.Color.rgb(200, 200, 200),
            "E-waste Scrap Item"
        )
    }

    val bgPaint = android.graphics.Paint().apply {
        color = bgColor
        isAntiAlias = true
    }
    canvas.drawRect(0f, 0f, 480f, 360f, bgPaint)

    // Draw grid traces to simulate real e-waste texture
    val gridPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.argb(45, 255, 255, 255)
        strokeWidth = 2f
    }
    for (x in 0..480 step 30) {
        canvas.drawLine(x.toFloat(), 0f, x.toFloat(), 360f, gridPaint)
    }
    for (y in 0..360 step 30) {
        canvas.drawLine(0f, y.toFloat(), 480f, y.toFloat(), gridPaint)
    }

    // Draw central hardware badge
    val cardPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.argb(190, 0, 0, 0)
        isAntiAlias = true
    }
    canvas.drawRoundRect(40f, 60f, 440f, 300f, 24f, 24f, cardPaint)

    val borderPaint = android.graphics.Paint().apply {
        color = accentColor
        style = android.graphics.Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    canvas.drawRoundRect(40f, 60f, 440f, 300f, 24f, 24f, borderPaint)

    val titlePaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 24f
        isFakeBoldText = true
        textAlign = android.graphics.Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("${category.iconEmoji} $title", 240f, 150f, titlePaint)

    val subtitlePaint = android.graphics.Paint().apply {
        color = accentColor
        textSize = 16f
        textAlign = android.graphics.Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("KabadiWala AI Material Grading Sample", 240f, 190f, subtitlePaint)

    val hintPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.LTGRAY
        textSize = 14f
        textAlign = android.graphics.Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText("Ready for Gemini Vision Multimodal Inspection", 240f, 230f, hintPaint)

    return bitmap
}

// ------------------- STEP 1: PHOTO & AI IDENTIFICATION -------------------
@Composable
private fun Step1Photo(
    draft: LotCreationDraft,
    currentLanguage: Language,
    onAttachPhotoBitmap: (Bitmap?, String) -> Unit,
    onRunAiClassification: (Bitmap?) -> Unit,
    onSpeakText: (String) -> Unit,
    onSelectMaterial: (MaterialCategory) -> Unit,
    onSelectSubCategory: (String) -> Unit,
    onUpdateCondition: (String) -> Unit,
    onUpdateWeight: (Double) -> Unit,
    onNext: () -> Unit,
    onSkipToDetails: () -> Unit
) {
    val context = LocalContext.current

    // Real Camera Launcher (Takes real photo from device camera or camera emulator)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap: Bitmap? ->
        if (capturedBitmap != null) {
            onAttachPhotoBitmap(capturedBitmap, "camera_photo_${System.currentTimeMillis()}")
            onRunAiClassification(capturedBitmap)
        }
    }

    // Camera Permission Launcher for runtime dangerous permission handling
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Log.e("CreateLotScreen", "Error launching camera after permission granted", e)
                Toast.makeText(
                    context,
                    when (currentLanguage) {
                        Language.HINDI -> "कैमरा खोलने में त्रुटि हुई"
                        Language.MARATHI -> "कॅमेरा उघडण्यात त्रुटी आली"
                        Language.ENGLISH -> "Unable to launch camera app"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                when (currentLanguage) {
                    Language.HINDI -> "फोटो लेने के लिए कैमरा अनुमति आवश्यक है"
                    Language.MARATHI -> "फोटो घेण्यासाठी कॅमेरा परवानगी आवश्यक आहे"
                    Language.ENGLISH -> "Camera permission is required to take scrap photos"
                },
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Real Photo Picker (Zero-permission Android Photo Picker for picking real images from gallery)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val bitmap = try {
                if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                        decoder.isMutableRequired = true
                    }
                }
            } catch (e: Exception) {
                null
            }
            onAttachPhotoBitmap(bitmap, uri.toString())
            onRunAiClassification(bitmap)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "📷 " + AppStrings.get("take_photo", currentLanguage),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Text(
                text = when (currentLanguage) {
                    Language.HINDI -> "कैमरा से स्क्रैप की फोटो लें या AI सैंपल चुनें"
                    Language.MARATHI -> "कॅमेराने स्क्रॅपचा फोटो घ्या किंवा AI सॅम्पल निवडा"
                    Language.ENGLISH -> "Capture photo or pick an AI test sample below"
                },
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Photo Preview Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, if (draft.hasAiIdentified) StatusAuthorizedGreen else OutlineWarm),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .testTag("step1_photo_preview_card")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (draft.photoBitmap != null) {
                        Image(
                            bitmap = draft.photoBitmap.asImageBitmap(),
                            contentDescription = "Scrap Photo Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.7f),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusAuthorizedGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Photo Attached",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else if (draft.photoUri.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_safe_recycling),
                            contentDescription = "Scrap Photo Preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GreenContainer,
                                modifier = Modifier.size(72.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Camera",
                                        tint = GreenPrimary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = when (currentLanguage) {
                                    Language.HINDI -> "स्क्रैप की स्पष्ट फोटो लें"
                                    Language.MARATHI -> "स्क्रॅपचा स्पष्ट फोटो घ्या"
                                    Language.ENGLISH -> "Take a clear scrap photo"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimaryDark
                            )
                            Text(
                                text = when (currentLanguage) {
                                    Language.HINDI -> "AI अपने आप कैटेगरी, वजन व ग्रेड पहचान लेगा"
                                    Language.MARATHI -> "AI आपोआप प्रकार, वजन आणि ग्रेड ओळखेल"
                                    Language.ENGLISH -> "Gemini AI will categorize and grade automatically"
                                },
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Floating analyze indicator if currently processing
                    if (draft.isAnalyzingAi) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.Black.copy(alpha = 0.8f),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    color = SaffronAccent,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Analyzing with Gemini AI...",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Real Camera & Gallery Picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val hasCameraPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasCameraPermission) {
                            try {
                                cameraLauncher.launch(null)
                            } catch (e: Exception) {
                                Log.e("CreateLotScreen", "Error launching camera", e)
                                Toast.makeText(
                                    context,
                                    when (currentLanguage) {
                                        Language.HINDI -> "कैमरा खोलने में त्रुटि हुई"
                                        Language.MARATHI -> "कॅमेरा उघडण्यात त्रुटी आली"
                                        Language.ENGLISH -> "Unable to open camera on this device"
                                    },
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("step1_camera_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get("camera", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("step1_gallery_btn"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, GreenPrimary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GreenPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Gallery",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.get("gallery", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick E-Waste Sample Testing Strip (Instant one-tap testing)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "⚡ तुरंत AI टेस्ट के लिए सैंपल चुनें:"
                        Language.MARATHI -> "⚡ त्वरित AI चाचणीसाठी नमुना निवडा:"
                        Language.ENGLISH -> "⚡ Or tap a sample to test AI instantly:"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val samples = listOf(
                        Triple(MaterialCategory.PCB, "Motherboard", "🟢"),
                        Triple(MaterialCategory.CABLE, "Copper Wire", "🔴"),
                        Triple(MaterialCategory.BATTERY, "Li-ion Pack", "🔋"),
                        Triple(MaterialCategory.LCD, "Display Panel", "📺"),
                        Triple(MaterialCategory.MOTOR, "Electric Motor", "⚙️")
                    )
                    items(samples) { (category, label, emoji) ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = GreenContainer,
                            border = BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                val sampleBitmap = createSampleScrapBitmap(category)
                                onAttachPhotoBitmap(sampleBitmap, "sample_${category.id}")
                                onRunAiClassification(sampleBitmap)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimaryDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // AI Classification Assistant Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (draft.hasAiIdentified) Color(0xFFF9FFF9) else AmberContainer
                ),
                border = BorderStroke(
                    1.2.dp,
                    if (draft.hasAiIdentified) StatusAuthorizedGreen else AmberAccent.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("step1_ai_card")
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI",
                                tint = SaffronAccent,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "AI Material Categorizer",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF4A2500)
                                )
                                Text(
                                    text = if (draft.isRealAi) "Live Gemini Vision Model" else "Smart On-Device Vision Engine",
                                    fontSize = 11.sp,
                                    color = if (draft.isRealAi) StatusAuthorizedGreen else Color(0xFF7A4A00),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val bmp = draft.photoBitmap ?: createSampleScrapBitmap(MaterialCategory.PCB)
                                if (draft.photoBitmap == null) {
                                    onAttachPhotoBitmap(bmp, "manual_sample_pcb")
                                }
                                onRunAiClassification(bmp)
                            },
                            enabled = !draft.isAnalyzingAi,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("run_ai_classify_btn")
                        ) {
                            if (draft.isAnalyzingAi) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FlashOn,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = AppStrings.get("ai_identify", currentLanguage),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // When AI Analysis is in progress
                    if (draft.isAnalyzingAi) {
                        Spacer(modifier = Modifier.height(14.dp))
                        LinearProgressIndicator(
                            color = SaffronAccent,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when (currentLanguage) {
                                Language.HINDI -> "AI फोटो की जांच कर रहा है... कृपया 2 सेकंड प्रतीक्षा करें"
                                Language.MARATHI -> "AI फोटो तपासत आहे... कृपया 2 सेकंद थांबा"
                                Language.ENGLISH -> "Gemini AI inspecting material grading and hazard markers..."
                            },
                            fontSize = 12.sp,
                            color = Color(0xFF6A3B00),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }

                    // When AI has identified the material
                    if (draft.hasAiIdentified) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFE0E0E0))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Category & Confidence Banner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${draft.material.iconEmoji} ${draft.material.getLocalizedName(currentLanguage)}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenPrimaryDark
                                )
                                Text(
                                    text = draft.subCategory,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.DarkGray
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GreenContainer,
                                border = BorderStroke(1.dp, StatusAuthorizedGreen.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✓ ${draft.aiConfidence}% Match",
                                        color = GreenPrimaryDark,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Detected Features Tags
                        if (draft.aiFeatures.isNotEmpty()) {
                            Text(
                                text = "Detected Features:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                draft.aiFeatures.take(3).forEach { feat ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFE8F5E9),
                                        border = BorderStroke(0.5.dp, Color(0xFFA5D6A7))
                                    ) {
                                        Text(
                                            text = "• $feat",
                                            fontSize = 11.sp,
                                            color = Color(0xFF2E7D32),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Localized AI description
                        if (draft.aiDescription.isNotEmpty()) {
                            Text(
                                text = draft.aiDescription,
                                fontSize = 12.sp,
                                color = Color(0xFF333333),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Safety Hazard Warning if any
                        if (!draft.aiSafetyWarning.isNullOrEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF3E0),
                                border = BorderStroke(1.dp, Color(0xFFFFB74D)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Warning",
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = draft.aiSafetyWarning,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFBF360C)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Audio Speech & Quick Action Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val speech = "${draft.material.getLocalizedName(currentLanguage)}. ${draft.subCategory}. ${draft.aiDescription}"
                                    onSpeakText(speech)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, GreenPrimary),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppStrings.get("speak", currentLanguage),
                                    fontSize = 12.sp,
                                    color = GreenPrimary
                                )
                            }

                            Button(
                                onClick = {
                                    onSelectMaterial(draft.material)
                                    onSelectSubCategory(draft.subCategory)
                                    onUpdateCondition(draft.condition)
                                    onUpdateWeight(draft.weightKg)
                                    onSkipToDetails()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.HINDI -> "✓ AI परिणाम लागू करें"
                                        Language.MARATHI -> "✓ AI निकाल स्वीकारा"
                                        Language.ENGLISH -> "✓ Use AI Result"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Standard Navigation Buttons
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("step1_continue_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "श्रेणी सूची से चुनें (Select Category) ➔"
                        Language.MARATHI -> "प्रकार यादीतून निवडा ➔"
                        Language.ENGLISH -> "Select Category Manually ➔"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ------------------- STEP 2: MATERIAL CATEGORY CARDS -------------------
@Composable
private fun Step2Category(
    selected: MaterialCategory,
    currentLanguage: Language,
    onSelect: (MaterialCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = AppStrings.get("select_category", currentLanguage),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimaryDark
        )
        Text(
            text = "Tap on the e-waste scrap material to continue:",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(MaterialCategory.entries.toTypedArray()) { cat ->
                val isSelected = cat == selected
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) GreenContainer else Color.White
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) GreenPrimary else OutlineWarm
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable { onSelect(cat) }
                        .testTag("category_card_${cat.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = cat.iconEmoji, fontSize = 28.sp)
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = GreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = cat.getLocalizedName(currentLanguage),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 2,
                                color = GreenPrimaryDark
                            )
                            Text(
                                text = "₹${cat.defaultRatePerKg}/kg avg",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = SaffronAccent
                            )
                        }
                    }
                }
            }
        }
    }
}

// ------------------- STEP 3: DETAILS & WEIGHT STEPPER -------------------
@Composable
private fun Step3Details(
    draft: LotCreationDraft,
    currentLanguage: Language,
    onSelectSubCategory: (String) -> Unit,
    onUpdateWeight: (Double) -> Unit,
    onUpdateCondition: (String) -> Unit,
    onUpdateSource: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onNext: () -> Unit
) {
    var isSubCategoryExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Selected Material Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = draft.material.iconEmoji, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = draft.material.getLocalizedName(currentLanguage),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "Standard Market Rate: ₹${draft.material.defaultRatePerKg}/kg",
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-category selector
            Text(
                text = "Sub-category (उप-प्रकार):",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, OutlineWarm, RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { isSubCategoryExpanded = true }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = draft.subCategory, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "▼", fontSize = 12.sp, color = Color.Gray)
            }

            DropdownMenu(
                expanded = isSubCategoryExpanded,
                onDismissRequest = { isSubCategoryExpanded = false }
            ) {
                draft.material.subCategories.forEach { sub ->
                    DropdownMenuItem(
                        text = { Text(sub) },
                        onClick = {
                            onSelectSubCategory(sub)
                            isSubCategoryExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Approximate Weight Stepper: [ - ] 10 [ + ] kg
            Text(
                text = AppStrings.get("approx_weight", currentLanguage),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = GreenPrimaryDark
            )
            Text(
                text = "Weight does not need to be perfectly accurate for initial quote",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    IconButton(
                        onClick = { onUpdateWeight(draft.weightKg - 1.0) },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(GreenContainer)
                            .testTag("weight_decrement_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Minus",
                            tint = GreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Weight Display
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${draft.weightKg.toInt()} kg",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "approximate",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    // Plus Button
                    IconButton(
                        onClick = { onUpdateWeight(draft.weightKg + 1.0) },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(GreenContainer)
                            .testTag("weight_increment_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Plus",
                            tint = GreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Condition Options: Good / Used / Damaged
            Text(
                text = AppStrings.get("condition", currentLanguage),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Good", "Used", "Damaged").forEach { cond ->
                    val isSelected = draft.condition.equals(cond, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) GreenPrimary else Color.White,
                        border = BorderStroke(1.dp, if (isSelected) GreenPrimary else OutlineWarm),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onUpdateCondition(cond) }
                    ) {
                        Text(
                            text = cond,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Source Options: Household / Shop / Office / Industrial / Other
            Text(
                text = AppStrings.get("source", currentLanguage),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Shop", "Household", "Office", "Industrial").forEach { src ->
                    val isSelected = draft.sourceType.equals(src, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AmberAccent else Color.White,
                        border = BorderStroke(1.dp, if (isSelected) AmberAccent else OutlineWarm),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onUpdateSource(src) }
                    ) {
                        Text(
                            text = src,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // CTA: "Price Dekhein"
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("step3_see_price_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    text = "${AppStrings.get("see_price", currentLanguage)} ➔",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ------------------- STEP 4: PRICE ESTIMATION & TREND -------------------
@Composable
private fun Step4PriceEstimation(
    draft: LotCreationDraft,
    currentLanguage: Language,
    onSpeakPrice: () -> Unit,
    onNext: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Big Estimated Value Card
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = GreenContainer),
                border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = AppStrings.get("estimated_value", currentLanguage),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${draft.estimatedMin} – ₹${draft.estimatedMax}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = GreenPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${AppStrings.get("approx_rate", currentLanguage)} ₹${draft.quotedRatePerKg}/kg",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaffronAccent
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = GreenPrimary.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = "Material: ${draft.material.nameEn}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Weight: ${draft.weightKg.toInt()} kg",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "📍 Indore",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Prominent Voice Button: 🔊 "Price Sunayein"
                    Button(
                        onClick = onSpeakPrice,
                        colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("listen_price_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak Price",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get("listen_price", currentLanguage),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Disclaimer
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ℹ️ " + AppStrings.get("price_disclaimer", currentLanguage),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Trend Card: 📈 Increasing, June -> July -> August -> Sept
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
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
                            text = AppStrings.get("price_trend", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GreenPrimaryDark
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GreenContainer
                        ) {
                            Text(
                                text = AppStrings.get("increasing", currentLanguage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusAuthorizedGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Historical Mini Bar Chart
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TrendBarItem(month = "June", rate = (draft.quotedRatePerKg * 0.88).toInt(), heightDp = 40)
                        TrendBarItem(month = "July", rate = (draft.quotedRatePerKg * 0.92).toInt(), heightDp = 52)
                        TrendBarItem(month = "August", rate = (draft.quotedRatePerKg * 0.96).toInt(), heightDp = 64)
                        TrendBarItem(month = "Sept (Now)", rate = draft.quotedRatePerKg, heightDp = 78, isHighlight = true)
                    }
                }
            }

            // Unusual Price Warning (if applicable)
            if (draft.isUnusualPrice) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, StatusAlertRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = StatusAlertRed
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.get("unusual_price_warn", currentLanguage),
                            fontSize = 12.sp,
                            color = StatusAlertRed,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CTA: Find Recyclers
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("step4_find_recyclers_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    text = when (currentLanguage) {
                        Language.HINDI -> "अधिकृत रिसाइक्लर चुनें ➔"
                        Language.MARATHI -> "अधिकृत रिसायकलर निवडा ➔"
                        Language.ENGLISH -> "Select Recycler ➔"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TrendBarItem(
    month: String,
    rate: Int,
    heightDp: Int,
    isHighlight: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        Text(
            text = "₹$rate",
            fontSize = 11.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) GreenPrimary else Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(heightDp.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(if (isHighlight) GreenPrimary else Color(0xFFCFD8DC))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = month,
            fontSize = 11.sp,
            color = if (isHighlight) GreenPrimaryDark else Color.DarkGray,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ------------------- STEP 5: RECOMMEND & SELECT RECYCLER -------------------
@Composable
private fun Step5SelectRecycler(
    draft: LotCreationDraft,
    allRecyclers: List<RecyclerEntity>,
    currentLanguage: Language,
    onSelectRecycler: (RecyclerEntity) -> Unit
) {
    val rankedRecyclers = remember(allRecyclers, draft.material) {
        AiClassifierHelper.rankRecyclers(allRecyclers, draft.material, currentLanguage)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Authorized Recycler Dhoondein",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Text(
                text = "Ranked by Government Authorization, Material Match, Price & Distance:",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(rankedRecyclers.size) { index ->
            val scoreItem = rankedRecyclers[index]
            val recycler = scoreItem.recycler

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(
                    width = if (scoreItem.isBestMatch) 2.dp else 1.dp,
                    color = if (scoreItem.isBestMatch) GreenPrimary else OutlineWarm
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("recycler_recommend_card_${recycler.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Rank & Authorization Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (scoreItem.isBestMatch) AmberContainer else GreenContainer
                        ) {
                            Text(
                                text = scoreItem.rankBadge,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (scoreItem.isBestMatch) SaffronAccent else GreenPrimaryDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (recycler.isAuthorized) GreenContainer else Color(0xFFFFEBEE)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (recycler.isAuthorized) Icons.Default.Verified else Icons.Default.Warning,
                                    contentDescription = "Auth",
                                    tint = if (recycler.isAuthorized) StatusAuthorizedGreen else StatusAlertRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (recycler.isAuthorized) "🟢 Authorized" else "⚠️ Unverified",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (recycler.isAuthorized) StatusAuthorizedGreen else StatusAlertRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = recycler.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "📍 ${recycler.distanceKm} km away • ⭐ ${recycler.rating} (${recycler.reviewsCount} reviews)",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Match Reasons Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        scoreItem.matchReasons.take(3).forEach { reason ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFF1F6F1)
                            ) {
                                Text(
                                    text = "✓ $reason",
                                    fontSize = 11.sp,
                                    color = GreenPrimaryDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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
                                text = if (recycler.pickupAvailable) "Pickup Available" else "Drop-off Only",
                                fontSize = 12.sp,
                                color = if (recycler.pickupAvailable) GreenPrimary else Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "Rate: ₹${recycler.offeredRatePerKg}/kg",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onSelectRecycler(recycler) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text(
                            text = AppStrings.get("select_this_recycler", currentLanguage),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ------------------- STEP 6: LOT SUMMARY & CONFIRMATION -------------------
@Composable
private fun Step6LotSummary(
    draft: LotCreationDraft,
    currentLanguage: Language,
    onConfirm: () -> Unit
) {
    val recycler = draft.selectedRecycler

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Your Lot Summary (लॉट विवरण)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.5.dp, GreenPrimary.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Draft Lot ID:",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = draft.createdLotId,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = GreenPrimaryDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = OutlineWarm)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Material & Weight
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = GreenContainer,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = draft.material.iconEmoji, fontSize = 28.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = draft.material.getLocalizedName(currentLanguage),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                            Text(
                                text = "${draft.subCategory} • ${draft.condition} condition",
                                fontSize = 13.sp,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "Weight: ${draft.weightKg.toInt()} kg",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estimated Value
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AmberContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Value:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF5D2E00)
                            )
                            Text(
                                text = "₹${draft.estimatedMin} – ₹${draft.estimatedMax}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = SaffronAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selected Recycler
                    Text(
                        text = "Selected Recycler:",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recycler?.name ?: "XYZ E-Waste Recycling",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "🟢 Authorized • Pickup: ${if (recycler?.pickupAvailable != false) "Available" else "Not Available"}",
                        fontSize = 12.sp,
                        color = StatusAuthorizedGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm CTA
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("confirm_lot_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${AppStrings.get("confirm_lot", currentLanguage)} 🚀",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ------------------- STEP 7: DIGITAL HANDOVER & RECEIPT -------------------
@Composable
private fun Step7HandoverAndReceipt(
    draft: LotCreationDraft,
    currentLanguage: Language,
    onUpdatePayment: (String, String) -> Unit,
    onDone: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf(draft.paymentMethod) }
    var selectedPaymentStatus by remember { mutableStateOf(draft.paymentStatus) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item {
            // Success Banner
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = StatusAuthorizedGreen,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Lot Created & Verified!",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimaryDark
                        )
                        Text(
                            text = "Lot ID: ${draft.createdLotId}",
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Handover Timeline
            Text(
                text = "Traceability Timeline (हैंडओवर टाइमलाइन)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimaryDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            TraceabilityTimelineView()

            Spacer(modifier = Modifier.height(18.dp))

            // Payment Mode Section (CRITICAL: Support Cash, No forced digital!)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, OutlineWarm),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = AppStrings.get("payment_method", currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = GreenPrimaryDark
                    )
                    Text(
                        text = "Cash is always supported. Digital payment is optional.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Cash Option
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPaymentMethod == "Cash") GreenContainer else Color.White,
                            border = BorderStroke(1.dp, if (selectedPaymentMethod == "Cash") GreenPrimary else OutlineWarm),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedPaymentMethod = "Cash"
                                    onUpdatePayment("Cash", selectedPaymentStatus)
                                }
                                .testTag("payment_cash_choice")
                        ) {
                            Text(
                                text = AppStrings.get("cash", currentLanguage),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (selectedPaymentMethod == "Cash") GreenPrimaryDark else Color.DarkGray,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        // Digital Option
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedPaymentMethod == "Digital") GreenContainer else Color.White,
                            border = BorderStroke(1.dp, if (selectedPaymentMethod == "Digital") GreenPrimary else OutlineWarm),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedPaymentMethod = "Digital"
                                    onUpdatePayment("Digital", selectedPaymentStatus)
                                }
                                .testTag("payment_digital_choice")
                        ) {
                            Text(
                                text = AppStrings.get("digital", currentLanguage),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = if (selectedPaymentMethod == "Digital") GreenPrimaryDark else Color.DarkGray,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Payment Status Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Payment Status:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedPaymentStatus == "Paid") StatusAuthorizedGreen else Color.LightGray,
                                modifier = Modifier.clickable {
                                    selectedPaymentStatus = "Paid"
                                    onUpdatePayment(selectedPaymentMethod, "Paid")
                                }
                            ) {
                                Text(
                                    text = "✓ Paid",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedPaymentStatus == "Pending") SaffronAccent else Color.LightGray,
                                modifier = Modifier.clickable {
                                    selectedPaymentStatus = "Pending"
                                    onUpdatePayment(selectedPaymentMethod, "Pending")
                                }
                            ) {
                                Text(
                                    text = "⏳ Pending",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Digital Handover Receipt Card
            val lotEntity = com.example.data.local.LotEntity(
                lotId = draft.createdLotId,
                materialCategoryId = draft.material.id,
                subCategory = draft.subCategory,
                approximateWeightKg = draft.weightKg,
                condition = draft.condition,
                sourceType = draft.sourceType,
                quotedRatePerKg = draft.quotedRatePerKg,
                estimatedMinPrice = draft.estimatedMin,
                estimatedMaxPrice = draft.estimatedMax,
                finalPrice = (draft.estimatedMin + draft.estimatedMax) / 2,
                recyclerId = draft.selectedRecycler?.id ?: "rec_xyz",
                recyclerName = draft.selectedRecycler?.name ?: "XYZ E-Waste Recycling",
                status = "CONFIRMED",
                paymentMethod = selectedPaymentMethod,
                paymentStatus = selectedPaymentStatus,
                handoverReference = draft.handoverRef.ifEmpty { "HR-99210" }
            )

            DigitalReceiptCard(
                lot = lotEntity,
                currentLanguage = currentLanguage,
                onDismiss = onDone
            )
        }
    }
}

@Composable
fun TraceabilityTimelineView() {
    val steps = listOf(
        "📷 Material Collected",
        "🏷️ Lot Created",
        "♻️ Recycler Selected",
        "🤝 Handed Over to Recycler",
        "✅ Recycler Verified",
        "💵 Payment Settled",
        "♻️ Formal Eco-Recycling"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineWarm),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            steps.forEachIndexed { index, stepName ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (index <= 4) GreenPrimary else Color(0xFFCFD8DC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stepName,
                        fontSize = 13.sp,
                        fontWeight = if (index <= 4) FontWeight.Bold else FontWeight.Normal,
                        color = if (index <= 4) GreenPrimaryDark else Color.Gray
                    )
                }
            }
        }
    }
}
