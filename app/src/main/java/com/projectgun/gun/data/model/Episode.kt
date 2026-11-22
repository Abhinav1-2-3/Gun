package com.projectgun.gun.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class Episode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val type: EpisodeType,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metricName: String? = null,
    val metricValue: String? = null,
    val significance: Int = 5 // 1 to 10 scale
)
