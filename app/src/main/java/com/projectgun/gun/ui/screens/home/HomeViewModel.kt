package com.projectgun.gun.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.local.GunDataStore
import com.projectgun.gun.data.model.Episode
import com.projectgun.gun.data.model.Goal
import com.projectgun.gun.data.model.UserProfile
import com.projectgun.gun.data.repository.*
import com.projectgun.gun.domain.context.ContextEngine
import com.projectgun.gun.domain.context.GunLocalContext
import com.projectgun.gun.domain.recovery.RecoveryEngine
import com.projectgun.gun.domain.recovery.RecoveryEvaluation
import com.projectgun.gun.domain.training.GeneratedWorkoutPlan
import com.projectgun.gun.domain.training.TrainingProgramGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

data class HomeUiState(
    val userProfile: UserProfile? = null,
    val primaryGoal: Goal? = null,
    val currentStreak: Int = 0,
    val completedWorkoutsCount: Int = 0,
    val totalVolumeKg: Float = 0f,
    val todayWorkoutPlan: GeneratedWorkoutPlan? = null,
    val recoveryEvaluation: RecoveryEvaluation? = null,
    val completedDaysThisWeek: Set<DayOfWeek> = emptySet(),
    val latestEpisode: Episode? = null,
    val gunGreeting: String = "Discipline over motivation. State your readiness."
)

class HomeViewModel(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val workoutRepository: WorkoutRepository,
    private val recoveryRepository: RecoveryRepository,
    private val episodeRepository: EpisodeRepository,
    private val dataStore: GunDataStore,
    private val contextEngine: ContextEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            val profile = userRepository.getUserProfileOnce()
            val primaryGoal = goalRepository.getPrimaryGoal()
            val streak = workoutRepository.calculateWeeklyStreak()
            val completedCount = workoutRepository.getCompletedWorkoutCount()
            val totalVolume = workoutRepository.getTotalVolumeMoved()
            val activeSplit = dataStore.activeSplit.first()

            val latestRecovery = recoveryRepository.getLatestRecoveryLogOnce()
            val recoveryEval = RecoveryEngine.evaluate(latestRecovery, consecutiveTrainingDays = streak)

            val todayPlan = TrainingProgramGenerator.generateTodayWorkout(
                userProfile = profile,
                activeSplit = activeSplit,
                recoveryMultiplier = recoveryEval.volumeMultiplier
            )

            // Calculate completed days this week
            val recentWorkouts = workoutRepository.getRecentCompletedWorkouts(14)
            val today = LocalDate.now()
            val startOfWeek = today.with(DayOfWeek.MONDAY)
            val completedDays = recentWorkouts
                .map { LocalDate.ofEpochDay(it.dateEpochDay) }
                .filter { !it.isBefore(startOfWeek) && !it.isAfter(today) }
                .map { it.dayOfWeek }
                .toSet()

            val recentEpisodes = episodeRepository.getRecentEpisodes(1)
            val latestEpisode = recentEpisodes.firstOrNull()

            val name = profile?.name ?: "Trainee"
            val greeting = when {
                streak >= 3 -> "Good rhythm, $name. You are holding a $streak-day streak. Don't let complacency break it."
                completedCount == 0 -> "Welcome to the system, $name. The talking is over. It's time to log your first session."
                else -> "Discipline over motivation, $name. The session is waiting."
            }

            _uiState.value = HomeUiState(
                userProfile = profile,
                primaryGoal = primaryGoal,
                currentStreak = streak,
                completedWorkoutsCount = completedCount,
                totalVolumeKg = totalVolume,
                todayWorkoutPlan = todayPlan,
                recoveryEvaluation = recoveryEval,
                completedDaysThisWeek = completedDays,
                latestEpisode = latestEpisode,
                gunGreeting = greeting
            )
        }
    }

    fun logQuickRecovery(fatigue: Int, sleepHours: Float, soreness: Int, painAreas: List<String>) {
        viewModelScope.launch {
            recoveryRepository.logDailyRecovery(
                fatigueLevel = fatigue,
                sorenessLevel = soreness,
                sleepHours = sleepHours,
                sleepQuality = 4,
                painAreas = painAreas,
                recommendation = "Recovery recorded."
            )
            loadDashboard()
        }
    }
}

class HomeViewModelFactory(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val workoutRepository: WorkoutRepository,
    private val recoveryRepository: RecoveryRepository,
    private val episodeRepository: EpisodeRepository,
    private val dataStore: GunDataStore,
    private val contextEngine: ContextEngine
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            userRepository,
            goalRepository,
            workoutRepository,
            recoveryRepository,
            episodeRepository,
            dataStore,
            contextEngine
        ) as T
    }
}
