package com.projectgun.gun.ui.screens.train

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.local.GunDataStore
import com.projectgun.gun.data.local.dao.WorkoutWithSets
import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.data.model.WorkoutSession
import com.projectgun.gun.data.model.WorkoutSet
import com.projectgun.gun.data.repository.ExerciseRepository
import com.projectgun.gun.data.repository.RecoveryRepository
import com.projectgun.gun.data.repository.UserRepository
import com.projectgun.gun.data.repository.WorkoutRepository
import com.projectgun.gun.domain.recovery.RecoveryEngine
import com.projectgun.gun.domain.training.GeneratedWorkoutPlan
import com.projectgun.gun.domain.training.TrainingProgramGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TrainUiState(
    val todayPlan: GeneratedWorkoutPlan? = null,
    val allExercises: List<Exercise> = emptyList(),
    val pastWorkouts: List<WorkoutWithSets> = emptyList(),
    val activeSession: WorkoutSession? = null,
    val activeSets: List<WorkoutSet> = emptyList(),
    val isRestTimerVisible: Boolean = false,
    val restTimerDurationSec: Int = 90,
    val workoutDurationSeconds: Long = 0L,
    val isWorkoutActive: Boolean = false
)

class TrainViewModel(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository,
    private val recoveryRepository: RecoveryRepository,
    private val dataStore: GunDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainUiState())
    val uiState: StateFlow<TrainUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val profile = userRepository.getUserProfileOnce()
            val activeSplit = dataStore.activeSplit.first()
            val streak = workoutRepository.calculateWeeklyStreak()
            val latestRecovery = recoveryRepository.getLatestRecoveryLogOnce()
            val recoveryEval = RecoveryEngine.evaluate(latestRecovery, streak)

            val plan = TrainingProgramGenerator.generateTodayWorkout(
                userProfile = profile,
                activeSplit = activeSplit,
                recoveryMultiplier = recoveryEval.volumeMultiplier
            )

            val exercises = exerciseRepository.getAllExercisesOnce()
            val past = workoutRepository.getRecentCompletedWorkoutsWithSets(15)
            val active = workoutRepository.getActiveWorkoutSession()

            var activeSets: List<WorkoutSet> = emptyList()
            if (active != null) {
                activeSets = workoutRepository.getSetsForSession(active.id).first()
            }

            val defaultRest = dataStore.defaultRestTimerSec.first()

            _uiState.value = TrainUiState(
                todayPlan = plan,
                allExercises = exercises,
                pastWorkouts = past,
                activeSession = active,
                activeSets = activeSets,
                restTimerDurationSec = defaultRest,
                isWorkoutActive = active != null
            )
        }
    }

    fun startWorkout(onStarted: (Long) -> Unit) {
        viewModelScope.launch {
            val plan = _uiState.value.todayPlan ?: return@launch
            val sessionId = workoutRepository.createWorkoutSession(
                title = plan.title,
                splitType = plan.splitType
            )

            // Seed planned sets into database
            val sets = mutableListOf<WorkoutSet>()
            plan.exercises.forEach { item ->
                for (setNum in 1..item.targetSets) {
                    val prevBest = workoutRepository.getBestSetForExercise(item.exercise.id)
                    val initialWeight = prevBest?.weightKg ?: 0f
                    sets.add(
                        WorkoutSet(
                            workoutSessionId = sessionId,
                            exerciseId = item.exercise.id,
                            exerciseName = item.exercise.name,
                            setOrder = setNum,
                            targetReps = item.targetReps,
                            actualReps = item.targetReps,
                            weightKg = initialWeight,
                            rpe = 7.5f,
                            isCompleted = false
                        )
                    )
                }
            }
            workoutRepository.addSetsToSession(sets)
            loadActiveWorkout(sessionId)
            onStarted(sessionId)
        }
    }

    fun loadActiveWorkout(sessionId: Long) {
        viewModelScope.launch {
            val sessionWithSets = workoutRepository.getWorkoutWithSets(sessionId)
            if (sessionWithSets != null) {
                _uiState.value = _uiState.value.copy(
                    activeSession = sessionWithSets.session,
                    activeSets = sessionWithSets.sets,
                    isWorkoutActive = true
                )
            }
        }
    }

    fun toggleSetCompleted(set: WorkoutSet) {
        viewModelScope.launch {
            val newCompleted = !set.isCompleted
            val updated = set.copy(isCompleted = newCompleted)
            workoutRepository.updateSet(updated)

            // Auto-trigger rest timer when a set is marked complete
            if (newCompleted) {
                val exercise = exerciseRepository.getExerciseById(set.exerciseId)
                val restSec = exercise?.defaultRestSec ?: 90
                _uiState.value = _uiState.value.copy(
                    isRestTimerVisible = true,
                    restTimerDurationSec = restSec
                )
            }

            // Refresh active sets
            _uiState.value.activeSession?.let { loadActiveWorkout(it.id) }
        }
    }

    fun updateSetValues(setId: Long, weight: Float, reps: Int, rpe: Float) {
        viewModelScope.launch {
            val existing = _uiState.value.activeSets.find { it.id == setId } ?: return@launch
            val updated = existing.copy(weightKg = weight, actualReps = reps, rpe = rpe)
            workoutRepository.updateSet(updated)
            _uiState.value.activeSession?.let { loadActiveWorkout(it.id) }
        }
    }

    fun dismissRestTimer() {
        _uiState.value = _uiState.value.copy(isRestTimerVisible = false)
    }

    fun finishWorkout(notes: String?, onFinished: () -> Unit) {
        viewModelScope.launch {
            val sessionId = _uiState.value.activeSession?.id ?: return@launch
            workoutRepository.completeWorkoutSession(sessionId, notes)
            _uiState.value = _uiState.value.copy(
                activeSession = null,
                activeSets = emptyList(),
                isWorkoutActive = false
            )
            loadData()
            onFinished()
        }
    }
}

class TrainViewModelFactory(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository,
    private val recoveryRepository: RecoveryRepository,
    private val dataStore: GunDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrainViewModel(
            workoutRepository,
            exerciseRepository,
            userRepository,
            recoveryRepository,
            dataStore
        ) as T
    }
}
