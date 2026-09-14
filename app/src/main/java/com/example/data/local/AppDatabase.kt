package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LotEntity::class,
        RecyclerEntity::class,
        PriceRateEntity::class,
        SafetyTipEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lotDao(): LotDao
    abstract fun recyclerDao(): RecyclerDao
    abstract fun priceRateDao(): PriceRateDao
    abstract fun safetyTipDao(): SafetyTipDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kabadiwala_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val scope: CoroutineScope) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: AppDatabase) {
            // Seed Recyclers (Indore context)
            val recyclers = listOf(
                RecyclerEntity(
                    id = "rec_xyz",
                    name = "XYZ E-Waste Recycling",
                    isAuthorized = true,
                    authorizationRegNo = "MPPCB/E-WASTE/2024/091",
                    distanceKm = 4.8,
                    materialsAccepted = "PCB, Cable, Battery, LCD",
                    offeredRatePerKg = 350,
                    pickupAvailable = true,
                    rating = 4.9,
                    contactPhone = "+91 98261 44550",
                    address = "Plot 42, Sanwer Road Industrial Area, Sector E, Indore",
                    operatingHours = "8:00 AM – 7:30 PM",
                    reviewsCount = 186
                ),
                RecyclerEntity(
                    id = "rec_abc",
                    name = "ABC Recycling Solutions",
                    isAuthorized = true,
                    authorizationRegNo = "MPPCB/E-WASTE/2023/118",
                    distanceKm = 7.5,
                    materialsAccepted = "Cable, Motor, PCB, Plastic",
                    offeredRatePerKg = 330,
                    pickupAvailable = false,
                    rating = 4.6,
                    contactPhone = "+91 94250 88219",
                    address = "Pologround Industrial Estate, Near Power House, Indore",
                    operatingHours = "9:00 AM – 6:00 PM",
                    reviewsCount = 94
                ),
                RecyclerEntity(
                    id = "rec_malwa",
                    name = "Malwa Eco-Aggregators & Refiners",
                    isAuthorized = true,
                    authorizationRegNo = "MPPCB/E-WASTE/2024/044",
                    distanceKm = 9.2,
                    materialsAccepted = "PCB, Battery, Magnet, Cable, CRT",
                    offeredRatePerKg = 345,
                    pickupAvailable = true,
                    rating = 4.8,
                    contactPhone = "+91 731 2894012",
                    address = "Laxmibai Nagar Mandi Road, Indore",
                    operatingHours = "8:30 AM – 8:00 PM",
                    reviewsCount = 120
                ),
                RecyclerEntity(
                    id = "rec_central",
                    name = "Central Bharat E-Scrap Hub",
                    isAuthorized = false,
                    authorizationRegNo = "Pending Verification",
                    distanceKm = 3.2,
                    materialsAccepted = "Mixed Plastic, Cable, Motor",
                    offeredRatePerKg = 310,
                    pickupAvailable = true,
                    rating = 3.9,
                    contactPhone = "+91 98930 11223",
                    address = "Loha Mandi, Malharganj, Indore",
                    operatingHours = "10:00 AM – 7:00 PM",
                    reviewsCount = 45
                )
            )
            db.recyclerDao().insertAll(recyclers)

            // Seed Price Rates (Indore Market)
            val rates = listOf(
                PriceRateEntity(
                    materialId = "pcb",
                    materialName = "PCB (Circuit Boards)",
                    pricePerKg = 350,
                    trend = "UP",
                    changePercent = 4.8,
                    lastUpdated = "Today, 09:30 AM",
                    juneRate = 310,
                    julyRate = 325,
                    augustRate = 340,
                    septRate = 350
                ),
                PriceRateEntity(
                    materialId = "cable",
                    materialName = "Copper Cable & Wire",
                    pricePerKg = 180,
                    trend = "UP",
                    changePercent = 3.2,
                    lastUpdated = "Today, 10:00 AM",
                    juneRate = 165,
                    julyRate = 170,
                    augustRate = 175,
                    septRate = 180
                ),
                PriceRateEntity(
                    materialId = "battery",
                    materialName = "Lithium & Lead Battery",
                    pricePerKg = 120,
                    trend = "STABLE",
                    changePercent = 0.5,
                    lastUpdated = "Today, 08:45 AM",
                    juneRate = 115,
                    julyRate = 118,
                    augustRate = 120,
                    septRate = 120
                ),
                PriceRateEntity(
                    materialId = "motor",
                    materialName = "Electric Motor Scrap",
                    pricePerKg = 75,
                    trend = "UP",
                    changePercent = 2.1,
                    lastUpdated = "Yesterday, 05:00 PM",
                    juneRate = 68,
                    julyRate = 70,
                    augustRate = 72,
                    septRate = 75
                ),
                PriceRateEntity(
                    materialId = "lcd",
                    materialName = "LCD / LED Panel",
                    pricePerKg = 60,
                    trend = "DOWN",
                    changePercent = -1.5,
                    lastUpdated = "Today, 11:15 AM",
                    juneRate = 65,
                    julyRate = 64,
                    augustRate = 62,
                    septRate = 60
                ),
                PriceRateEntity(
                    materialId = "crt",
                    materialName = "CRT Monitor / TV",
                    pricePerKg = 25,
                    trend = "STABLE",
                    changePercent = 0.0,
                    lastUpdated = "2 days ago",
                    juneRate = 25,
                    julyRate = 25,
                    augustRate = 25,
                    septRate = 25
                ),
                PriceRateEntity(
                    materialId = "magnet",
                    materialName = "Magnet Assembly (HDD)",
                    pricePerKg = 140,
                    trend = "UP",
                    changePercent = 5.0,
                    lastUpdated = "Today, 09:00 AM",
                    juneRate = 125,
                    julyRate = 130,
                    augustRate = 135,
                    septRate = 140
                ),
                PriceRateEntity(
                    materialId = "plastic",
                    materialName = "E-Waste Mixed Plastic",
                    pricePerKg = 30,
                    trend = "STABLE",
                    changePercent = 0.0,
                    lastUpdated = "3 days ago",
                    juneRate = 28,
                    julyRate = 29,
                    augustRate = 30,
                    septRate = 30
                )
            )
            db.priceRateDao().insertAll(rates)

            // Seed Safety Tips
            val tips = listOf(
                SafetyTipEntity(
                    id = "tip_1",
                    iconEmoji = "🔥",
                    isDanger = true,
                    titleHi = "केबल्स को खुले में मत जलाएं",
                    titleMr = "केबल्स उघड्यावर जाळू नका",
                    titleEn = "Do NOT burn cables in open",
                    descHi = "तार जलाने से जहरीला धुआं (Dioxin) निकलता है जो फेफड़ों को भारी नुकसान पहुंचाता है। केबल स्ट्रिपर का उपयोग करें।",
                    descMr = "केबल्स जाळल्याने विषारी वायू बाहेर पडतो जो आरोग्यास अत्यंत घातक आहे. स्ट्रिपिंग यंत्र वापरा.",
                    descEn = "Burning cables releases toxic dioxins causing lung damage. Sell to authorized recyclers with mechanical strippers.",
                    audioHi = "केबल्स को खुले में मत जलाएं। इससे जहरीला धुआं निकलता है जो फेफड़ों को नुकसान पहुंचाता है।",
                    audioMr = "केबल्स उघड्यावर जाळू नका. यामुळे विषारी धूर निघतो आणि श्वसनास त्रास होतो.",
                    audioEn = "Do not burn cables in the open. It releases hazardous fumes."
                ),
                SafetyTipEntity(
                    id = "tip_2",
                    iconEmoji = "🔋",
                    isDanger = true,
                    titleHi = "बैटरी को कभी न तोड़ें या खोलें",
                    titleMr = "बॅटरी कधीही फोडू नका किंवा उघडू नका",
                    titleEn = "Do NOT puncture or dismantle batteries",
                    descHi = "लिथियम बैटरी में आग और धमाका हो सकता है, और लेड बैटरी से तेजाब गिर सकता है। बैटरी को साबुत बेचें।",
                    descMr = "लिथियम बॅटरीमध्ये स्फोट किंवा आग लागू शकते. बॅटरी अखंड ठेवा.",
                    descEn = "Lithium batteries can catch violent fires. Lead-acid leaks toxic acid. Always handover intact batteries.",
                    audioHi = "बैटरी को कभी भी न तोड़ें। इसमें आग लगने और धमाका होने का खतरा होता है।",
                    audioMr = "बॅटरी कधीही तोडू नका. यामध्ये आग लागण्याचा धोका असतो.",
                    audioEn = "Never dismantle batteries. Risk of explosion and severe burns."
                ),
                SafetyTipEntity(
                    id = "tip_3",
                    iconEmoji = "🧪",
                    isDanger = true,
                    titleHi = "सोने/तांबे के लिए तेज़ाब (Acid) का इस्तेमाल न करें",
                    titleMr = "सोन्या/तांब्यासाठी अॅसिडचा वापर करू नका",
                    titleEn = "Never use acid leaching yourself",
                    descHi = "नाईट्रिक या सल्फ्यूरिक एसिड से अंधापन और त्वचा जलने का भारी खतरा होता है। अधिकृत रिसाइक्लर को पीसीबी बेचें।",
                    descMr = "अॅसिड वापरल्याने डोळे आणि त्वचेला गंभीर धोका निर्माण होतो. पीसीबी थेट अधिकृत रिसायकलरला द्या.",
                    descEn = "Acid leaching causes blindness and chemical burns. Let automated hydrometallurgical refiners process PCBs safely.",
                    audioHi = "कीमती धातु निकालने के लिए एसिड का प्रयोग न करें। यह जानलेवा हो सकता है।",
                    audioMr = "धातू काढण्यासाठी अॅसिड वापरू नका. हे धोकादायक आहे.",
                    audioEn = "Do not use chemical acid. Handover circuit boards to authorized recyclers."
                ),
                SafetyTipEntity(
                    id = "tip_4",
                    iconEmoji = "📺",
                    isDanger = false,
                    titleHi = "सीआरटी टीवी को सावधानी से संभालें",
                    titleMr = "सीआरटी टीव्ही काळजीपूर्वक हाताळा",
                    titleEn = "Handle CRT Glass with High Care",
                    descHi = "सीआरटी के पीछे वैक्यूम और लेड भरा कांच होता है। इसे जोर से न पटकें ताकि कांच टूटने से बिखराव न हो।",
                    descMr = "सीआरटीमध्ये व्हॅक्यूम आणि लेड असते. ते काळजीपूर्वक हाताळा.",
                    descEn = "CRT tubes carry high vacuum and leaded glass. Prevent implosion and glass shards.",
                    audioHi = "सीआरटी टीवी को सावधानी से उठाएं, कांच फूटने से चोट लग सकती है।",
                    audioMr = "सीआरटी काच फुटू नये म्हणून काळजी घ्या.",
                    audioEn = "Handle CRT monitors carefully to avoid glass implosion."
                ),
                SafetyTipEntity(
                    id = "tip_5",
                    iconEmoji = "⚠️",
                    isDanger = false,
                    titleHi = "हज़ार्डस घटकों को न तोड़ें (मरकरी/पारा)",
                    titleMr = "घातक घटक तोडू नका",
                    titleEn = "Do NOT smash mercury-bearing components",
                    descHi = "सीएफएल लैंप, ट्यूबलाइट व पुराने एलसीडी बैकलाइट में पारा होता है। दस्ताने पहनें और बिना तोड़े बेचें।",
                    descMr = "सीएफएल आणि जुन्या दिव्यांमध्ये पारा असतो, ते तोडू नका.",
                    descEn = "Old LCD backlights and tubes contain hazardous mercury vapour. Wear safety gloves.",
                    audioHi = "पारे वाले बल्ब या स्क्रीन को न तोड़ें। दस्ताने का प्रयोग करें।",
                    audioMr = "पारा असलेले घटक फोडू नका. हातमोजे वापरा.",
                    audioEn = "Do not smash mercury-bearing parts. Always wear protective gloves."
                )
            )
            db.safetyTipDao().insertAll(tips)

            // Seed Initial Transactions (as requested in prompt)
            val initialLots = listOf(
                LotEntity(
                    lotId = "KW-2026-00108",
                    materialCategoryId = "pcb",
                    subCategory = "Low-grade PCB",
                    approximateWeightKg = 10.0,
                    condition = "Used",
                    sourceType = "Shop",
                    quotedRatePerKg = 320,
                    estimatedMinPrice = 3000,
                    estimatedMaxPrice = 3500,
                    finalPrice = 3200,
                    recyclerId = "rec_xyz",
                    recyclerName = "XYZ E-Waste Recycling",
                    pickupRequired = true,
                    status = "PAID",
                    paymentMethod = "Cash",
                    paymentStatus = "Paid",
                    timestamp = System.currentTimeMillis() - 86400000L * 3,
                    location = "Indore, MP",
                    handoverReference = "HR-99210",
                    isSynced = true
                ),
                LotEntity(
                    lotId = "KW-2026-00114",
                    materialCategoryId = "cable",
                    subCategory = "Insulated Copper Wire",
                    approximateWeightKg = 20.0,
                    condition = "Good",
                    sourceType = "Household",
                    quotedRatePerKg = 180,
                    estimatedMinPrice = 2200,
                    estimatedMaxPrice = 2600,
                    finalPrice = 2400,
                    recyclerId = "rec_abc",
                    recyclerName = "ABC Recycling Solutions",
                    pickupRequired = false,
                    status = "CONFIRMED",
                    paymentMethod = "Digital",
                    paymentStatus = "Pending",
                    timestamp = System.currentTimeMillis() - 86400000L * 1,
                    location = "Indore, MP",
                    handoverReference = "HR-99245",
                    isSynced = true
                ),
                LotEntity(
                    lotId = "KW-2026-00120",
                    materialCategoryId = "battery",
                    subCategory = "UPS Lead-Acid",
                    approximateWeightKg = 12.0,
                    condition = "Used",
                    sourceType = "Office",
                    quotedRatePerKg = 120,
                    estimatedMinPrice = 1300,
                    estimatedMaxPrice = 1600,
                    finalPrice = 1440,
                    recyclerId = "rec_malwa",
                    recyclerName = "Malwa Eco-Aggregators",
                    pickupRequired = true,
                    status = "PAID",
                    paymentMethod = "Cash",
                    paymentStatus = "Paid",
                    timestamp = System.currentTimeMillis() - 3600000L * 5,
                    location = "Indore, MP",
                    handoverReference = "HR-99288",
                    isSynced = true
                )
            )
            db.lotDao().insertAll(initialLots)
        }
    }
}
