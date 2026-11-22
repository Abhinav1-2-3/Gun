package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Long = 1L,
    val name: String,
    val age: Int,
    val heightCm: Float,
    val weightKg: Float,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val fitnessLevel: FitnessLevel = FitnessLevel.BEGINNER,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
    val trainingLocation: TrainingLocation = TrainingLocation.COMMERCIAL_GYM,
    val availableEquipment: List<String> = emptyList(),
    val preferredDaysPerWeek: Int = 3,
    val preferredSessionDurationMin: Int = 45,
    val injuriesAndLimitations: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
