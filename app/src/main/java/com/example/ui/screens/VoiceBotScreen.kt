package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.service.SahayakLotDraft
import com.example.data.service.SahayakMessage
import com.example.data.service.SahayakSender
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.BackgroundWarm
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenOnContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.GreenPrimaryDark
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.StatusAuthorizedGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMedium

@Composable
fun VoiceBotScreen(
    messages: List<SahayakMessage>,
    draft: SahayakLotDraft,
    sahayakLanguage: Language,
    isThinking: Boolean,
    isSpeaking: Boolean,
    onSendMessage: (String) -> Unit,
    onSelectLanguage: (Language) -> Unit,
    onConfirmLot: () -> Unit,
    onResetConversation: () -> Unit,
    onStopSpeaking: () -> Unit,
    onSpeakText: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Speech Recognizer Launcher for native voice input
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                onSendMessage(spokenText)
            }
        }
    }

    fun launchSpeechInput() {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    if (sahayakLanguage == Language.HINDI) "hi-IN" else "en-IN"
                )
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    if (sahayakLanguage == Language.HINDI) "बोलिए (जैसे: 15 किलो सर्किट बोर्ड)..." else "Speak now (e.g. 15 kg Circuit Boards)..."
                )
            }
            speechRecognizerLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                if (sahayakLanguage == Language.HINDI) "आवाज़ पहचान समर्थित नहीं है, नीचे टाइप करें" else "Voice input not available, please type below",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Auto-scroll to latest message
    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Pulse animation for mic and live assistant indicator
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWarm)
            .navigationBarsPadding()
            .imePadding()
    ) {
        // 1. Top Header Bar with Language Switcher & Controls
        Surface(
            color = GreenPrimaryDark,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("voice_bot_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Sahayak",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                // Live pulse dot
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isSpeaking) GoldYellow else Color(0xFF69F0AE))
                                )
                            }
                            Text(
                                text = if (isSpeaking) {
                                    if (sahayakLanguage == Language.HINDI) "बोल रहा है... 🔊" else "Speaking... 🔊"
                                } else if (isThinking) {
                                    if (sahayakLanguage == Language.HINDI) "सोच रहा है... 💭" else "Analyzing... 💭"
                                } else {
                                    "Sell by Speaking"
                                },
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // Right Side: Retry option as requested by user
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onResetConversation() }
                                .testTag("voice_bot_retry_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Retry",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = when (sahayakLanguage) {
                                        Language.HINDI -> "रीट्राय"
                                        Language.MARATHI -> "पुन्हा प्रयत्न"
                                        Language.ENGLISH -> "Retry"
                                    },
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Stop Audio if speaking
                        if (isSpeaking) {
                            IconButton(
                                onClick = onStopSpeaking,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Sticky Live Lot Progress Card
        LiveLotStatusCard(
            draft = draft,
            language = sahayakLanguage,
            onConfirmClick = onConfirmLot
        )

        // 3. Conversation Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    language = sahayakLanguage,
                    onSpeakClick = { onSpeakText(message.text) },
                    onConfirmLot = onConfirmLot,
                    onGoHome = onNavigateHome,
                    onResetConversation = onResetConversation
                )
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = GreenPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (sahayakLanguage == Language.HINDI) "सहायक सोच रहा है..." else "Sahayak is thinking...",
                            fontSize = 13.sp,
                            color = TextMedium
                        )
                    }
                }
            }
        }

        // 4. Quick Reply Suggestions Row
        val latestBotMsg = messages.lastOrNull { it.sender == SahayakSender.SAHAYAK }
        val quickReplies = latestBotMsg?.quickReplies.orEmpty()
        if (quickReplies.isNotEmpty() && !draft.isConfirmed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickReplies.forEach { reply ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = AmberContainer,
                        border = BorderStroke(1.dp, AmberAccent.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clickable {
                                if (reply.contains("लॉट बना") || reply.contains("Create Lot") || reply.contains("Confirm")) {
                                    onConfirmLot()
                                } else {
                                    onSendMessage(reply)
                                }
                            }
                            .testTag("quick_reply_${reply.take(6)}")
                    ) {
                        Text(
                            text = reply,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SaffronAccent,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // 5. Bottom Voice & Text Input Bar
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Large Mic Button with glowing pulse
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .scale(if (isSpeaking) pulseScale else 1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSpeaking) GoldYellow else GreenPrimary,
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { launchSpeechInput() }
                                .testTag("sahayak_mic_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Speak to Sahayak",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Text Field for typing alternative
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = if (sahayakLanguage == Language.HINDI) "माइक दबाकर बोलें या लिखें..." else "Tap mic to speak or type...",
                                fontSize = 14.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFD6E2D5),
                            focusedContainerColor = Color(0xFFF8FAF7),
                            unfocusedContainerColor = Color(0xFFF8FAF7)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (inputText.isNotBlank()) {
                                    onSendMessage(inputText)
                                    inputText = ""
                                }
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sahayak_text_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Send Button
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (inputText.isNotBlank()) GreenPrimary else Color(0xFFE0E0E0),
                                CircleShape
                            )
                            .testTag("sahayak_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveLotStatusCard(
    draft: SahayakLotDraft,
    language: Language,
    onConfirmClick: () -> Unit
) {
    AnimatedVisibility(visible = draft.material != null || draft.weightKg > 0) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (draft.isConfirmed) GreenContainer else Color(0xFFF1F8E9)
            ),
            border = BorderStroke(
                1.dp,
                if (draft.isConfirmed) StatusAuthorizedGreen else GreenPrimary.copy(alpha = 0.3f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (draft.isConfirmed) "✅ लॉट कन्फर्म हो गया" else "📋 वर्तमान लॉट ड्राफ्ट (Live Lot Draft)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (draft.isConfirmed) StatusAuthorizedGreen else GreenPrimaryDark
                        )
                    }

                    if (draft.isConfirmed) {
                        Surface(
                            color = StatusAuthorizedGreen,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = draft.confirmedLotId,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Material
                    Column {
                        Text(
                            text = if (language == Language.HINDI) "सामान (Material)" else "Material",
                            fontSize = 11.sp,
                            color = TextMedium
                        )
                        Text(
                            text = draft.material?.let {
                                if (language == Language.HINDI) it.nameHi else it.nameEn
                            } ?: "—",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    // Weight
                    Column {
                        Text(
                            text = if (language == Language.HINDI) "वजन (Weight)" else "Weight",
                            fontSize = 11.sp,
                            color = TextMedium
                        )
                        Text(
                            text = if (draft.weightKg > 0) "${draft.weightKg} kg" else if (language == Language.HINDI) "पूछना बाकी" else "Pending",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    // Estimated Price
                    Column {
                        Text(
                            text = if (language == Language.HINDI) "अनुमानित भाव" else "Est. Payout",
                            fontSize = 11.sp,
                            color = TextMedium
                        )
                        Text(
                            text = if (draft.estimatedMinPrice > 0) "₹${draft.estimatedMinPrice.toInt()} - ₹${draft.estimatedMaxPrice.toInt()}" else "—",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusAuthorizedGreen
                        )
                    }
                }

                // If ready to confirm and not yet confirmed, show instant action button
                if (draft.isReadyToConfirm && !draft.isConfirmed) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onConfirmClick,
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("confirm_lot_live_card_btn")
                    ) {
                        Text(
                            text = if (language == Language.HINDI) "✅ लॉट फाइनल करें (Confirm & Create Lot)" else "✅ Confirm & Create Lot Now",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: SahayakMessage,
    language: Language,
    onSpeakClick: () -> Unit,
    onConfirmLot: () -> Unit,
    onGoHome: () -> Unit,
    onResetConversation: () -> Unit
) {
    val isBot = message.sender == SahayakSender.SAHAYAK

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isBot) Arrangement.Start else Arrangement.End
    ) {
        if (isBot) {
            // Sahayak Bot Avatar
            Surface(
                shape = CircleShape,
                color = GreenPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "🤖",
                        fontSize = 18.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isBot) Alignment.Start else Alignment.End
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isBot) 4.dp else 16.dp,
                    bottomEnd = if (isBot) 16.dp else 4.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isBot) Color.White else GreenContainer
                ),
                border = BorderStroke(
                    1.dp,
                    if (isBot) Color(0xFFE0E0E0) else GreenPrimary.copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.testTag("chat_bubble_${message.id.take(4)}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 20.sp
                    )

                    // Audio speaker button for bot messages
                    if (isBot) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onSpeakClick() }
                                .padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Listen to message",
                                tint = SaffronAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.HINDI) "सुनें (Listen)" else "Listen",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SaffronAccent
                            )
                        }
                    }
                }
            }

            // Confirmed Lot Receipt inside chat if lot was just created
            if (message.isLotCreated && message.lotPreview != null) {
                Spacer(modifier = Modifier.height(8.dp))
                ConfirmedLotReceiptBubble(
                    draft = message.lotPreview,
                    language = language,
                    onGoHome = onGoHome,
                    onNewLot = onResetConversation
                )
            }
        }
    }
}

@Composable
fun ConfirmedLotReceiptBubble(
    draft: SahayakLotDraft,
    language: Language,
    onGoHome: () -> Unit,
    onNewLot: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenContainer),
        border = BorderStroke(1.5.dp, StatusAuthorizedGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("confirmed_lot_bubble_receipt")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = StatusAuthorizedGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == Language.HINDI) "लॉट सफलतापूर्वक दर्ज हुआ!" else "Lot Successfully Created!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimaryDark
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = StatusAuthorizedGreen.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (language == Language.HINDI) "लॉट ट्रैकिंग ID" else "Lot ID",
                        fontSize = 11.sp,
                        color = TextMedium
                    )
                    Text(
                        text = draft.confirmedLotId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimaryDark
                    )
                }

                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "Handover QR",
                    tint = GreenPrimaryDark,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${if (language == Language.HINDI) "अधिकृत रीसाइक्लर: " else "Authorized Recycler: "}${draft.recyclerName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )

            Text(
                text = "${if (language == Language.HINDI) "अनुमानित भुगतान: " else "Est. Payout: "}₹${draft.estimatedMinPrice.toInt()} - ₹${draft.estimatedMaxPrice.toInt()}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StatusAuthorizedGreen
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onGoHome,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (language == Language.HINDI) "होम देखें" else "Go to Home",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onNewLot,
                    colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (language == Language.HINDI) "नया लॉट बनाएं" else "New Lot",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
