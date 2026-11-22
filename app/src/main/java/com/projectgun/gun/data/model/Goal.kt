package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val type: GoalType = GoalType.GENERAL_FITNESS,
    val targetValue: Float? = null,
    val currentValue: Float? = null,
    val unit: String? = null,
    val targetDate: Long? = null,
    val isPrimary: Boolean = false,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
