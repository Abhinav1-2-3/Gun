package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recovery_logs",
    indices = [Index(value = ["dateEpochDay"], unique = true)]
)
data class RecoveryLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateEpochDay: Long,
    val fatigueLevel: Int, // 1 to 5
    val sorenessLevel: Int, // 1 to 5
    val sleepHours: Float,
    val sleepQuality: Int, // 1 to 5
    val painAreas: List<String> = emptyList(),
    val gunRecommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)
