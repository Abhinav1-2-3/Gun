package com.projectgun.gun

import com.projectgun.gun.data.model.RecoveryLog
import com.projectgun.gun.domain.recovery.SafetyCheckResult
import com.projectgun.gun.domain.recovery.SafetyEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SafetyEngineTest {

    @Test
    fun testActivePainHaltsTraining() {
        val log = RecoveryLog(
            dateEpochDay = 1000L,
            fatigueLevel = 2,
            sorenessLevel = 2,
            sleepHours = 8f,
            sleepQuality = 5,
            painAreas = listOf("Right Shoulder"),
            gunRecommendation = ""
        )

        val result = SafetyEngine.evaluateSafety(
            latestRecovery = log,
            consecutiveTrainingDays = 1,
            userLimitations = emptyList()
        )

        assertTrue(result is SafetyCheckResult.HaltTraining)
    }

    @Test
    fun testExtremeFatigueHaltsTraining() {
        val log = RecoveryLog(
            dateEpochDay = 1000L,
            fatigueLevel = 5,
            sorenessLevel = 4,
            sleepHours = 3.5f,
            sleepQuality = 1,
            painAreas = emptyList(),
            gunRecommendation = ""
        )

        val result = SafetyEngine.evaluateSafety(
            latestRecovery = log,
            consecutiveTrainingDays = 2,
            userLimitations = emptyList()
        )

        assertTrue(result is SafetyCheckResult.HaltTraining)
    }

    @Test
    fun testSafeExerciseValidation() {
        val limitations = listOf("Shoulder Impingement")
        val isSafe = SafetyEngine.isExerciseSafe("Overhead Barbell Press", "Anterior Deltoids", limitations)
        assertEquals(false, isSafe)

        val isSafeSquat = SafetyEngine.isExerciseSafe("Barbell Back Squat", "Quadriceps", limitations)
        assertEquals(true, isSafeSquat)
    }
}
