package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.data.model.Language
import java.util.Locale

class TtsHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var onSpeechStatusChange: ((Boolean) -> Unit)? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isReady = true
            tts?.setSpeechRate(0.92f) // slightly slower for maximum low-literacy clarity
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onSpeechStatusChange?.invoke(true)
                }

                override fun onDone(utteranceId: String?) {
                    onSpeechStatusChange?.invoke(false)
                }

                override fun onError(utteranceId: String?) {
                    onSpeechStatusChange?.invoke(false)
                }
            })
        }
    }

    fun setSpeechStatusListener(listener: (Boolean) -> Unit) {
        onSpeechStatusChange = listener
    }

    fun speak(text: String, language: Language) {
        if (!isReady || tts == null) return

        val locale = when (language) {
            Language.HINDI -> Locale.forLanguageTag("hi-IN")
            Language.MARATHI -> Locale.forLanguageTag("mr-IN")
            Language.ENGLISH -> Locale.forLanguageTag("en-IN")
        }

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to English if regional voice pack not installed on low-end device
            tts?.setLanguage(Locale.ENGLISH)
        }

        val cleanText = text.replace("*", "").replace("#", "").trim()
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "KabadiWalaTts_${System.currentTimeMillis()}")
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
