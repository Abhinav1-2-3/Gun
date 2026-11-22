package com.projectgun.gun.domain.recovery

import com.projectgun.gun.data.model.RecoveryLog

enum class ReadinessStatus {
    OPTIMAL,
    ADEQUATE,
    FATIGUED,
    OVERREACHED
}

data class RecoveryEvaluation(
    val readinessScore: Int, // 0 to 100
    val status: ReadinessStatus,
    val summary: String,
    val recommendation: String,
    val volumeMultiplier: Float // 1.0f for normal, 0.7f for fatigued, 0.0f for rest
)

object RecoveryEngine {

    fun evaluate(latestLog: RecoveryLog?, consecutiveTrainingDays: Int): RecoveryEvaluation {
        if (latestLog == null) {
            return RecoveryEvaluation(
                readinessScore = 80,
                status = ReadinessStatus.ADEQUATE,
                summary = "No recovery log recorded for today. Baseline readiness assumed.",
                recommendation = "Proceed with normal planned session. Log your fatigue and sleep for tailored adjustments.",
                volumeMultiplier = 1.0f
            )
        }

        // Calculate score from 100
        // Sleep contribution (max 40 pts): 8 hrs = 40 pts
        val sleepScore = ((latestLog.sleepHours / 8f) * 30f + (latestLog.sleepQuality / 5f) * 10f).coerceIn(0f, 40f)

        // Fatigue penalty (max 30 pts): 1 = 30 pts, 5 = 0 pts
        val fatigueScore = ((5 - latestLog.fatigueLevel) / 4f * 30f).coerceIn(0f, 30f)

        // Soreness penalty (max 20 pts): 1 = 20 pts, 5 = 0 pts
        val sorenessScore = ((5 - latestLog.sorenessLevel) / 4f * 20f).coerceIn(0f, 20f)

        // Consecutive days penalty (max 10 pts)
        val consecutiveScore = when {
            consecutiveTrainingDays >= 4 -> 0f
            consecutiveTrainingDays == 3 -> 5f
            else -> 10f
        }

        var total = (sleepScore + fatigueScore + sorenessScore + consecutiveScore).toInt().coerceIn(0, 100)

        // If pain is present, drastically cap score
        if (latestLog.painAreas.isNotEmpty()) {
            total = total.coerceAtMost(35)
        }

        return when {
            total >= 80 -> RecoveryEvaluation(
                readinessScore = total,
                status = ReadinessStatus.OPTIMAL,
                summary = "Full recovery demonstrated. Nervous system and muscular tissue are primed.",
                recommendation = "Execute the prescribed training session with full intensity. Push for progressive overload where appropriate.",
                volumeMultiplier = 1.0f
            )
            total >= 60 -> RecoveryEvaluation(
                readinessScore = total,
                status = ReadinessStatus.ADEQUATE,
                summary = "Adequate recovery. Minor systemic fatigue detected.",
                recommendation = "Train as planned. Maintain strict form and avoid reckless max attempts.",
                volumeMultiplier = 1.0f
            )
            total >= 40 -> RecoveryEvaluation(
                readinessScore = total,
                status = ReadinessStatus.FATIGUED,
                summary = "Noticeable fatigue accumulated. Recovery is falling behind volume.",
                recommendation = "Reduce total sets by 30%. Keep repetitions 2-3 reps shy of failure (RPE <= 7).",
                volumeMultiplier = 0.7f
            )
            else -> RecoveryEvaluation(
                readinessScore = total,
                status = ReadinessStatus.OVERREACHED,
                summary = "Severe recovery deficit. Training today risks injury or regression.",
                recommendation = "Mandatory rest or light mobility walk only. Discipline means respecting recovery.",
                volumeMultiplier = 0.0f
            )
        }
    }
}
