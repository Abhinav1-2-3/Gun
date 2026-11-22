package com.projectgun.gun

import com.projectgun.gun.domain.intent.IntentRecognitionEngine
import com.projectgun.gun.domain.intent.UserIntent
import org.junit.Assert.assertEquals
import org.junit.Test

class IntentRecognitionTest {

    @Test
    fun testPainRecognition() {
        val match = IntentRecognitionEngine.recognize("I felt a sharp pain in my knee during squats")
        assertEquals(UserIntent.REPORT_PAIN, match.intent)
    }

    @Test
    fun testFatigueRecognition() {
        val match = IntentRecognitionEngine.recognize("I am totally exhausted today and have no energy")
        assertEquals(UserIntent.REPORT_FATIGUE, match.intent)
    }

    @Test
    fun testMotivationExcuseRecognition() {
        val match = IntentRecognitionEngine.recognize("I don't feel like working out today, I want to skip")
        assertEquals(UserIntent.ASK_MOTIVATION, match.intent)
    }

    @Test
    fun testProgressQuery() {
        val match = IntentRecognitionEngine.recognize("Am I improving and getting stronger?")
        assertEquals(UserIntent.ASK_PROGRESS, match.intent)
    }

    @Test
    fun testPrQuery() {
        val match = IntentRecognitionEngine.recognize("Show me the numbers on my personal records")
        assertEquals(UserIntent.ASK_PR, match.intent)
    }
}
