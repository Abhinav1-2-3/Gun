package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.WorkoutDao
import com.projectgun.gun.data.local.dao.WorkoutWithSets
import com.projectgun.gun.data.model.EpisodeType
import com.projectgun.gun.data.model.WorkoutSession
import com.projectgun.gun.data.model.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val episodeRepository: EpisodeRepository
) {
    val allWorkoutsWithSets: Flow<List<WorkoutWithSets>> = workoutDao.getAllWorkoutsWithSets()

    suspend fun getActiveWorkoutSession(): WorkoutSession? = workoutDao.getActiveWorkoutSession()

    suspend fun getWorkoutWithSets(sessionId: Long): WorkoutWithSets? =
        workoutDao.getWorkoutWithSetsById(sessionId)

    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>> =
        workoutDao.getSetsForSession(sessionId)

    suspend fun createWorkoutSession(
        title: String,
        splitType: String
    ): Long {
        val todayEpochDay = LocalDate.now().toEpochDay()
        val session = WorkoutSession(
            title = title,
            splitType = splitType,
            dateEpochDay = todayEpochDay,
            startTimeMs = System.currentTimeMillis()
        )
        return workoutDao.insertSession(session)
    }

    suspend fun addSetsToSession(sets: List<WorkoutSet>) {
        workoutDao.insertSets(sets)
    }

    suspend fun updateSet(set: WorkoutSet) {
        var updated = set
        if (set.isCompleted && set.weightKg > 0f) {
            val prevMax = workoutDao.getMaxWeightForExercise(set.exerciseId) ?: 0f
            if (set.weightKg > prevMax) {
                updated = set.copy(isPr = true)
                episodeRepository.recordEpisode(
                    type = EpisodeType.PERSONAL_RECORD,
                    title = "New PR: ${set.exerciseName}",
                    description = "Achieved ${set.weightKg} kg x ${set.actualReps} reps.",
                    metricName = set.exerciseName,
                    metricValue = "${set.weightKg} kg",
                    significance = 8
                )
            }
        }
        workoutDao.updateSet(updated)
    }

    suspend fun completeWorkoutSession(
        sessionId: Long,
        notes: String? = null
    ) {
        val sessionWithSets = workoutDao.getWorkoutWithSetsById(sessionId) ?: return
        val session = sessionWithSets.session
        val sets = sessionWithSets.sets.filter { it.isCompleted }

        val totalVolume = sets.sumOf { (it.weightKg * it.actualReps).toDouble() }.toFloat()
        val avgRpe = if (sets.isNotEmpty()) {
            sets.map { it.rpe }.average().toFloat()
        } else null

        val durationMinutes = ((System.currentTimeMillis() - session.startTimeMs) / 60000).toInt().coerceAtLeast(1)

        val completed = session.copy(
            isCompleted = true,
            endTimeMs = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            totalVolumeKg = totalVolume,
            averageRpe = avgRpe,
            notes = notes
        )
        workoutDao.updateSession(completed)

        // Check if this was the first completed workout
        val totalCompleted = workoutDao.getCompletedWorkoutCount()
        if (totalCompleted == 1) {
            episodeRepository.recordEpisode(
                type = EpisodeType.FIRST_WORKOUT,
                title = "First Blood: Initial Workout Completed",
                description = "Completed '${session.title}' with $totalVolume kg total volume.",
                metricName = "Total Volume",
                metricValue = "$totalVolume kg",
                significance = 9
            )
        } else {
            episodeRepository.recordEpisode(
                type = EpisodeType.MILESTONE,
                title = "Workout Logged: ${session.title}",
                description = "Logged session: $totalVolume kg volume, duration ${durationMinutes}m, average RPE ${"%.1f".format(avgRpe ?: 7f)}.",
                metricName = "Volume",
                metricValue = "$totalVolume kg",
                significance = 4
            )
        }
    }

    suspend fun getLastCompletedWorkout(): WorkoutSession? = workoutDao.getLastCompletedWorkout()

    suspend fun getRecentCompletedWorkouts(limit: Int = 10): List<WorkoutSession> =
        workoutDao.getRecentCompletedWorkouts(limit)

    suspend fun getRecentCompletedWorkoutsWithSets(limit: Int = 10): List<WorkoutWithSets> =
        workoutDao.getRecentCompletedWorkoutsWithSets(limit)

    suspend fun getCompletedWorkoutCount(): Int = workoutDao.getCompletedWorkoutCount()

    suspend fun getTotalVolumeMoved(): Float = workoutDao.getTotalVolumeMoved() ?: 0f

    suspend fun getBestSetForExercise(exerciseId: String): WorkoutSet? =
        workoutDao.getBestSetForExercise(exerciseId)

    suspend fun getAllPrSets(): List<WorkoutSet> = workoutDao.getAllPrSets()

    suspend fun deleteSessionById(id: Long) = workoutDao.deleteSessionById(id)

    suspend fun clearAllWorkouts() = workoutDao.clearSessions()

    suspend fun calculateWeeklyStreak(): Int {
        val completed = workoutDao.getRecentCompletedWorkouts(30)
        if (completed.isEmpty()) return 0

        val today = LocalDate.now()
        val daysWithWorkouts = completed.map { LocalDate.ofEpochDay(it.dateEpochDay) }.toSet()

        var streak = 0
        var current = today
        // Allow streak to count if worked out today or yesterday
        if (!daysWithWorkouts.contains(current)) {
            current = current.minusDays(1)
        }
        while (daysWithWorkouts.contains(current)) {
            streak++
            current = current.minusDays(1)
        }
        return streak
    }
}
