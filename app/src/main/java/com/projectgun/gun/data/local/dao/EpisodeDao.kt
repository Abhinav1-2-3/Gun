package com.projectgun.gun.data.local.dao

import androidx.room.*
import com.projectgun.gun.data.model.Episode
import com.projectgun.gun.data.model.EpisodeType
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM episodes ORDER BY timestamp DESC")
    fun getAllEpisodes(): Flow<List<Episode>>

    @Query("SELECT * FROM episodes ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentEpisodes(limit: Int = 10): List<Episode>

    @Query("SELECT * FROM episodes WHERE type = :type ORDER BY timestamp DESC")
    suspend fun getEpisodesByType(type: EpisodeType): List<Episode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: Episode): Long

    @Delete
    suspend fun deleteEpisode(episode: Episode)

    @Query("DELETE FROM episodes WHERE id = :id")
    suspend fun deleteEpisodeById(id: Long)

    @Query("DELETE FROM episodes")
    suspend fun clearEpisodes()
}
