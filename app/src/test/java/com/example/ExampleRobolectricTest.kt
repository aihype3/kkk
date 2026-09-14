package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("KabadiWala", appName)
  }

  @Test
  fun `test price estimation calculation`() {
    val (minP, maxP, rate) = com.example.util.AiClassifierHelper.estimatePrice(
      com.example.data.model.MaterialCategory.PCB,
      10.0,
      "Used"
    )
    assertEquals(350, rate)
    org.junit.Assert.assertTrue(minP in 3000..3500)
    org.junit.Assert.assertTrue(maxP in 3500..4000)
  }

  @Test
  fun `test gemini vision service fallback classification`() = kotlinx.coroutines.runBlocking {
    val bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    val result = com.example.data.service.GeminiVisionService.categorizeScrapMaterial(bitmap)
    org.junit.Assert.assertNotNull(result)
    org.junit.Assert.assertTrue(result.confidencePercent in 80..100)
    org.junit.Assert.assertTrue(result.description.isNotEmpty())
  }

  @Test
  fun `test sahayak voice bot hindi processing`() = kotlinx.coroutines.runBlocking {
    val draft = com.example.data.service.SahayakLotDraft()
    val (message, updatedDraft) = com.example.data.service.SahayakChatService.processUserSpeech(
      userInput = "मेरे पास 15 किलो सर्किट बोर्ड है",
      currentDraft = draft,
      language = com.example.data.model.Language.HINDI,
      conversationHistory = emptyList()
    )
    org.junit.Assert.assertEquals(com.example.data.model.MaterialCategory.PCB, updatedDraft.material)
    org.junit.Assert.assertEquals(15.0, updatedDraft.weightKg, 0.01)
    org.junit.Assert.assertTrue(updatedDraft.estimatedMinPrice > 0)
    org.junit.Assert.assertTrue(message.text.isNotEmpty())
  }

  @Test
  fun `test sahayak voice bot english processing`() = kotlinx.coroutines.runBlocking {
    val draft = com.example.data.service.SahayakLotDraft()
    val (message, updatedDraft) = com.example.data.service.SahayakChatService.processUserSpeech(
      userInput = "I have 20 kg copper cable to sell",
      currentDraft = draft,
      language = com.example.data.model.Language.ENGLISH,
      conversationHistory = emptyList()
    )
    org.junit.Assert.assertEquals(com.example.data.model.MaterialCategory.CABLE, updatedDraft.material)
    org.junit.Assert.assertEquals(20.0, updatedDraft.weightKg, 0.01)
    org.junit.Assert.assertTrue(updatedDraft.estimatedMinPrice > 0)
    org.junit.Assert.assertTrue(message.text.isNotEmpty())
  }
}
