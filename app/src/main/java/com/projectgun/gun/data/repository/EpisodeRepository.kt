package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.EpisodeDao
import com.projectgun.gun.data.model.Episode
import com.projectgun.gun.data.model.EpisodeType
import kotlinx.coroutines.flow.Flow

class EpisodeRepository(private val episodeDao: EpisodeDao) {
    val allEpisodes: Flow<List<Episode>> = episodeDao.getAllEpisodes()

    suspend fun getRecentEpisodes(limit: Int = 10): List<Episode> =
        episodeDao.getRecentEpisodes(limit)

    suspend fun getEpisodesByType(type: EpisodeType): List<Episode> =
        episodeDao.getEpisodesByType(type)

    suspend fun recordEpisode(
        type: EpisodeType,
        title: String,
        description: String,
        metricName: String? = null,
        metricValue: String? = null,
        significance: Int = 5
    ): Long {
        val episode = Episode(
            type = type,
            title = title,
            description = description,
            timestamp = System.currentTimeMillis(),
            metricName = metricName,
            metricValue = metricValue,
            significance = significance
        )
        return episodeDao.insertEpisode(episode)
    }

    suspend fun deleteEpisodeById(id: Long) = episodeDao.deleteEpisodeById(id)

    suspend fun clearEpisodes() = episodeDao.clearEpisodes()
}
