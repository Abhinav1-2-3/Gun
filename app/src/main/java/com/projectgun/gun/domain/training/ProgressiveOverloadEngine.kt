package com.projectgun.gun.domain.training

import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.data.model.ExerciseCategory
import com.projectgun.gun.data.model.WorkoutSet

data class ProgressionAdvice(
    val recommendedWeightKg: Float,
    val recommendedReps: Int,
    val isWeightIncrease: Boolean,
    val message: String
)

object ProgressiveOverloadEngine {

    /**
     * Epley Formula for 1-Rep Max estimation:
     * 1RM = Weight * (1 + Reps / 30)
     */
    fun calculateEstimated1RM(weightKg: Float, reps: Int): Float {
        if (weightKg <= 0f || reps <= 0) return 0f
        if (reps == 1) return weightKg
        val oneRm = weightKg * (1f + reps / 30f)
        return (Math.round(oneRm * 10f) / 10f)
    }

    /**
     * Evaluates recent performance for an exercise and advises on the next session.
     */
    fun evaluateProgression(
        exercise: Exercise,
        recentSets: List<WorkoutSet>
    ): ProgressionAdvice {
        val completedSets = recentSets.filter { it.isCompleted && it.actualReps > 0 }
        if (completedSets.isEmpty()) {
            return ProgressionAdvice(
                recommendedWeightKg = 0f,
                recommendedReps = exercise.minReps,
                isWeightIncrease = false,
                message = "Establish your baseline. Start with a conservative weight and record clean reps."
            )
        }

        val lastSet = completedSets.last()
        val currentWeight = lastSet.weightKg
        val currentReps = lastSet.actualReps
        val avgRpe = completedSets.map { it.rpe }.average().toFloat()
        val hitTopReps = completedSets.all { it.actualReps >= exercise.maxReps }

        val isLowerBody = exercise.category == ExerciseCategory.LEGS
        val weightIncrement = if (isLowerBody) 5.0f else 2.5f

        return when {
            // Completed all sets at or above the top rep range with good reserve (RPE <= 8)
            hitTopReps && avgRpe <= 8.5f -> {
                val nextWeight = currentWeight + weightIncrement
                ProgressionAdvice(
                    recommendedWeightKg = nextWeight,
                    recommendedReps = exercise.minReps,
                    isWeightIncrease = true,
                    message = "Standard met. You completed all sets at top reps with controlled effort. Increase load by +$weightIncrement kg next session."
                )
            }
            // Reps fell below the minimum range
            currentReps < exercise.minReps -> {
                ProgressionAdvice(
                    recommendedWeightKg = currentWeight,
                    recommendedReps = exercise.minReps,
                    isWeightIncrease = false,
                    message = "Reps were below the target threshold ($currentReps < ${exercise.minReps}). Maintain current load until every rep meets strict standards."
                )
            }
            // High RPE / struggling at top range
            avgRpe >= 9.5f -> {
                ProgressionAdvice(
                    recommendedWeightKg = currentWeight,
                    recommendedReps = currentReps,
                    isWeightIncrease = false,
                    message = "RPE approached maximum failure. Do not increase weight yet. Consolidate your form and build volume at this weight."
                )
            }
            // In the middle of the rep range
            else -> {
                ProgressionAdvice(
                    recommendedWeightKg = currentWeight,
                    recommendedReps = (currentReps + 1).coerceAtMost(exercise.maxReps),
                    isWeightIncrease = false,
                    message = "Double progression active. Add 1 repetition with current weight before moving up."
                )
            }
        }
    }
}
