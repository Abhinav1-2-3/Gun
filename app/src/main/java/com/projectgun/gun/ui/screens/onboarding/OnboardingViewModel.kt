package com.projectgun.gun.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.local.GunDataStore
import com.projectgun.gun.data.model.*
import com.projectgun.gun.data.repository.EpisodeRepository
import com.projectgun.gun.data.repository.GoalRepository
import com.projectgun.gun.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val currentStep: Int = 0,
    val totalSteps: Int = 5,
    val name: String = "",
    val ageText: String = "24",
    val heightText: String = "178",
    val weightText: String = "75",
    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
    val primaryGoalType: GoalType = GoalType.MUSCLE_BUILDING,
    val trainingLocation: TrainingLocation = TrainingLocation.COMMERCIAL_GYM,
    val selectedEquipment: Set<String> = setOf("Full Gym"),
    val daysPerWeek: Int = 4,
    val sessionDurationMin: Int = 45,
    val injuries: Set<String> = emptySet(),
    val isComplete: Boolean = false
)

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val episodeRepository: EpisodeRepository,
    private val dataStore: GunDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateName(name: String) { _uiState.value = _uiState.value.copy(name = name) }
    fun updateAge(age: String) { _uiState.value = _uiState.value.copy(ageText = age) }
    fun updateHeight(h: String) { _uiState.value = _uiState.value.copy(heightText = h) }
    fun updateWeight(w: String) { _uiState.value = _uiState.value.copy(weightText = w) }
    fun setFitnessLevel(level: FitnessLevel) { _uiState.value = _uiState.value.copy(fitnessLevel = level) }
    fun setGoal(goal: GoalType) { _uiState.value = _uiState.value.copy(primaryGoalType = goal) }
    fun setLocation(loc: TrainingLocation) { _uiState.value = _uiState.value.copy(trainingLocation = loc) }
    fun setDaysPerWeek(days: Int) { _uiState.value = _uiState.value.copy(daysPerWeek = days) }
    fun setDuration(min: Int) { _uiState.value = _uiState.value.copy(sessionDurationMin = min) }

    fun toggleInjury(injury: String) {
        val current = _uiState.value.injuries.toMutableSet()
        if (current.contains(injury)) current.remove(injury) else current.add(injury)
        _uiState.value = _uiState.value.copy(injuries = current)
    }

    fun nextStep() {
        if (_uiState.value.currentStep < _uiState.value.totalSteps - 1) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep + 1)
        } else {
            completeOnboarding()
        }
    }

    fun prevStep() {
        if (_uiState.value.currentStep > 0) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep - 1)
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            val state = _uiState.value
            val profile = UserProfile(
                name = state.name.ifBlank { "Trainee" },
                age = state.ageText.toIntOrNull() ?: 24,
                heightCm = state.heightText.toFloatOrNull() ?: 178f,
                weightKg = state.weightText.toFloatOrNull() ?: 75f,
                fitnessLevel = state.fitnessLevel,
                activityLevel = state.activityLevel,
                trainingLocation = state.trainingLocation,
                availableEquipment = state.selectedEquipment.toList(),
                preferredDaysPerWeek = state.daysPerWeek,
                preferredSessionDurationMin = state.sessionDurationMin,
                injuriesAndLimitations = state.injuries.toList()
            )
            userRepository.saveProfile(profile)

            // Save primary goal
            val goalTitle = when (state.primaryGoalType) {
                GoalType.MUSCLE_BUILDING -> "Build Lean Muscle Mass"
                GoalType.STRENGTH -> "Develop Maximal Raw Strength"
                GoalType.FAT_LOSS -> "Shed Body Fat & Increase Definition"
                GoalType.GENERAL_FITNESS -> "Athletic Foundation & Durability"
                GoalType.ENDURANCE -> "Cardiovascular & Muscular Stamina"
                GoalType.DISCIPLINE -> "Strict Consistency & Mental Resilience"
                GoalType.CUSTOM -> "Personal Transformation"
            }
            goalRepository.addGoal(
                Goal(
                    title = goalTitle,
                    type = state.primaryGoalType,
                    isPrimary = true
                )
            )

            // Set active split based on preferred days
            val recommendedSplit = when {
                state.daysPerWeek <= 3 -> "FULL_BODY"
                state.daysPerWeek == 4 -> "UPPER_LOWER"
                else -> "PUSH_PULL_LEGS"
            }
            dataStore.setActiveSplit(recommendedSplit)

            // Save initial memory episode
            episodeRepository.recordEpisode(
                type = EpisodeType.USER_DECISION,
                title = "Initial Assessment Completed",
                description = "Enrolled under Gun's mentorship. Objective: '$goalTitle'. Split assigned: $recommendedSplit (${state.daysPerWeek} days/week).",
                significance = 10
            )

            dataStore.setOnboardingCompleted(true)
            _uiState.value = _uiState.value.copy(isComplete = true)
        }
    }
}

class OnboardingViewModelFactory(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val episodeRepository: EpisodeRepository,
    private val dataStore: GunDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardingViewModel(userRepository, goalRepository, episodeRepository, dataStore) as T
    }
}
