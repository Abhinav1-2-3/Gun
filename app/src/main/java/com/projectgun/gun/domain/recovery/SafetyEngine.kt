package com.projectgun.gun.domain.recovery

import com.projectgun.gun.data.model.RecoveryLog

sealed class SafetyCheckResult {
    object Clear : SafetyCheckResult()
    data class Warning(val message: String, val suggestedModification: String) : SafetyCheckResult()
    data class HaltTraining(val reason: String, val mandatoryAction: String) : SafetyCheckResult()
}

object SafetyEngine {

    /**
     * Safety check has supreme authority over all training objectives.
     * Evaluates pain, severe fatigue, or dangerous lack of sleep.
     */
    fun evaluateSafety(
        latestRecovery: RecoveryLog?,
        consecutiveTrainingDays: Int,
        userLimitations: List<String>
    ): SafetyCheckResult {
        if (latestRecovery != null) {
            // Check for reported pain or injury
            if (latestRecovery.painAreas.isNotEmpty()) {
                val painString = latestRecovery.painAreas.joinToString(", ")
                return SafetyCheckResult.HaltTraining(
                    reason = "Active physical pain reported in: $painString.",
                    mandatoryAction = "Do not train through joint or muscular pain. Rest the affected area or seek clinical assessment. Training through injury is stupidity, not strength."
                )
            }

            // Severe fatigue (5/5) or severe sleep deprivation (< 4 hours)
            if (latestRecovery.fatigueLevel >= 5 || latestRecovery.sleepHours < 4f) {
                return SafetyCheckResult.HaltTraining(
                    reason = "Critical systemic exhaustion (Fatigue: ${latestRecovery.fatigueLevel}/5, Sleep: ${latestRecovery.sleepHours} hrs).",
                    mandatoryAction = "High-load lifting in this state causes neuromuscular failure and injury. Mandatory rest or light walking only today."
                )
            }

            // High fatigue (4/5)
            if (latestRecovery.fatigueLevel == 4 || latestRecovery.sorenessLevel >= 4) {
                return SafetyCheckResult.Warning(
                    message = "Elevated systemic fatigue recorded.",
                    suggestedModification = "Reduce working volume by 40% and keep RPE under 7 today. Focus on mobility and technique."
                )
            }
        }

        // More than 3 consecutive high-volume days without a rest day
        if (consecutiveTrainingDays >= 4) {
            return SafetyCheckResult.Warning(
                message = "You have trained for $consecutiveTrainingDays consecutive days without a rest day.",
                suggestedModification = "Hypertrophy and strength are built during recovery, not destruction. Gun recommends an active recovery or rest day today."
            )
        }

        return SafetyCheckResult.Clear
    }

    /**
     * Checks if a specific exercise violates the user's declared physical limitations.
     */
    fun isExerciseSafe(exerciseName: String, primaryMuscle: String, limitations: List<String>): Boolean {
        val lowerName = exerciseName.lowercase()
        val lowerMuscle = primaryMuscle.lowercase()
        for (limitation in limitations) {
            val lim = limitation.lowercase()
            if (lim.contains("shoulder") && (lowerName.contains("press") || lowerMuscle.contains("deltoid"))) return false
            if (lim.contains("knee") && (lowerName.contains("squat") || lowerName.contains("lunge") || lowerName.contains("leg extension"))) return false
            if (lim.contains("lower back") && (lowerName.contains("deadlift") || lowerName.contains("bent-over row") || lowerName.contains("squat"))) return false
            if (lim.contains("wrist") && (lowerName.contains("barbell curl") || lowerName.contains("bench press"))) return false
        }
        return true
    }
}
