package com.projectgun.gun.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.projectgun.gun.data.model.WorkoutSet
import com.projectgun.gun.data.repository.WorkoutRepository
import com.projectgun.gun.domain.training.ProgressiveOverloadEngine
import com.projectgun.gun.ui.components.VolumeBarData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class PrDisplayItem(
    val exerciseName: String,
    val weightKg: Float,
    val reps: Int,
    val estimated1RM: Float
)

data class ProgressUiState(
    val totalWorkouts: Int = 0,
    val totalVolumeKg: Float = 0f,
    val weeklyStreak: Int = 0,
    val volumeChartData: List<VolumeBarData> = emptyList(),
    val personalRecords: List<PrDisplayItem> = emptyList(),
    val gunProgressObservation: String = "Perform and log your workouts. Gun records results, not promises."
)

class ProgressViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            val totalCount = workoutRepository.getCompletedWorkoutCount()
            val totalVolume = workoutRepository.getTotalVolumeMoved()
            val streak = workoutRepository.calculateWeeklyStreak()

            val recentSessions = workoutRepository.getRecentCompletedWorkouts(7)
            val chartData = recentSessions.reversed().mapIndexed { index, session ->
                val date = LocalDate.ofEpochDay(session.dateEpochDay)
                val label = "${date.dayOfMonth}/${date.monthValue}"
                VolumeBarData(
                    label = label,
                    volumeKg = session.totalVolumeKg
                )
            }

            val prSets = workoutRepository.getAllPrSets()
            // Deduplicate to show the highest record per exercise
            val prItems = prSets.groupBy { it.exerciseId }.map { (_, sets) ->
                val best = sets.maxByOrNull { it.weightKg } ?: sets.first()
                val est1Rm = ProgressiveOverloadEngine.calculateEstimated1RM(best.weightKg, best.actualReps)
                PrDisplayItem(
                    exerciseName = best.exerciseName,
                    weightKg = best.weightKg,
                    reps = best.actualReps,
                    estimated1RM = est1Rm
                )
            }

            val observation = when {
                totalCount == 0 -> "You have no recorded training history. Start logging sets to generate evidence-backed evaluations."
                prItems.isNotEmpty() -> "You have secured ${prItems.size} verified personal records across your lifting history. Keep progressive overload active; do not settle into comfort."
                totalCount >= 5 -> "Your consistency is taking shape. $totalCount sessions recorded with %.0f kg moved. Maintain this standard.".format(totalVolume)
                else -> "Early phase. The foundation is being established. Consistency over intensity."
            }

            _uiState.value = ProgressUiState(
                totalWorkouts = totalCount,
                totalVolumeKg = totalVolume,
                weeklyStreak = streak,
                volumeChartData = chartData,
                personalRecords = prItems,
                gunProgressObservation = observation
            )
        }
    }
}

class ProgressViewModelFactory(
    private val workoutRepository: WorkoutRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProgressViewModel(workoutRepository) as T
    }
}
