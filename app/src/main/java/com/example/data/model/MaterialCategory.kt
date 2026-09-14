package com.example.data.model

enum class MaterialCategory(
    val id: String,
    val iconEmoji: String,
    val nameEn: String,
    val nameHi: String,
    val nameMr: String,
    val defaultRatePerKg: Int,
    val minRate: Int,
    val maxRate: Int,
    val subCategories: List<String>
) {
    PCB(
        id = "pcb",
        iconEmoji = "🔌",
        nameEn = "PCB (Circuit Boards)",
        nameHi = "पीसीबी (सर्किट बोर्ड)",
        nameMr = "पीसीबी (सर्किट बोर्ड)",
        defaultRatePerKg = 350,
        minRate = 300,
        maxRate = 420,
        subCategories = listOf("Motherboard High-Grade", "Low-grade PCB", "Telecom Board", "Mixed Circuit Boards")
    ),
    CABLE(
        id = "cable",
        iconEmoji = "🔗",
        nameEn = "Copper Cable & Wire",
        nameHi = "केबल और तार (कॉपर)",
        nameMr = "केबल आणि वायर (तांबे)",
        defaultRatePerKg = 180,
        minRate = 160,
        maxRate = 220,
        subCategories = listOf("Insulated Copper Wire", "Unpeeled Heavy Cable", "Flat Ribbon Cable", "Power Cord")
    ),
    BATTERY(
        id = "battery",
        iconEmoji = "🔋",
        nameEn = "Lithium & Lead Battery",
        nameHi = "बैटरी (लिथियम/लेड)",
        nameMr = "बॅटरी (लिथियम/लेड)",
        defaultRatePerKg = 120,
        minRate = 95,
        maxRate = 145,
        subCategories = listOf("Smartphone Li-ion", "Laptop Pack", "UPS Lead-Acid", "EV Battery Pack")
    ),
    MOTOR(
        id = "motor",
        iconEmoji = "⚙️",
        nameEn = "Electric Motor",
        nameHi = "इलेक्ट्रिक मोटर",
        nameMr = "इलेक्ट्रिक मोटर",
        defaultRatePerKg = 75,
        minRate = 60,
        maxRate = 90,
        subCategories = listOf("Washing Machine Motor", "Fan Motor", "Compressor Motor", "Stepper Motor")
    ),
    LCD(
        id = "lcd",
        iconEmoji = "🖥️",
        nameEn = "LCD / LED Panel",
        nameHi = "एलसीडी / एलईडी पैनल",
        nameMr = "एलसीडी / एलईडी पॅनेल",
        defaultRatePerKg = 60,
        minRate = 45,
        maxRate = 80,
        subCategories = listOf("Intact Display Panel", "Laptop Screen", "Cracked LCD", "Monitor Assembly")
    ),
    CRT(
        id = "crt",
        iconEmoji = "📺",
        nameEn = "CRT Monitor / TV",
        nameHi = "सीआरटी मॉनिटर / टीवी",
        nameMr = "सीआरटी टीव्ही / मॉनिटर",
        defaultRatePerKg = 25,
        minRate = 15,
        maxRate = 35,
        subCategories = listOf("Intact Glass Tube", "De-yoked CRT", "Color TV Chassis", "Monochrome CRT")
    ),
    MAGNET(
        id = "magnet",
        iconEmoji = "🧲",
        nameEn = "Magnet Assembly (HDD/Speaker)",
        nameHi = "मैग्नेट असेंबली (हार्ड डिस्क)",
        nameMr = "मॅग्नेट असेंब्ली (हार्ड डिस्क)",
        defaultRatePerKg = 140,
        minRate = 110,
        maxRate = 170,
        subCategories = listOf("HDD Neodymium Magnet", "Audio Speaker Magnet", "Motor Stator Magnet")
    ),
    PLASTIC(
        id = "plastic",
        iconEmoji = "🧴",
        nameEn = "E-Waste Mixed Plastic",
        nameHi = "ई-कचरा मिक्स्ड प्लास्टिक",
        nameMr = "ई-कचरा प्लास्टिक",
        defaultRatePerKg = 30,
        minRate = 20,
        maxRate = 40,
        subCategories = listOf("ABS Casing", "Polycarbonate Housing", "Printer Enclosure", "Mixed Scrap Plastic")
    ),
    OTHER(
        id = "other",
        iconEmoji = "➕",
        nameEn = "Other E-Waste",
        nameHi = "अन्य इलेक्ट्रॉनिक कचरा",
        nameMr = "इतर इलेक्ट्रॉनिक कचरा",
        defaultRatePerKg = 50,
        minRate = 30,
        maxRate = 70,
        subCategories = listOf("Mixed Small Appliances", "Adapter & Chargers", "Keyboards & Mice", "Telecom Scrap")
    );

    fun getLocalizedName(lang: Language): String = when (lang) {
        Language.HINDI -> nameHi
        Language.MARATHI -> nameMr
        Language.ENGLISH -> nameEn
    }

    companion object {
        fun fromId(id: String): MaterialCategory =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: PCB
    }
}
