package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.LotEntity
import com.example.data.local.PriceRateEntity
import com.example.data.local.RecyclerEntity
import com.example.data.local.SafetyTipEntity
import com.example.data.model.AppRole
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.data.repository.DashboardStats
import com.example.data.repository.KabadiWalaRepository
import com.example.data.service.GeminiVisionService
import com.example.data.service.SahayakChatService
import com.example.data.service.SahayakLotDraft
import com.example.data.service.SahayakMessage
import com.example.data.service.SahayakSender
import com.example.util.AiClassifierHelper
import com.example.util.AppStrings
import com.example.util.RecyclerScore
import com.example.util.TtsHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    LANGUAGE_SELECT,
    ROLE_SELECT,
    LOGIN_PHONE,
    LOGIN_OTP,
    LOGIN_DETAILS,
    ONBOARDING,
    HOME,
    CREATE_LOT,
    VOICE_BOT,
    SELL_CATEGORY,
    SELL_PHOTOS,
    SELL_AI_CATEGORIZER,
    SELL_PRICE_TRENDS,
    SELL_RECYCLER,
    SELL_CONFIRMED,
    RATES_BOARD,
    FIND_RECYCLERS,
    HISTORY,
    SAFETY,
    EARNINGS,
    PROFILE,
    RECYCLER_DASHBOARD,
    ADMIN_DASHBOARD
}

data class LotCreationDraft(
    val step: Int = 1, // 1: Photo, 2: Category, 3: Details, 4: Price, 5: Recyclers, 6: Summary, 7: Receipt
    val photoBitmap: Bitmap? = null,
    val photoDrawableRes: Int? = null,
    val photoUri: String = "",
    val photoSeed: Int = 0,
    val isAnalyzingAi: Boolean = false,
    val hasAiIdentified: Boolean = false,
    val aiConfidence: Int = 87,
    val aiDescription: String = "",
    val aiFeatures: List<String> = emptyList(),
    val aiSafetyWarning: String? = null,
    val isRealAi: Boolean = false,
    val rawModelUsed: String = "",
    val material: MaterialCategory = MaterialCategory.PCB,
    val subCategory: String = "Low-grade PCB",
    val weightKg: Double = 10.0,
    val condition: String = "Used", // Good, Used, Damaged
    val sourceType: String = "Shop", // Household, Shop, Office, Industrial, Other
    val description: String = "",
    val estimatedMin: Int = 3000,
    val estimatedMax: Int = 4000,
    val quotedRatePerKg: Int = 350,
    val isUnusualPrice: Boolean = false,
    val selectedRecycler: RecyclerEntity? = null,
    val createdLotId: String = "",
    val paymentMethod: String = "Cash", // Cash, Digital
    val paymentStatus: String = "Paid",
    val handoverRef: String = ""
)

class KabadiWalaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: KabadiWalaRepository
    private val ttsHelper: TtsHelper = TtsHelper(application)

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = KabadiWalaRepository(db)
        ttsHelper.setSpeechStatusListener { speaking ->
            _isSahayakSpeaking.value = speaking
        }
    }

    // Main navigation and session states
    private val _currentScreen = MutableStateFlow(AppScreen.LANGUAGE_SELECT)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.HINDI)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _currentRole = MutableStateFlow(AppRole.COLLECTOR)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    // User Profile / Auth states matching the flow
    private val _userPhoneNumber = MutableStateFlow("98765 43210")
    val userPhoneNumber: StateFlow<String> = _userPhoneNumber.asStateFlow()

    private val _userOtp = MutableStateFlow("123456")
    val userOtp: StateFlow<String> = _userOtp.asStateFlow()

    private val _userFirstName = MutableStateFlow("")
    val userFirstName: StateFlow<String> = _userFirstName.asStateFlow()

    private val _userLastName = MutableStateFlow("")
    val userLastName: StateFlow<String> = _userLastName.asStateFlow()

    private val _userRoleChoice = MutableStateFlow("Seller") // "Seller" or "Recycler"
    val userRoleChoice: StateFlow<String> = _userRoleChoice.asStateFlow()

    private val _selectedRecyclerOptionIndex = MutableStateFlow(0)
    val selectedRecyclerOptionIndex: StateFlow<Int> = _selectedRecyclerOptionIndex.asStateFlow()

    private val _confirmedLotId = MutableStateFlow("KW202508150001")
    val confirmedLotId: StateFlow<String> = _confirmedLotId.asStateFlow()

    private val _hasCompletedOnboarding = MutableStateFlow(false)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    // Offline mode & Syncing simulation
    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    // Database reactive streams
    val allLots: StateFlow<List<LotEntity>> = repository.allLots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecyclers: StateFlow<List<RecyclerEntity>> = repository.allRecyclers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRates: StateFlow<List<PriceRateEntity>> = repository.allRates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSafetyTips: StateFlow<List<SafetyTipEntity>> = repository.allSafetyTips
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dashboardStats: StateFlow<DashboardStats> = repository.stats
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DashboardStats(2450, 750, 12, 24850, 8450)
        )

    // Active Creation Draft
    private val _creationDraft = MutableStateFlow(LotCreationDraft())
    val creationDraft: StateFlow<LotCreationDraft> = _creationDraft.asStateFlow()

    // Selected items for detail dialogs
    private val _selectedLot = MutableStateFlow<LotEntity?>(null)
    val selectedLot: StateFlow<LotEntity?> = _selectedLot.asStateFlow()

    private val _selectedRecycler = MutableStateFlow<RecyclerEntity?>(null)
    val selectedRecycler: StateFlow<RecyclerEntity?> = _selectedRecycler.asStateFlow()

    // Voice Chatbot (Sahayak) State
    private val _sahayakLanguage = MutableStateFlow(Language.HINDI)
    val sahayakLanguage: StateFlow<Language> = _sahayakLanguage.asStateFlow()

    private val _sahayakMessages = MutableStateFlow<List<SahayakMessage>>(emptyList())
    val sahayakMessages: StateFlow<List<SahayakMessage>> = _sahayakMessages.asStateFlow()

    private val _sahayakDraft = MutableStateFlow(SahayakLotDraft())
    val sahayakDraft: StateFlow<SahayakLotDraft> = _sahayakDraft.asStateFlow()

    private val _isSahayakThinking = MutableStateFlow(false)
    val isSahayakThinking: StateFlow<Boolean> = _isSahayakThinking.asStateFlow()

    private val _isSahayakSpeaking = MutableStateFlow(false)
    val isSahayakSpeaking: StateFlow<Boolean> = _isSahayakSpeaking.asStateFlow()

    // Screen navigation
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
    }

    fun setPhoneNumber(phone: String) {
        _userPhoneNumber.value = phone
    }

    fun setOtp(otp: String) {
        _userOtp.value = otp
    }

    fun setName(firstName: String, lastName: String) {
        _userFirstName.value = firstName
        _userLastName.value = lastName
    }

    fun setUserRoleChoice(role: String) {
        _userRoleChoice.value = role
        if (role == "Recycler") {
            _currentRole.value = AppRole.RECYCLER
        } else {
            _currentRole.value = AppRole.COLLECTOR
        }
    }

    fun setSelectedRecyclerOption(index: Int) {
        _selectedRecyclerOptionIndex.value = index
    }

    fun startSellFlow() {
        val cat = MaterialCategory.PCB
        val weight = 5.0
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(cat, weight, "Used")
        _creationDraft.value = LotCreationDraft(
            step = 1,
            material = cat,
            subCategory = cat.subCategories.first(),
            weightKg = weight,
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate,
            isUnusualPrice = false,
            createdLotId = "KW202508150001"
        )
        // Directly navigate to offline AI material photo capture / recognition
        _currentScreen.value = AppScreen.SELL_PHOTOS
    }

    fun selectCategoryForSell(cat: MaterialCategory) {
        val weight = _creationDraft.value.weightKg.takeIf { it > 0 } ?: 5.0
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(cat, weight, "Used")
        val result = AiClassifierHelper.classifyCategory(cat)
        _creationDraft.value = _creationDraft.value.copy(
            material = cat,
            subCategory = cat.subCategories.first(),
            weightKg = weight,
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate,
            aiConfidence = result.confidencePercent,
            aiDescription = result.description,
            aiFeatures = result.featuresDetected
        )
        _currentScreen.value = AppScreen.SELL_PHOTOS
    }

    fun photoAddedForSell(
        bitmap: Bitmap? = null,
        sampleCategory: MaterialCategory? = null,
        drawableRes: Int? = null
    ) {
        val result = when {
            bitmap != null -> AiClassifierHelper.classifyBitmap(bitmap)
            sampleCategory != null -> AiClassifierHelper.classifyCategory(sampleCategory)
            else -> AiClassifierHelper.classifyPhoto(_creationDraft.value.photoSeed)
        }

        val weight = _creationDraft.value.weightKg.takeIf { it > 0 } ?: 5.0
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(result.category, weight, "Used")

        _creationDraft.value = _creationDraft.value.copy(
            photoBitmap = bitmap,
            photoDrawableRes = drawableRes,
            photoUri = if (bitmap != null) "captured_photo" else "sample_photo",
            photoSeed = _creationDraft.value.photoSeed + 1,
            material = result.category,
            subCategory = result.category.subCategories.firstOrNull() ?: "General",
            aiConfidence = result.confidencePercent,
            aiDescription = result.description,
            aiFeatures = result.featuresDetected,
            hasAiIdentified = true,
            isAnalyzingAi = false,
            quotedRatePerKg = rate,
            estimatedMin = minP,
            estimatedMax = maxP
        )
        _currentScreen.value = AppScreen.SELL_AI_CATEGORIZER
    }

    fun overrideAiCategory(newCat: MaterialCategory) {
        val weight = _creationDraft.value.weightKg.takeIf { it > 0 } ?: 5.0
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(newCat, weight, "Used")
        val result = AiClassifierHelper.classifyCategory(newCat)
        _creationDraft.value = _creationDraft.value.copy(
            material = newCat,
            subCategory = newCat.subCategories.firstOrNull() ?: "General",
            aiConfidence = 95,
            aiDescription = result.description,
            aiFeatures = result.featuresDetected,
            quotedRatePerKg = rate,
            estimatedMin = minP,
            estimatedMax = maxP
        )
    }

    fun confirmAiCategoryForSell() {
        _currentScreen.value = AppScreen.SELL_PRICE_TRENDS
    }

    fun proceedToSelectRecycler() {
        _currentScreen.value = AppScreen.SELL_RECYCLER
    }

    fun confirmRecyclerAndFinalizeLot(paymentMethod: String = "UPI") {
        val draft = _creationDraft.value
        val lotId = "KW202508150001"
        _confirmedLotId.value = lotId
        val recyclerName = when (_selectedRecyclerOptionIndex.value) {
            0 -> "GreenCycle Recyclers"
            1 -> "EcoReclaim Ltd."
            else -> "ReGen Resources"
        }
        val estimatedTotal = (draft.weightKg * draft.quotedRatePerKg).toInt().coerceAtLeast(500)
        val lotEntity = LotEntity(
            lotId = lotId,
            materialCategoryId = draft.material.id,
            subCategory = draft.subCategory,
            approximateWeightKg = draft.weightKg,
            condition = "Used",
            sourceType = "Household",
            description = "AI Categorized E-Waste Lot",
            photoUri = draft.photoUri,
            quotedRatePerKg = draft.quotedRatePerKg,
            estimatedMinPrice = estimatedTotal,
            estimatedMaxPrice = estimatedTotal,
            finalPrice = estimatedTotal,
            recyclerId = "rec_greencycle",
            recyclerName = recyclerName,
            status = "CONFIRMED",
            timestamp = System.currentTimeMillis(),
            isSynced = true,
            handoverReference = "HR-99881",
            paymentMethod = paymentMethod
        )
        viewModelScope.launch {
            repository.insertLot(lotEntity)
        }
        _currentScreen.value = AppScreen.SELL_CONFIRMED
    }

    fun setRole(role: AppRole) {
        _currentRole.value = role
        if (role == AppRole.RECYCLER) {
            _currentScreen.value = AppScreen.RECYCLER_DASHBOARD
        } else if (role == AppRole.ADMIN) {
            _currentScreen.value = AppScreen.ADMIN_DASHBOARD
        } else {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun toggleOffline() {
        _isOffline.value = !_isOffline.value
        if (!_isOffline.value) {
            syncData()
        }
    }

    fun syncData() {
        viewModelScope.launch {
            _isSyncing.value = true
            delay(1200)
            repository.syncAllOfflineData()
            _isSyncing.value = false
        }
    }

    // Lot creation steps
    fun startNewLot() {
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(MaterialCategory.PCB, 10.0, "Used")
        _creationDraft.value = LotCreationDraft(
            step = 1,
            material = MaterialCategory.PCB,
            subCategory = MaterialCategory.PCB.subCategories.first(),
            weightKg = 10.0,
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate,
            isUnusualPrice = false,
            createdLotId = "KW-2026-${(10000..99999).random()}"
        )
        _currentScreen.value = AppScreen.CREATE_LOT
    }

    fun setDraftStep(step: Int) {
        _creationDraft.value = _creationDraft.value.copy(step = step)
    }

    fun attachPhoto(uri: String = "photo_mock") {
        val nextSeed = _creationDraft.value.photoSeed + 1
        _creationDraft.value = _creationDraft.value.copy(
            photoUri = uri,
            photoSeed = nextSeed
        )
    }

    fun attachPhotoBitmap(bitmap: Bitmap?, uri: String = "") {
        val nextSeed = _creationDraft.value.photoSeed + 1
        _creationDraft.value = _creationDraft.value.copy(
            photoBitmap = bitmap,
            photoUri = uri,
            photoSeed = nextSeed
        )
    }

    fun runAiClassification(customBitmap: Bitmap? = null) {
        val targetBitmap = customBitmap ?: _creationDraft.value.photoBitmap
        viewModelScope.launch {
            _creationDraft.value = _creationDraft.value.copy(isAnalyzingAi = true)
            val details = GeminiVisionService.categorizeScrapMaterial(
                bitmap = targetBitmap,
                sampleIndex = _creationDraft.value.photoSeed,
                currentLanguage = _currentLanguage.value
            )
            val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(
                details.category,
                details.estimatedWeightKg.takeIf { it > 0 } ?: _creationDraft.value.weightKg,
                details.condition
            )
            _creationDraft.value = _creationDraft.value.copy(
                isAnalyzingAi = false,
                hasAiIdentified = true,
                material = details.category,
                subCategory = details.subCategory,
                aiConfidence = details.confidencePercent,
                condition = details.condition,
                weightKg = details.estimatedWeightKg.takeIf { it > 0 } ?: _creationDraft.value.weightKg,
                aiFeatures = details.featuresDetected,
                aiDescription = details.description,
                aiSafetyWarning = details.safetyWarning,
                isRealAi = details.isRealAi,
                rawModelUsed = details.rawModelUsed,
                estimatedMin = minP,
                estimatedMax = maxP,
                quotedRatePerKg = rate,
                isUnusualPrice = AiClassifierHelper.checkUnusualPrice(rate, details.category)
            )
        }
    }

    fun updateMaterialCategory(cat: MaterialCategory) {
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(
            cat,
            _creationDraft.value.weightKg,
            _creationDraft.value.condition
        )
        _creationDraft.value = _creationDraft.value.copy(
            material = cat,
            subCategory = cat.subCategories.first(),
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate,
            isUnusualPrice = AiClassifierHelper.checkUnusualPrice(rate, cat)
        )
    }

    fun updateSubCategory(sub: String) {
        _creationDraft.value = _creationDraft.value.copy(subCategory = sub)
    }

    fun updateWeight(weightKg: Double) {
        val safeWeight = weightKg.coerceIn(0.5, 500.0)
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(
            _creationDraft.value.material,
            safeWeight,
            _creationDraft.value.condition
        )
        _creationDraft.value = _creationDraft.value.copy(
            weightKg = safeWeight,
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate
        )
    }

    fun updateCondition(cond: String) {
        val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(
            _creationDraft.value.material,
            _creationDraft.value.weightKg,
            cond
        )
        _creationDraft.value = _creationDraft.value.copy(
            condition = cond,
            estimatedMin = minP,
            estimatedMax = maxP,
            quotedRatePerKg = rate
        )
    }

    fun updateSource(source: String) {
        _creationDraft.value = _creationDraft.value.copy(sourceType = source)
    }

    fun updateDescription(desc: String) {
        _creationDraft.value = _creationDraft.value.copy(description = desc)
    }

    fun selectRecyclerForDraft(recycler: RecyclerEntity) {
        _creationDraft.value = _creationDraft.value.copy(selectedRecycler = recycler)
    }

    fun confirmLotCreation() {
        val draft = _creationDraft.value
        val lotId = draft.createdLotId.ifEmpty { "KW-2026-${(10000..99999).random()}" }
        val recycler = draft.selectedRecycler ?: allRecyclers.value.firstOrNull()
        val recName = recycler?.name ?: "XYZ E-Waste Recycling"
        val recId = recycler?.id ?: "rec_xyz"
        val handoverRef = "HR-${(10000..99999).random()}"

        val lotEntity = LotEntity(
            lotId = lotId,
            materialCategoryId = draft.material.id,
            subCategory = draft.subCategory,
            approximateWeightKg = draft.weightKg,
            condition = draft.condition,
            sourceType = draft.sourceType,
            description = draft.description,
            photoUri = draft.photoUri,
            quotedRatePerKg = draft.quotedRatePerKg,
            estimatedMinPrice = draft.estimatedMin,
            estimatedMaxPrice = draft.estimatedMax,
            finalPrice = (draft.estimatedMin + draft.estimatedMax) / 2,
            recyclerId = recId,
            recyclerName = recName,
            pickupRequired = recycler?.pickupAvailable ?: true,
            status = "HANDOVER_PENDING",
            paymentMethod = draft.paymentMethod,
            paymentStatus = "Pending",
            location = "Indore, MP",
            handoverReference = handoverRef,
            isSynced = !_isOffline.value
        )

        viewModelScope.launch {
            repository.insertLot(lotEntity)
            _creationDraft.value = draft.copy(
                step = 7,
                createdLotId = lotId,
                selectedRecycler = recycler,
                handoverRef = handoverRef
            )
        }
    }

    fun completeHandoverPayment(method: String, status: String) {
        val draft = _creationDraft.value
        val finalP = (draft.estimatedMin + draft.estimatedMax) / 2
        viewModelScope.launch {
            repository.confirmHandover(
                lotId = draft.createdLotId,
                actualWeightKg = draft.weightKg,
                finalPrice = finalP,
                paymentMethod = method,
                paymentStatus = status
            )
            _creationDraft.value = draft.copy(
                paymentMethod = method,
                paymentStatus = status
            )
        }
    }

    fun selectLotForDetail(lot: LotEntity?) {
        _selectedLot.value = lot
    }

    fun selectRecyclerForDetail(rec: RecyclerEntity?) {
        _selectedRecycler.value = rec
    }

    fun speak(text: String) {
        ttsHelper.speak(text, _currentLanguage.value)
    }

    fun speakPriceEstimation() {
        val draft = _creationDraft.value
        val lang = _currentLanguage.value
        val matName = draft.material.getLocalizedName(lang)
        val text = when (lang) {
            Language.HINDI -> "${matName} का अनुमानित मूल्य ₹${draft.estimatedMin} से ₹${draft.estimatedMax} है। वजन ${draft.weightKg.toInt()} किलोग्राम, दर ₹${draft.quotedRatePerKg} प्रति किलो है।"
            Language.MARATHI -> "${matName} चे अंदाजे मूल्य ₹${draft.estimatedMin} ते ₹${draft.estimatedMax} आहे. वजन ${draft.weightKg.toInt()} किलो, दर ₹${draft.quotedRatePerKg} प्रति किलो."
            Language.ENGLISH -> "Estimated value for ${draft.material.nameEn} is between ₹${draft.estimatedMin} and ₹${draft.estimatedMax}. Weight is ${draft.weightKg.toInt()} kilograms at ₹${draft.quotedRatePerKg} per kilogram."
        }
        ttsHelper.speak(text, lang)
    }

    fun speakTodayRates() {
        val rates = allRates.value
        val lang = _currentLanguage.value
        val text = when (lang) {
            Language.HINDI -> "आज के भाव: पीसीबी ₹350 प्रति किलो, कॉपर केबल ₹180 प्रति किलो, बैटरी ₹120 प्रति किलो, मोटर ₹75 प्रति किलो।"
            Language.MARATHI -> "आजचे दर: पीसीबी ₹350 प्रति किलो, कॉपर केबल ₹180 प्रति किलो, बॅटरी ₹120 प्रति किलो, मोटर ₹75 प्रति किलो."
            Language.ENGLISH -> "Today's e-waste rates: PCB is ₹350 per kg, copper cable ₹180 per kg, battery ₹120 per kg, motor ₹75 per kg."
        }
        ttsHelper.speak(text, lang)
    }

    // Voice Chatbot (Sahayak) Operations
    fun startSahayakSession(language: Language? = null) {
        val activeLang = language ?: _currentLanguage.value
        _sahayakLanguage.value = activeLang
        _sahayakDraft.value = SahayakLotDraft()
        val greeting = SahayakChatService.getInitialGreeting(activeLang)
        _sahayakMessages.value = listOf(greeting)
        ttsHelper.speak(greeting.text, activeLang)
        _currentScreen.value = AppScreen.VOICE_BOT
    }

    fun setSahayakLanguage(lang: Language) {
        _sahayakLanguage.value = lang
        val switchNotice = when (lang) {
            Language.HINDI -> "भाषा बदलकर हिंदी कर दी गई है। आप बोल सकते हैं।"
            Language.MARATHI -> "भाषा मराठी केली आहे. आपण बोलू शकता."
            Language.ENGLISH -> "Language switched to English. You can speak now."
        }
        val botMessage = SahayakMessage(
            sender = SahayakSender.SAHAYAK,
            text = switchNotice,
            quickReplies = when (lang) {
                Language.HINDI -> listOf("🖥️ सर्किट बोर्ड (PCB)", "🔌 कॉपर केबल", "🔋 बैटरी", "⚙️ मोटर")
                Language.MARATHI -> listOf("🖥️ सर्किट बोर्ड (PCB)", "🔌 कॉपर केबल", "🔋 बॅटरी", "⚙️ मोटर")
                Language.ENGLISH -> listOf("🖥️ Circuit Board (PCB)", "🔌 Copper Cable", "🔋 Battery", "⚙️ Motor")
            }
        )
        _sahayakMessages.value = _sahayakMessages.value + botMessage
        ttsHelper.speak(switchNotice, lang)
    }

    fun sendSahayakMessage(userText: String) {
        if (userText.isBlank()) return
        val currentLang = _sahayakLanguage.value
        val userMsg = SahayakMessage(
            sender = SahayakSender.USER,
            text = userText
        )
        _sahayakMessages.value = _sahayakMessages.value + userMsg

        viewModelScope.launch {
            _isSahayakThinking.value = true
            val (botMsg, updatedDraft) = SahayakChatService.processUserSpeech(
                userInput = userText,
                currentDraft = _sahayakDraft.value,
                language = currentLang,
                conversationHistory = _sahayakMessages.value
            )
            _sahayakDraft.value = updatedDraft
            _sahayakMessages.value = _sahayakMessages.value + botMsg
            _isSahayakThinking.value = false

            // If the chatbot confirmed lot creation, save it to Room local DB
            if (updatedDraft.isConfirmed && updatedDraft.confirmedLotId.isNotEmpty()) {
                persistSahayakLot(updatedDraft)
            }

            // Speak Sahayak response
            ttsHelper.speak(botMsg.text, currentLang)
        }
    }

    fun confirmSahayakLot() {
        val draft = _sahayakDraft.value
        if (draft.material == null || draft.weightKg <= 0.0) return

        val confirmedId = "KW-2026-${(10000..99999).random()}"
        val finalDraft = draft.copy(
            isConfirmed = true,
            confirmedLotId = confirmedId
        )
        _sahayakDraft.value = finalDraft

        val lang = _sahayakLanguage.value
        val reply = when (lang) {
            Language.HINDI -> "🎉 बहुत बढ़िया! आपका लॉट ($confirmedId) सेव कर दिया गया है। अधिकृत रीसाइक्लर 'EcoRecycle Hub' से संपर्क साधा जा रहा है।"
            Language.MARATHI -> "🎉 छान! तुमचा लॉट ($confirmedId) सेव्ह केला आहे. 'EcoRecycle Hub' कडून संपर्क केला जाईल."
            Language.ENGLISH -> "🎉 Great! Your lot ($confirmedId) has been saved and scheduled with 'EcoRecycle Hub'."
        }

        val botMessage = SahayakMessage(
            sender = SahayakSender.SAHAYAK,
            text = reply,
            quickReplies = listOf(
                if (lang == Language.HINDI) "होम पर जाएं" else "Go to Home",
                if (lang == Language.HINDI) "नया लॉट बनाएं" else "Create Another Lot"
            ),
            lotPreview = finalDraft,
            isLotCreated = true
        )
        _sahayakMessages.value = _sahayakMessages.value + botMessage

        persistSahayakLot(finalDraft)
        ttsHelper.speak(reply, lang)
    }

    private fun persistSahayakLot(draft: SahayakLotDraft) {
        viewModelScope.launch {
            val rec = allRecyclers.value.firstOrNull()
            val recName = rec?.name ?: "EcoRecycle Authorized Hub"
            val recId = rec?.id ?: "rec_01"
            val handoverRef = "HR-${(10000..99999).random()}"

            val lotEntity = LotEntity(
                lotId = draft.confirmedLotId,
                materialCategoryId = draft.material?.id ?: MaterialCategory.PCB.id,
                subCategory = draft.subCategory.ifEmpty { "Standard Grade" },
                approximateWeightKg = draft.weightKg,
                condition = draft.condition,
                sourceType = draft.sourceType,
                description = "Created via Sahayak Voice Assistant",
                photoUri = "",
                quotedRatePerKg = draft.quotedRatePerKg,
                estimatedMinPrice = draft.estimatedMinPrice,
                estimatedMaxPrice = draft.estimatedMaxPrice,
                finalPrice = (draft.estimatedMinPrice + draft.estimatedMaxPrice) / 2,
                recyclerId = recId,
                recyclerName = recName,
                pickupRequired = true,
                status = "HANDOVER_PENDING",
                paymentMethod = "UPI",
                paymentStatus = "Pending",
                location = "Indore, MP",
                handoverReference = handoverRef,
                isSynced = !_isOffline.value
            )
            repository.insertLot(lotEntity)
        }
    }

    fun resetSahayakConversation() {
        startSahayakSession(_sahayakLanguage.value)
    }

    fun stopSahayakSpeaking() {
        ttsHelper.stop()
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper.shutdown()
    }
}
