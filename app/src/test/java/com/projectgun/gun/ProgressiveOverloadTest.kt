package com.projectgun.gun

import com.projectgun.gun.domain.training.ProgressiveOverloadEngine
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressiveOverloadTest {

    @Test
    fun test1RMCalculationEpley() {
        // 100 kg x 10 reps -> 100 * (1 + 10/30) = 133.3 kg
        val oneRm = ProgressiveOverloadEngine.calculateEstimated1RM(100f, 10)
        assertEquals(133.3f, oneRm, 0.1f)
    }

    @Test
    fun testSingleRepIsExactWeight() {
        val oneRm = ProgressiveOverloadEngine.calculateEstimated1RM(120f, 1)
        assertEquals(120f, oneRm, 0.01f)
    }
}
