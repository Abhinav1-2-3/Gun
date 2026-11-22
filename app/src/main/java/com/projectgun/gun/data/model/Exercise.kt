package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val movementPattern: MovementPattern,
    val primaryMuscle: String,
    val secondaryMuscles: List<String> = emptyList(),
    val equipmentRequired: EquipmentType,
    val difficulty: DifficultyLevel = DifficultyLevel.BEGINNER,
    val instructions: List<String> = emptyList(),
    val defaultSets: Int = 3,
    val minReps: Int = 8,
    val maxReps: Int = 12,
    val defaultRestSec: Int = 90,
    val safetyTips: List<String> = emptyList(),
    val alternatives: List<String> = emptyList()
)
