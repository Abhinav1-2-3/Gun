package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sessions",
    indices = [Index(value = ["dateEpochDay"])]
)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val splitType: String,
    val dateEpochDay: Long, // LocalDate.toEpochDay()
    val startTimeMs: Long = System.currentTimeMillis(),
    val endTimeMs: Long? = null,
    val durationMinutes: Int = 0,
    val isCompleted: Boolean = false,
    val totalVolumeKg: Float = 0f,
    val averageRpe: Float? = null,
    val notes: String? = null
)

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["id"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workoutSessionId"]),
        Index(value = ["exerciseId"])
    ]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val workoutSessionId: Long,
    val exerciseId: String,
    val exerciseName: String,
    val setOrder: Int,
    val targetReps: Int,
    val actualReps: Int = 0,
    val weightKg: Float = 0f,
    val rpe: Float = 7.0f,
    val isCompleted: Boolean = false,
    val isPr: Boolean = false
)
