package com.projectgun.gun.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.local.GunDataStore
import com.projectgun.gun.data.model.Episode
import com.projectgun.gun.data.model.Goal
import com.projectgun.gun.data.model.UserProfile
import com.projectgun.gun.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileUiState(
    val userProfile: UserProfile? = null,
    val goals: List<Goal> = emptyList(),
    val activeSplit: String = "FULL_BODY",
    val strictness: Int = 85,
    val directness: Int = 90,
    val storedEpisodes: List<Episode> = emptyList(),
    val exportedJson: String? = null
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val episodeRepository: EpisodeRepository,
    private val workoutRepository: WorkoutRepository,
    private val recoveryRepository: RecoveryRepository,
    private val dataStore: GunDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            val profile = userRepository.getUserProfileOnce()
            val goalsList = goalRepository.getAllGoalsOnce()
            val split = dataStore.activeSplit.first()
            val strict = dataStore.strictness.first()
            val direct = dataStore.directness.first()
            val episodes = episodeRepository.getRecentEpisodes(20)

            _uiState.value = ProfileUiState(
                userProfile = profile,
                goals = goalsList,
                activeSplit = split,
                strictness = strict,
                directness = direct,
                storedEpisodes = episodes
            )
        }
    }

    fun updateSplit(split: String) {
        viewModelScope.launch {
            dataStore.setActiveSplit(split)
            _uiState.value = _uiState.value.copy(activeSplit = split)
        }
    }

    fun updatePersonality(strictness: Int, directness: Int) {
        viewModelScope.launch {
            dataStore.updatePersonality(strictness, directness, 80)
            _uiState.value = _uiState.value.copy(strictness = strictness, directness = directness)
        }
    }

    fun deleteEpisode(id: Long) {
        viewModelScope.launch {
            episodeRepository.deleteEpisodeById(id)
            loadProfileData()
        }
    }

    fun exportData() {
        viewModelScope.launch {
            val profile = _uiState.value.userProfile
            val episodes = _uiState.value.storedEpisodes
            val workouts = workoutRepository.getRecentCompletedWorkouts(50)

            val json = buildString {
                append("{\n")
                append("  \"user\": \"${profile?.name}\",\n")
                append("  \"fitnessLevel\": \"${profile?.fitnessLevel}\",\n")
                append("  \"completedWorkouts\": ${workouts.size},\n")
                append("  \"episodes\": [\n")
                episodes.forEachIndexed { i, ep ->
                    append("    {\"title\": \"${ep.title}\", \"type\": \"${ep.type}\", \"date\": ${ep.timestamp}}")
                    if (i < episodes.size - 1) append(",")
                    append("\n")
                }
                append("  ]\n")
                append("}")
            }

            _uiState.value = _uiState.value.copy(exportedJson = json)
        }
    }

    fun clearExport() {
        _uiState.value = _uiState.value.copy(exportedJson = null)
    }

    fun factoryReset(onReset: () -> Unit) {
        viewModelScope.launch {
            userRepository.clearProfile()
            goalRepository.clearGoals()
            episodeRepository.clearEpisodes()
            workoutRepository.clearAllWorkouts()
            recoveryRepository.clearRecoveryLogs()
            dataStore.clearAll()
            onReset()
        }
    }
}

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val episodeRepository: EpisodeRepository,
    private val workoutRepository: WorkoutRepository,
    private val recoveryRepository: RecoveryRepository,
    private val dataStore: GunDataStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            userRepository,
            goalRepository,
            episodeRepository,
            workoutRepository,
            recoveryRepository,
            dataStore
        ) as T
    }
}
