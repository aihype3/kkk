package com.example.data.service

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.Language
import com.example.data.model.MaterialCategory
import com.example.util.AiClassifierHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

enum class SahayakSender {
    USER,
    SAHAYAK
}

data class SahayakLotDraft(
    val material: MaterialCategory? = null,
    val subCategory: String = "",
    val weightKg: Double = 0.0,
    val condition: String = "Used",
    val sourceType: String = "Household",
    val estimatedMinPrice: Int = 0,
    val estimatedMaxPrice: Int = 0,
    val quotedRatePerKg: Int = 0,
    val recyclerName: String = "EcoRecycle Authorized Hub",
    val recyclerId: String = "rec_01",
    val isReadyToConfirm: Boolean = false,
    val isConfirmed: Boolean = false,
    val confirmedLotId: String = ""
)

data class SahayakMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: SahayakSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val quickReplies: List<String> = emptyList(),
    val lotPreview: SahayakLotDraft? = null,
    val isLotCreated: Boolean = false
)

object SahayakChatService {
    private const val TAG = "SahayakChatService"
    private val CANDIDATE_MODELS = listOf("gemini-3.6-flash", "gemini-flash-latest", "gemini-2.5-flash")
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun getInitialGreeting(language: Language): SahayakMessage {
        val text = when (language) {
            Language.HINDI -> "नमस्ते, मैं आपका सहायक वॉयस असिस्टेंट हूँ! आप क्या बेचना चाहते हैं?"
            Language.MARATHI -> "नमस्ते, मी तुमचा सहाय्यक व्हॉइस असिस्टंट आहे! तुम्ही काय विकू इच्छिता?"
            Language.ENGLISH -> "Namaste, I am your Sahayak voice assistant! What do you want to sell?"
        }

        val quickReplies = when (language) {
            Language.HINDI -> listOf("🖥️ PCBs", "🔌 Cables", "🔋 Batteries", "⚙️ Motors", "📺 CRTs", "💻 LCD/LED panels", "🧲 Magnet assemblies", "🧴 Mixed plastic")
            Language.MARATHI -> listOf("🖥️ PCBs", "🔌 Cables", "🔋 Batteries", "⚙️ Motors", "📺 CRTs", "💻 LCD/LED panels", "🧲 Magnet assemblies", "🧴 Mixed plastic")
            Language.ENGLISH -> listOf("🖥️ PCBs", "🔌 Cables", "🔋 Batteries", "⚙️ Motors", "📺 CRTs", "💻 LCD/LED panels", "🧲 Magnet assemblies", "🧴 Mixed plastic")
        }

        return SahayakMessage(
            sender = SahayakSender.SAHAYAK,
            text = text,
            quickReplies = quickReplies
        )
    }

    suspend fun processUserSpeech(
        userInput: String,
        currentDraft: SahayakLotDraft,
        language: Language,
        conversationHistory: List<SahayakMessage>
    ): Pair<SahayakMessage, SahayakLotDraft> = withContext(Dispatchers.IO) {
        val trimmedInput = userInput.trim()

        // 1. Try Gemini API for contextual parsing
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val isKeyValid = apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY" && apiKey != "GEMINI_API_KEY"

        if (isKeyValid) {
            val geminiResult = callGeminiForConversation(trimmedInput, currentDraft, language, conversationHistory, apiKey)
            if (geminiResult != null) {
                return@withContext geminiResult
            }
        }

        // 2. Resilient Rule-Based Offline Parser
        return@withContext processOfflineConversation(trimmedInput, currentDraft, language)
    }

    private fun callGeminiForConversation(
        userInput: String,
        currentDraft: SahayakLotDraft,
        language: Language,
        conversationHistory: List<SahayakMessage>,
        apiKey: String
    ): Pair<SahayakMessage, SahayakLotDraft>? {
        return try {
            val systemPrompt = """
                You are 'Sahayak', an empathetic, highly efficient voice assistant in the KabadiWala Android app.
                Your task is to guide Indian scrap collectors (kabadiwalas) through voice/text to create an e-waste scrap lot.
                The necessary details to make a lot are:
                1. material: One of [PCB, CABLE, BATTERY, MOTOR, LCD, CRT, MAGNET, PLASTIC]
                2. subCategory: specific type (e.g., Computer Motherboard, Clean Copper Cable, Lead-Acid Battery)
                3. weightKg: number in kilograms (e.g. 5, 10.5, 20)
                4. condition: e.g. "Used", "Good", "Scrap", "Refurbishable"
                5. sourceType: e.g. "Household", "Electronics Shop", "Commercial/Office", "Repair Center"
                6. isReadyToConfirm: boolean, true when material and weight are known and user is presented with the price quote.
                7. userConfirmed: boolean, true if user explicitly agrees to confirm/finalize/create the lot (e.g. "yes", "हां", "बना दो", "confirm", "theek hai").

                Current lot state:
                - Material: ${currentDraft.material?.name ?: "UNKNOWN"}
                - SubCategory: ${currentDraft.subCategory}
                - WeightKg: ${currentDraft.weightKg}
                - Condition: ${currentDraft.condition}
                - SourceType: ${currentDraft.sourceType}
                - IsReadyToConfirm: ${currentDraft.isReadyToConfirm}

                User's preferred language: ${if (language == Language.HINDI) "Hindi (Devanagari script with polite, simple Hindi)" else "English"}

                Output strictly JSON:
                {
                  "extractedMaterial": "PCB" | "CABLE" | "BATTERY" | "MOTOR" | "LCD" | "CRT" | "MAGNET" | "PLASTIC" | null,
                  "extractedSubCategory": string or null,
                  "extractedWeightKg": number or null,
                  "extractedCondition": string or null,
                  "extractedSource": string or null,
                  "userConfirmed": boolean,
                  "replyText": "your spoken conversational reply in ${if (language == Language.HINDI) "Hindi" else "English"}",
                  "quickReplies": ["3-4 short reply suggestions for buttons"]
                }
            """.trimIndent()

            val jsonRequest = JSONObject().apply {
                val contents = JSONArray()
                val parts = JSONArray().apply {
                    put(JSONObject().put("text", "$systemPrompt\n\nUser input: $userInput"))
                }
                contents.put(JSONObject().put("role", "user").put("parts", parts))
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("responseMimeType", "application/json")
                })
            }

            val requestBodyString = jsonRequest.toString()

            for (model in CANDIDATE_MODELS) {
                try {
                    val url = "$BASE_URL/$model:generateContent?key=$apiKey"
                    val request = Request.Builder()
                        .url(url)
                        .post(requestBodyString.toRequestBody("application/json".toMediaType()))
                        .build()

                    val response = okHttpClient.newCall(request).execute()
                    if (!response.isSuccessful) {
                        Log.w(TAG, "Gemini chat failed on $model with code ${response.code}")
                        continue
                    }

                    val body = response.body?.string() ?: continue
                    val jsonResp = JSONObject(body)
                    val candidates = jsonResp.optJSONArray("candidates") ?: continue
                    if (candidates.length() == 0) continue

                    val firstCandidate = candidates.getJSONObject(0)
                    val contentObj = firstCandidate.optJSONObject("content") ?: continue
                    val partsArr = contentObj.optJSONArray("parts") ?: continue
                    if (partsArr.length() == 0) continue

                    val rawJson = partsArr.getJSONObject(0).optString("text", "")
                    val parsed = JSONObject(rawJson.removePrefix("```json").removePrefix("```").removeSuffix("```").trim())

                    // Parse fields
                    val matStr = parsed.optString("extractedMaterial", "")
                    val newMat = when {
                        matStr.contains("PCB") -> MaterialCategory.PCB
                        matStr.contains("CABLE") -> MaterialCategory.CABLE
                        matStr.contains("BATTERY") -> MaterialCategory.BATTERY
                        matStr.contains("MOTOR") -> MaterialCategory.MOTOR
                        matStr.contains("LCD") -> MaterialCategory.LCD
                        matStr.contains("CRT") -> MaterialCategory.CRT
                        matStr.contains("MAGNET") -> MaterialCategory.MAGNET
                        matStr.contains("PLASTIC") -> MaterialCategory.PLASTIC
                        else -> currentDraft.material
                    }

                    val newSub = parsed.optString("extractedSubCategory").takeIf { it.isNotEmpty() && it != "null" }
                        ?: currentDraft.subCategory.ifEmpty { newMat?.subCategories?.firstOrNull() ?: "Standard Grade" }

                    val rawWeight = parsed.optDouble("extractedWeightKg", Double.NaN)
                    val newWeight = if (!rawWeight.isNaN() && rawWeight > 0) rawWeight else currentDraft.weightKg

                    val newCond = parsed.optString("extractedCondition").takeIf { it.isNotEmpty() && it != "null" }
                        ?: currentDraft.condition

                    val newSource = parsed.optString("extractedSource").takeIf { it.isNotEmpty() && it != "null" }
                        ?: currentDraft.sourceType

                    val userConfirmed = parsed.optBoolean("userConfirmed", false)

                    // Estimate prices
                    val activeMat = newMat ?: MaterialCategory.PCB
                    val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(activeMat, if (newWeight > 0) newWeight else 10.0, newCond)

                    val isReady = newMat != null && newWeight > 0.0
                    val isConfirmed = userConfirmed && (isReady || currentDraft.isReadyToConfirm)

                    val updatedDraft = currentDraft.copy(
                        material = newMat,
                        subCategory = newSub,
                        weightKg = newWeight,
                        condition = newCond,
                        sourceType = newSource,
                        estimatedMinPrice = minP,
                        estimatedMaxPrice = maxP,
                        quotedRatePerKg = rate,
                        isReadyToConfirm = isReady,
                        isConfirmed = isConfirmed,
                        confirmedLotId = if (isConfirmed) "KW-2026-${(10000..99999).random()}" else currentDraft.confirmedLotId
                    )

                    val replyText = parsed.optString("replyText", "समझ गया।")
                    val replies = mutableListOf<String>()
                    val qJson = parsed.optJSONArray("quickReplies")
                    if (qJson != null) {
                        for (i in 0 until qJson.length()) {
                            replies.add(qJson.getString(i))
                        }
                    }

                    val message = SahayakMessage(
                        sender = SahayakSender.SAHAYAK,
                        text = replyText,
                        quickReplies = replies.ifEmpty { generateDefaultReplies(updatedDraft, language) },
                        lotPreview = updatedDraft,
                        isLotCreated = isConfirmed
                    )

                    return Pair(message, updatedDraft)
                } catch (e: Exception) {
                    Log.w(TAG, "Error invoking $model: ${e.message}")
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Gemini conversation exception: ${e.message}")
            null
        }
    }

    private fun processOfflineConversation(
        input: String,
        currentDraft: SahayakLotDraft,
        language: Language
    ): Pair<SahayakMessage, SahayakLotDraft> {
        val lower = input.lowercase()
        var updated = currentDraft

        // Check for confirmation intent
        val isConfirmIntent = lower.contains("yes") || lower.contains("confirm") || lower.contains("हाँ") ||
                lower.contains("हां") || lower.contains("लॉट बना") || lower.contains("हो गया") ||
                lower.contains("theek hai") || lower.contains("ठीक है") || lower.contains("save") ||
                lower.contains("डन") || lower.contains("done") || lower.contains("ok") || lower.contains("करा")

        if (isConfirmIntent && (updated.isReadyToConfirm || (updated.material != null && updated.weightKg > 0))) {
            val confirmedId = "KW20250815" + String.format("%04d", (1..9999).random())
            val finalDraft = updated.copy(
                isConfirmed = true,
                confirmedLotId = confirmedId
            )
            val reply = when (language) {
                Language.HINDI -> "🎉 आपका लॉट कन्फर्म हो गया है! आपका लॉट ID है: $confirmedId। इसे आप हिस्ट्री (History) विकल्प में देख सकते हैं।"
                Language.MARATHI -> "🎉 तुमचा लॉट निश्चित झाला आहे! तुमचा लॉट ID आहे: $confirmedId. तुम्ही हा इतिहास (History) पर्यायामध्ये पाहू शकता."
                Language.ENGLISH -> "🎉 Your lot has been confirmed! Your Lot ID is $confirmedId. You can see this lot anytime in the History option."
            }
            return Pair(
                SahayakMessage(
                    sender = SahayakSender.SAHAYAK,
                    text = reply,
                    quickReplies = listOf(
                        if (language == Language.HINDI) "📜 हिस्ट्री में देखें" else if (language == Language.MARATHI) "📜 इतिहास पहा" else "📜 View in History",
                        if (language == Language.HINDI) "🏠 होम पर जाएं" else if (language == Language.MARATHI) "🏠 मुख्य पान" else "🏠 Go to Home"
                    ),
                    lotPreview = finalDraft,
                    isLotCreated = true
                ),
                finalDraft
            )
        }

        // 1. Detect Material
        var detectedMat = updated.material
        if (lower.contains("pcb") || lower.contains("सर्किट") || lower.contains("मदरबोर्ड") || lower.contains("board") || lower.contains("chip")) {
            detectedMat = MaterialCategory.PCB
        } else if (lower.contains("cable") || lower.contains("wire") || lower.contains("केबल") || lower.contains("तार") || lower.contains("copper") || lower.contains("तांबा")) {
            detectedMat = MaterialCategory.CABLE
        } else if (lower.contains("battery") || lower.contains("बैटरी") || lower.contains("cell") || lower.contains("सेल") || lower.contains("lithium")) {
            detectedMat = MaterialCategory.BATTERY
        } else if (lower.contains("motor") || lower.contains("मोटर") || lower.contains("fan") || lower.contains("pump") || lower.contains("पंखा")) {
            detectedMat = MaterialCategory.MOTOR
        } else if (lower.contains("lcd") || lower.contains("screen") || lower.contains("स्क्रीन") || lower.contains("display") || lower.contains("मॉनिटर")) {
            detectedMat = MaterialCategory.LCD
        } else if (lower.contains("crt") || lower.contains("टीवी") || lower.contains("picture tube")) {
            detectedMat = MaterialCategory.CRT
        } else if (lower.contains("magnet") || lower.contains("चुंबक")) {
            detectedMat = MaterialCategory.MAGNET
        } else if (lower.contains("plastic") || lower.contains("प्लास्टिक") || lower.contains("casing")) {
            detectedMat = MaterialCategory.PLASTIC
        }

        if (detectedMat != null && updated.material != detectedMat) {
            updated = updated.copy(
                material = detectedMat,
                subCategory = detectedMat.subCategories.firstOrNull() ?: "Standard Grade"
            )
        }

        // 2. Detect Weight (e.g. "10 kg", "5.5 किलो", "20kg", "15")
        val weightRegex = Regex("""(\d+(\.\d+)?)\s*(kg|kilos?|किलो|किलोग्राम)?""", RegexOption.IGNORE_CASE)
        val match = weightRegex.find(lower)
        if (match != null) {
            val numStr = match.groupValues[1]
            val num = numStr.toDoubleOrNull()
            if (num != null && num in 0.5..1000.0) {
                updated = updated.copy(weightKg = num)
            }
        }

        // 3. Detect Condition
        if (lower.contains("scrap") || lower.contains("खराब") || lower.contains("कबाड़") || lower.contains("tuta")) {
            updated = updated.copy(condition = "Scrap")
        } else if (lower.contains("working") || lower.contains("चालू") || lower.contains("good") || lower.contains("बढ़िया")) {
            updated = updated.copy(condition = "Refurbishable")
        } else if (lower.contains("used") || lower.contains("पुराना")) {
            updated = updated.copy(condition = "Used")
        }

        // 4. Detect Source
        if (lower.contains("shop") || lower.contains("दुकान")) {
            updated = updated.copy(sourceType = "Electronics Shop")
        } else if (lower.contains("repair") || lower.contains("रिपेयर")) {
            updated = updated.copy(sourceType = "Repair Center")
        } else if (lower.contains("home") || lower.contains("house") || lower.contains("घर")) {
            updated = updated.copy(sourceType = "Household")
        } else if (lower.contains("office") || lower.contains("ऑफिस")) {
            updated = updated.copy(sourceType = "Commercial/Office")
        }

        // Recalculate prices if material and weight are available
        if (updated.material != null) {
            val weightForPricing = if (updated.weightKg > 0) updated.weightKg else 10.0
            val (minP, maxP, rate) = AiClassifierHelper.estimatePrice(updated.material!!, weightForPricing, updated.condition)
            updated = updated.copy(
                estimatedMinPrice = minP,
                estimatedMaxPrice = maxP,
                quotedRatePerKg = rate,
                isReadyToConfirm = updated.weightKg > 0
            )
        }

        // Determine what Sahayak should ask next
        val (replyText, quickReplies) = when {
            updated.material == null -> {
                val text = when (language) {
                    Language.HINDI -> "नमस्ते, मैं आपका सहायक वॉयस असिस्टेंट हूँ! आप क्या बेचना चाहते हैं?"
                    Language.MARATHI -> "नमस्ते, मी तुमचा सहाय्यक व्हॉइस असिस्टंट आहे! तुम्ही काय विकू इच्छिता?"
                    Language.ENGLISH -> "Namaste, I am your Sahayak voice assistant! What do you want to sell?"
                }
                Pair(text, listOf("🖥️ PCBs", "🔌 Cables", "🔋 Batteries", "⚙️ Motors", "📺 CRTs", "💻 LCD/LED panels", "🧲 Magnet assemblies", "🧴 Mixed plastic"))
            }
            updated.weightKg <= 0.0 -> {
                val text = when (language) {
                    Language.HINDI -> "सामान का वजन कितना है (किलो में)?"
                    Language.MARATHI -> "साहित्याचे वजन किती आहे (किलोमध्ये)?"
                    Language.ENGLISH -> "What is the weight of the item (in kg)?"
                }
                Pair(text, listOf("2 kg", "5 kg", "10 kg", "25 kg"))
            }
            updated.isReadyToConfirm -> {
                val matName = when (language) {
                    Language.HINDI -> updated.material!!.nameHi
                    Language.MARATHI -> updated.material!!.nameMr
                    Language.ENGLISH -> updated.material!!.nameEn
                }
                val totalPrice = (updated.weightKg * updated.quotedRatePerKg).toInt()
                val text = when (language) {
                    Language.HINDI -> "सामान का वर्तमान भाव: ₹${updated.quotedRatePerKg}/किग्रा है। ${updated.weightKg} किग्रा के लिए अनुमानित मूल्य ₹$totalPrice है। क्या आप इस लॉट को कन्फर्म करना चाहते हैं?"
                    Language.MARATHI -> "साहित्याचा सध्याचा दर: ₹${updated.quotedRatePerKg}/किलो आहे. ${updated.weightKg} किलोसाठी अंदाजे किंमत ₹$totalPrice आहे. तुम्ही हा लॉट निश्चित करू इच्छिता का?"
                    Language.ENGLISH -> "This is the current price of the item: ₹${updated.quotedRatePerKg}/kg. For ${updated.weightKg} kg, total estimated price is ₹$totalPrice. Do you want to confirm this lot?"
                }
                Pair(text, listOf(
                    if (language == Language.HINDI) "✅ कन्फर्म करें" else if (language == Language.MARATHI) "✅ लॉट निश्चित करा" else "✅ Confirm Lot",
                    if (language == Language.HINDI) "वजन बदलें" else if (language == Language.MARATHI) "वजन बदला" else "Change Weight"
                ))
            }
            else -> {
                val text = when (language) {
                    Language.HINDI -> "क्या आप इस लॉट को कन्फर्म करना चाहते हैं?"
                    Language.MARATHI -> "तुम्ही हा लॉट निश्चित करू इच्छिता का?"
                    Language.ENGLISH -> "Do you want to confirm this lot?"
                }
                Pair(
                    text,
                    listOf(
                        if (language == Language.HINDI) "✅ कन्फर्म करें" else if (language == Language.MARATHI) "✅ निश्चित करा" else "✅ Confirm Lot",
                        "Cancel"
                    )
                )
            }
        }

        return Pair(
            SahayakMessage(
                sender = SahayakSender.SAHAYAK,
                text = replyText,
                quickReplies = quickReplies,
                lotPreview = updated
            ),
            updated
        )
    }

    private fun generateDefaultReplies(draft: SahayakLotDraft, language: Language): List<String> {
        return when {
            draft.material == null -> {
                if (language == Language.HINDI) listOf("🖥️ PCB बोर्ड", "🔌 कॉपर केबल", "🔋 बैटरी", "⚙️ मोटर")
                else listOf("🖥️ PCB Board", "🔌 Copper Cable", "🔋 Battery", "⚙️ Motor")
            }
            draft.weightKg <= 0.0 -> listOf("5 kg", "10 kg", "20 kg", "50 kg")
            draft.isReadyToConfirm -> {
                if (language == Language.HINDI) listOf("✅ हाँ, लॉट बना दो", "वजन बदलें", "सामान बदलें")
                else listOf("✅ Yes, Create Lot", "Change Weight", "Change Material")
            }
            else -> listOf("Ok", "Help")
        }
    }
}
