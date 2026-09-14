package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AppRole
import com.example.data.model.Language
import com.example.ui.AppScreen
import com.example.ui.KabadiWalaViewModel
import com.example.ui.components.KabadiWalaBottomNav
import com.example.ui.components.KabadiWalaTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CollectorHomeScreen
import com.example.ui.screens.CreateLotScreen
import com.example.ui.screens.EarningsScreen
import com.example.ui.screens.FindRecyclersScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RatesBoardScreen
import com.example.ui.screens.RecyclerDashboardScreen
import com.example.ui.screens.SafetyCenterScreen
import com.example.ui.screens.TransactionsHistoryScreen
import com.example.ui.screens.VoiceBotScreen
import com.example.ui.screens.RoleSelectionScreen
import com.example.ui.screens.LoginPhoneScreen
import com.example.ui.screens.VerifyOtpScreen
import com.example.ui.screens.UserDetailsScreen
import com.example.ui.screens.SelectMaterialCategoryScreen
import com.example.ui.screens.AddPhotosScreen
import com.example.ui.screens.AiMaterialCategorizerScreen
import com.example.ui.screens.PriceAndTrendsScreen
import com.example.ui.screens.SelectRecyclerLotScreen
import com.example.ui.screens.LotConfirmedScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KabadiWalaApp()
            }
        }
    }
}

@Composable
fun KabadiWalaApp(viewModel: KabadiWalaViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()

    val stats by viewModel.dashboardStats.collectAsState()
    val allLots by viewModel.allLots.collectAsState()
    val allRecyclers by viewModel.allRecyclers.collectAsState()
    val allRates by viewModel.allRates.collectAsState()
    val allSafetyTips by viewModel.allSafetyTips.collectAsState()

    val creationDraft by viewModel.creationDraft.collectAsState()
    val selectedLot by viewModel.selectedLot.collectAsState()

    // Auth & Flow states
    val userPhoneNumber by viewModel.userPhoneNumber.collectAsState()
    val userOtp by viewModel.userOtp.collectAsState()
    val userFirstName by viewModel.userFirstName.collectAsState()
    val userLastName by viewModel.userLastName.collectAsState()
    val selectedRecyclerIndex by viewModel.selectedRecyclerOptionIndex.collectAsState()
    val confirmedLotId by viewModel.confirmedLotId.collectAsState()

    // Sahayak Voice Bot states
    val sahayakMessages by viewModel.sahayakMessages.collectAsState()
    val sahayakDraft by viewModel.sahayakDraft.collectAsState()
    val sahayakLanguage by viewModel.sahayakLanguage.collectAsState()
    val isSahayakThinking by viewModel.isSahayakThinking.collectAsState()
    val isSahayakSpeaking by viewModel.isSahayakSpeaking.collectAsState()

    // Handle back button behavior
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        when (currentScreen) {
            AppScreen.ROLE_SELECT -> viewModel.navigateTo(AppScreen.LANGUAGE_SELECT)
            AppScreen.LOGIN_PHONE -> viewModel.navigateTo(AppScreen.ROLE_SELECT)
            AppScreen.LOGIN_OTP -> viewModel.navigateTo(AppScreen.LOGIN_PHONE)
            AppScreen.LOGIN_DETAILS -> viewModel.navigateTo(AppScreen.LOGIN_OTP)
            AppScreen.SELL_CATEGORY -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.SELL_PHOTOS -> viewModel.navigateTo(AppScreen.SELL_CATEGORY)
            AppScreen.SELL_AI_CATEGORIZER -> viewModel.navigateTo(AppScreen.SELL_PHOTOS)
            AppScreen.SELL_PRICE_TRENDS -> viewModel.navigateTo(AppScreen.SELL_AI_CATEGORIZER)
            AppScreen.SELL_RECYCLER -> viewModel.navigateTo(AppScreen.SELL_PRICE_TRENDS)
            AppScreen.SELL_CONFIRMED -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.CREATE_LOT -> {
                if (creationDraft.step > 1 && creationDraft.step < 7) {
                    viewModel.setDraftStep(creationDraft.step - 1)
                } else {
                    viewModel.navigateTo(AppScreen.HOME)
                }
            }
            AppScreen.VOICE_BOT -> {
                viewModel.stopSahayakSpeaking()
                viewModel.navigateTo(AppScreen.HOME)
            }
            AppScreen.RECYCLER_DASHBOARD, AppScreen.ADMIN_DASHBOARD -> {
                viewModel.setRole(AppRole.COLLECTOR)
            }
            else -> viewModel.navigateTo(AppScreen.HOME)
        }
    }

    val showChrome = currentScreen == AppScreen.HOME ||
            currentScreen == AppScreen.RATES_BOARD ||
            currentScreen == AppScreen.FIND_RECYCLERS ||
            currentScreen == AppScreen.HISTORY ||
            currentScreen == AppScreen.SAFETY ||
            currentScreen == AppScreen.EARNINGS ||
            currentScreen == AppScreen.PROFILE ||
            currentScreen == AppScreen.RECYCLER_DASHBOARD ||
            currentScreen == AppScreen.ADMIN_DASHBOARD

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showChrome) {
                KabadiWalaTopBar(
                    currentScreen = currentScreen,
                    currentLanguage = currentLanguage,
                    currentRole = currentRole,
                    isOffline = isOffline,
                    isSyncing = isSyncing,
                    onLanguageSelect = { viewModel.setLanguage(it) },
                    onRoleSelect = { viewModel.setRole(it) },
                    onToggleOffline = { viewModel.toggleOffline() },
                    onProfileClick = { viewModel.navigateTo(AppScreen.PROFILE) }
                )
            }
        },
        bottomBar = {
            if (showChrome && currentRole == AppRole.COLLECTOR && currentScreen != AppScreen.CREATE_LOT && currentScreen != AppScreen.VOICE_BOT) {
                KabadiWalaBottomNav(
                    currentScreen = currentScreen,
                    currentLanguage = currentLanguage,
                    onNavigate = { viewModel.navigateTo(it) },
                    onNewLotClick = { viewModel.startSellFlow() }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.LANGUAGE_SELECT -> {
                    LanguageSelectionScreen(
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) },
                        onContinue = { viewModel.navigateTo(AppScreen.ROLE_SELECT) }
                    )
                }

                AppScreen.ROLE_SELECT -> {
                    RoleSelectionScreen(
                        currentLanguage = currentLanguage,
                        onRoleSelected = { role ->
                            viewModel.setUserRoleChoice(role)
                            viewModel.navigateTo(AppScreen.LOGIN_PHONE)
                        }
                    )
                }

                AppScreen.LOGIN_PHONE -> {
                    LoginPhoneScreen(
                        currentLanguage = currentLanguage,
                        initialPhoneNumber = userPhoneNumber,
                        onBack = { viewModel.navigateTo(AppScreen.ROLE_SELECT) },
                        onContinue = { phone ->
                            viewModel.setPhoneNumber(phone)
                            viewModel.navigateTo(AppScreen.LOGIN_OTP)
                        }
                    )
                }

                AppScreen.LOGIN_OTP -> {
                    VerifyOtpScreen(
                        currentLanguage = currentLanguage,
                        phoneNumber = userPhoneNumber,
                        initialOtp = userOtp,
                        onBack = { viewModel.navigateTo(AppScreen.LOGIN_PHONE) },
                        onVerify = { otp ->
                            viewModel.setOtp(otp)
                            viewModel.navigateTo(AppScreen.LOGIN_DETAILS)
                        }
                    )
                }

                AppScreen.LOGIN_DETAILS -> {
                    UserDetailsScreen(
                        currentLanguage = currentLanguage,
                        initialFirstName = userFirstName,
                        initialLastName = userLastName,
                        onBack = { viewModel.navigateTo(AppScreen.LOGIN_OTP) },
                        onContinue = { first, last ->
                            viewModel.setName(first, last)
                            viewModel.navigateTo(AppScreen.HOME)
                        }
                    )
                }

                AppScreen.SELL_CATEGORY -> {
                    SelectMaterialCategoryScreen(
                        currentLanguage = currentLanguage,
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onCategorySelected = { cat ->
                            viewModel.selectCategoryForSell(cat)
                        }
                    )
                }

                AppScreen.SELL_PHOTOS -> {
                    AddPhotosScreen(
                        currentLanguage = currentLanguage,
                        category = creationDraft.material,
                        onBack = { viewModel.navigateTo(AppScreen.SELL_CATEGORY) },
                        onPhotoCaptured = { bitmap ->
                            viewModel.photoAddedForSell(bitmap)
                        }
                    )
                }

                AppScreen.SELL_AI_CATEGORIZER -> {
                    AiMaterialCategorizerScreen(
                        currentLanguage = currentLanguage,
                        category = creationDraft.material,
                        capturedBitmap = creationDraft.photoBitmap,
                        confidencePercent = creationDraft.aiConfidence,
                        onBack = { viewModel.navigateTo(AppScreen.SELL_PHOTOS) },
                        onConfirm = {
                            viewModel.confirmAiCategoryForSell()
                        }
                    )
                }

                AppScreen.SELL_PRICE_TRENDS -> {
                    PriceAndTrendsScreen(
                        currentLanguage = currentLanguage,
                        category = creationDraft.material,
                        initialWeightKg = creationDraft.weightKg,
                        ratePerKg = creationDraft.quotedRatePerKg,
                        onBack = { viewModel.navigateTo(AppScreen.SELL_AI_CATEGORIZER) },
                        onNext = {
                            viewModel.proceedToSelectRecycler()
                        }
                    )
                }

                AppScreen.SELL_RECYCLER -> {
                    SelectRecyclerLotScreen(
                        currentLanguage = currentLanguage,
                        selectedIndex = selectedRecyclerIndex,
                        onBack = { viewModel.navigateTo(AppScreen.SELL_PRICE_TRENDS) },
                        onProceed = { idx, paymentMethod ->
                            viewModel.setSelectedRecyclerOption(idx)
                            viewModel.confirmRecyclerAndFinalizeLot(paymentMethod)
                        }
                    )
                }

                AppScreen.SELL_CONFIRMED -> {
                    LotConfirmedScreen(
                        currentLanguage = currentLanguage,
                        lotId = confirmedLotId,
                        categoryName = if (creationDraft.material == com.example.data.model.MaterialCategory.PCB) "PCBs" else creationDraft.material.nameEn,
                        weightKg = creationDraft.weightKg,
                        estimatedPrice = (creationDraft.weightKg * creationDraft.quotedRatePerKg).toInt(),
                        recyclerName = when (selectedRecyclerIndex) {
                            0 -> "GreenCycle Recyclers"
                            1 -> "EcoReclaim Ltd."
                            else -> "ReGen Resources"
                        },
                        paymentMethod = "UPI",
                        onBack = { viewModel.navigateTo(AppScreen.HOME) },
                        onViewHistory = { viewModel.navigateTo(AppScreen.HISTORY) }
                    )
                }

                AppScreen.ONBOARDING -> {
                    OnboardingScreen(
                        currentLanguage = currentLanguage,
                        onFinish = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.HOME -> {
                    CollectorHomeScreen(
                        userFirstName = userFirstName,
                        stats = stats,
                        recentLots = allLots,
                        currentLanguage = currentLanguage,
                        onNewLotClick = { viewModel.startSellFlow() },
                        onVoiceBotClick = { viewModel.startSahayakSession() },
                        onSelectCategory = { cat -> viewModel.selectCategoryForSell(cat) },
                        onNavigate = { viewModel.navigateTo(it) },
                        onLotClick = { viewModel.selectLotForDetail(it) },
                        onSpeakGreeting = {
                            val greetingText = when (currentLanguage) {
                                Language.HINDI -> "नमस्ते! कबाड़ीवाला ऐप में आपका स्वागत है। नया लॉट बनाएं या आज के रेट्स देखें।"
                                Language.MARATHI -> "नमस्ते! कबाडीवाला ॲपमध्ये आपले स्वागत आहे. नवीन लॉट तयार करा किंवा आजचे दर पहा."
                                Language.ENGLISH -> "Welcome to KabadiWala! Sell your electronic scrap directly to authorized recyclers."
                            }
                            viewModel.speak(greetingText)
                        }
                    )
                }

                AppScreen.VOICE_BOT -> {
                    VoiceBotScreen(
                        messages = sahayakMessages,
                        draft = sahayakDraft,
                        sahayakLanguage = sahayakLanguage,
                        isThinking = isSahayakThinking,
                        isSpeaking = isSahayakSpeaking,
                        onSendMessage = { viewModel.sendSahayakMessage(it) },
                        onSelectLanguage = { viewModel.setSahayakLanguage(it) },
                        onConfirmLot = { viewModel.confirmSahayakLot() },
                        onResetConversation = { viewModel.resetSahayakConversation() },
                        onStopSpeaking = { viewModel.stopSahayakSpeaking() },
                        onSpeakText = { viewModel.speak(it) },
                        onNavigateBack = {
                            viewModel.stopSahayakSpeaking()
                            viewModel.navigateTo(AppScreen.HOME)
                        },
                        onNavigateHome = {
                            viewModel.stopSahayakSpeaking()
                            viewModel.navigateTo(AppScreen.HOME)
                        }
                    )
                }

                AppScreen.CREATE_LOT -> {
                    CreateLotScreen(
                        draft = creationDraft,
                        allRecyclers = allRecyclers,
                        currentLanguage = currentLanguage,
                        onStepChange = { viewModel.setDraftStep(it) },
                        onAttachPhoto = { viewModel.attachPhoto("scrap_photo") },
                        onAttachPhotoBitmap = { bitmap, uri -> viewModel.attachPhotoBitmap(bitmap, uri) },
                        onRunAiClassification = { bitmap -> viewModel.runAiClassification(bitmap) },
                        onSelectMaterial = { viewModel.updateMaterialCategory(it) },
                        onSelectSubCategory = { viewModel.updateSubCategory(it) },
                        onUpdateWeight = { viewModel.updateWeight(it) },
                        onUpdateCondition = { viewModel.updateCondition(it) },
                        onUpdateSource = { viewModel.updateSource(it) },
                        onUpdateNotes = { viewModel.updateDescription(it) },
                        onSelectRecycler = { viewModel.selectRecyclerForDraft(it) },
                        onConfirmLot = { viewModel.confirmLotCreation() },
                        onCompleteHandoverPayment = { method, status ->
                            viewModel.completeHandoverPayment(method, status)
                        },
                        onSpeakPrice = { viewModel.speakPriceEstimation() },
                        onSpeakText = { viewModel.speak(it) },
                        onDone = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }

                AppScreen.RATES_BOARD -> {
                    RatesBoardScreen(
                        rates = allRates,
                        currentLanguage = currentLanguage,
                        onSpeakRates = { viewModel.speakTodayRates() }
                    )
                }

                AppScreen.FIND_RECYCLERS -> {
                    FindRecyclersScreen(
                        recyclers = allRecyclers,
                        currentLanguage = currentLanguage,
                        onRecyclerSelected = { viewModel.selectRecyclerForDetail(it) }
                    )
                }

                AppScreen.HISTORY -> {
                    TransactionsHistoryScreen(
                        lots = allLots,
                        currentLanguage = currentLanguage,
                        selectedLot = selectedLot,
                        onSelectLot = { viewModel.selectLotForDetail(it) }
                    )
                }

                AppScreen.SAFETY -> {
                    SafetyCenterScreen(
                        tips = allSafetyTips,
                        currentLanguage = currentLanguage,
                        onSpeakTip = { text -> viewModel.speak(text) }
                    )
                }

                AppScreen.EARNINGS -> {
                    EarningsScreen(
                        stats = stats,
                        currentLanguage = currentLanguage
                    )
                }

                AppScreen.PROFILE -> {
                    ProfileScreen(
                        userFirstName = userFirstName,
                        userLastName = userLastName,
                        currentLanguage = currentLanguage,
                        currentRole = currentRole,
                        isOffline = isOffline,
                        isSyncing = isSyncing,
                        onLanguageChange = { viewModel.setLanguage(it) },
                        onRoleChange = { viewModel.setRole(it) },
                        onToggleOffline = { viewModel.toggleOffline() },
                        onSyncNow = { viewModel.syncData() }
                    )
                }

                AppScreen.RECYCLER_DASHBOARD -> {
                    RecyclerDashboardScreen(
                        collectorName = if (userFirstName.isNotBlank()) "$userFirstName $userLastName".trim() else "Ramesh",
                        incomingLots = allLots,
                        currentLanguage = currentLanguage,
                        onConfirmHandover = { lotId, weight, price, method, status ->
                            viewModel.completeHandoverPayment(method, status)
                        },
                        onBackToCollector = { viewModel.setRole(AppRole.COLLECTOR) }
                    )
                }

                AppScreen.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        onBackToCollector = { viewModel.setRole(AppRole.COLLECTOR) }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}
