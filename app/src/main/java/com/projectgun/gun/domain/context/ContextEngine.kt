package com.projectgun.gun.domain.context

import com.projectgun.gun.data.model.Episode
import com.projectgun.gun.data.model.Goal
import com.projectgun.gun.data.model.UserProfile
import com.projectgun.gun.data.repository.*
import java.time.LocalDate

data class GunLocalContext(
    val userProfile: UserProfile?,
    val primaryGoal: Goal?,
    val completedWorkoutsCount: Int,
    val totalVolumeKg: Float,
    val currentStreak: Int,
    val lastWorkoutTitle: String?,
    val lastWorkoutDaysAgo: Long?,
    val topPrSummary: String?,
    val readinessScore: Int,
    val recoveryStatusText: String,
    val hasActivePain: Boolean,
    val activePainAreas: List<String>,
    val recentEpisodes: List<Episode>
)

class ContextEngine(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val workoutRepository: WorkoutRepository,
    private val recoveryRepository: RecoveryRepository,
    private val episodeRepository: EpisodeRepository
) {
    suspend fun assembleContext(): GunLocalContext {
        val profile = userRepository.getUserProfileOnce()
        val primaryGoal = goalRepository.getPrimaryGoal()
        val completedCount = workoutRepository.getCompletedWorkoutCount()
        val totalVolume = workoutRepository.getTotalVolumeMoved()
        val streak = workoutRepository.calculateWeeklyStreak()

        val lastSession = workoutRepository.getLastCompletedWorkout()
        val todayEpoch = LocalDate.now().toEpochDay()
        val daysAgo = lastSession?.let { todayEpoch - it.dateEpochDay }

        val prSets = workoutRepository.getAllPrSets()
        val topPr = prSets.firstOrNull()
        val topPrSummary = topPr?.let { "${it.exerciseName} at ${it.weightKg} kg" }

        val latestRecovery = recoveryRepository.getLatestRecoveryLogOnce()
        val readinessScore = if (latestRecovery != null) {
            val sleepPts = ((latestRecovery.sleepHours / 8f) * 40f).coerceIn(0f, 40f)
            val fatiguePts = ((5 - latestRecovery.fatigueLevel) / 4f * 30f).coerceIn(0f, 30f)
            val sorenessPts = ((5 - latestRecovery.sorenessLevel) / 4f * 30f).coerceIn(0f, 30f)
            (sleepPts + fatiguePts + sorenessPts).toInt().coerceIn(0, 100)
        } else {
            80
        }

        val hasPain = latestRecovery?.painAreas?.isNotEmpty() == true
        val painAreas = latestRecovery?.painAreas ?: emptyList()
        val recoveryStatus = when {
            hasPain -> "INJURY/PAIN FLAGGED"
            readinessScore >= 80 -> "OPTIMAL"
            readinessScore >= 60 -> "ADEQUATE"
            readinessScore >= 40 -> "FATIGUED"
            else -> "EXHAUSTED"
        }

        val episodes = episodeRepository.getRecentEpisodes(5)

        return GunLocalContext(
            userProfile = profile,
            primaryGoal = primaryGoal,
            completedWorkoutsCount = completedCount,
            totalVolumeKg = totalVolume,
            currentStreak = streak,
            lastWorkoutTitle = lastSession?.title,
            lastWorkoutDaysAgo = daysAgo,
            topPrSummary = topPrSummary,
            readinessScore = readinessScore,
            recoveryStatusText = recoveryStatus,
            hasActivePain = hasPain,
            activePainAreas = painAreas,
            recentEpisodes = episodes
        )
    }
}
