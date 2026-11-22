package com.projectgun.gun

import com.projectgun.gun.data.model.RecoveryLog
import com.projectgun.gun.domain.recovery.ReadinessStatus
import com.projectgun.gun.domain.recovery.RecoveryEngine
import org.junit.Assert.assertEquals
import org.junit.Test

class RecoveryEngineTest {

    @Test
    fun testOptimalRecovery() {
        val log = RecoveryLog(
            dateEpochDay = 100L,
            fatigueLevel = 1,
            sorenessLevel = 1,
            sleepHours = 8.5f,
            sleepQuality = 5,
            painAreas = emptyList(),
            gunRecommendation = ""
        )

        val eval = RecoveryEngine.evaluate(log, consecutiveTrainingDays = 0)
        assertEquals(ReadinessStatus.OPTIMAL, eval.status)
        assertEquals(1.0f, eval.volumeMultiplier, 0.01f)
    }

    @Test
    fun testPainCapsReadiness() {
        val log = RecoveryLog(
            dateEpochDay = 100L,
            fatigueLevel = 1,
            sorenessLevel = 1,
            sleepHours = 9f,
            sleepQuality = 5,
            painAreas = listOf("Elbow pain"),
            gunRecommendation = ""
        )

        val eval = RecoveryEngine.evaluate(log, consecutiveTrainingDays = 0)
        assertEquals(ReadinessStatus.OVERREACHED, eval.status)
        assertEquals(0.0f, eval.volumeMultiplier, 0.01f)
    }
}
