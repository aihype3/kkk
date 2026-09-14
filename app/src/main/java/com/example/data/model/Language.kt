package com.example.data.model

enum class Language(val code: String, val displayName: String, val nativeName: String) {
    HINDI("hi", "Hindi", "हिन्दी"),
    MARATHI("mr", "Marathi", "मराठी"),
    ENGLISH("en", "English", "English")
}

enum class AppRole(val displayName: String, val description: String) {
    COLLECTOR("Kabadiwala / Collector", "कबाड़ीवाला / स्क्रैप संग्रहकर्ता"),
    RECYCLER("Authorized Recycler", "अधिकृत रिसाइक्लर"),
    ADMIN("Impact & Data Admin", "डेटा एवं विश्लेषण")
}
