package com.projectgun.gun.domain.training

import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.data.model.UserProfile
import com.projectgun.gun.domain.recovery.SafetyEngine

data class GeneratedWorkoutPlan(
    val title: String,
    val splitType: String,
    val estimatedDurationMin: Int,
    val exercises: List<ExercisePlanItem>
)

data class ExercisePlanItem(
    val exercise: Exercise,
    val targetSets: Int,
    val targetReps: Int,
    val defaultWeightKg: Float,
    val restSec: Int
)

object TrainingProgramGenerator {

    fun generateTodayWorkout(
        userProfile: UserProfile?,
        activeSplit: String,
        dayOfWeek: Int = java.time.LocalDate.now().dayOfWeek.value, // 1 = Mon, 7 = Sun
        recoveryMultiplier: Float = 1.0f
    ): GeneratedWorkoutPlan {
        val equipment = userProfile?.availableEquipment ?: listOf("COMMERCIAL_GYM")
        val limitations = userProfile?.injuriesAndLimitations ?: emptyList()

        val split = activeSplit.uppercase()

        val (title, exercisesList) = when (split) {
            "UPPER_LOWER" -> {
                if (dayOfWeek % 2 != 0) {
                    "Upper Body Power & Hypertrophy" to ExerciseCatalog.getRecommendedForSplit("UPPER", equipment)
                } else {
                    "Lower Body & Core Foundation" to ExerciseCatalog.getRecommendedForSplit("LOWER", equipment)
                }
            }
            "PUSH_PULL_LEGS" -> {
                when (dayOfWeek % 3) {
                    1 -> "Push — Chest, Shoulders & Triceps" to ExerciseCatalog.getRecommendedForSplit("PUSH", equipment)
                    2 -> "Pull — Back, Rear Delts & Biceps" to ExerciseCatalog.getRecommendedForSplit("PULL", equipment)
                    else -> "Legs — Quads, Hamstrings & Calves" to ExerciseCatalog.getRecommendedForSplit("LEGS", equipment)
                }
            }
            else -> { // Default FULL_BODY
                val dayLabel = if (dayOfWeek % 2 != 0) "Session A" else "Session B"
                "Full Body Discipline — $dayLabel" to ExerciseCatalog.getRecommendedForSplit("FULL_BODY", equipment)
            }
        }

        // Filter out exercises that violate declared safety limitations
        val safeExercises = exercisesList.filter { exercise ->
            SafetyEngine.isExerciseSafe(exercise.name, exercise.primaryMuscle, limitations)
        }

        val items = safeExercises.map { exercise ->
            val adjustedSets = (exercise.defaultSets * recoveryMultiplier).toInt().coerceAtLeast(2)
            ExercisePlanItem(
                exercise = exercise,
                targetSets = adjustedSets,
                targetReps = exercise.maxReps,
                defaultWeightKg = 0f,
                restSec = exercise.defaultRestSec
            )
        }

        val estimatedMinutes = (items.sumOf { (it.targetSets * (it.restSec + 45)) } / 60).coerceIn(30, 90)

        return GeneratedWorkoutPlan(
            title = title,
            splitType = split,
            estimatedDurationMin = estimatedMinutes,
            exercises = items
        )
    }
}
