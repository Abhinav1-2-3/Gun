package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.RecoveryDao
import com.projectgun.gun.data.model.EpisodeType
import com.projectgun.gun.data.model.RecoveryLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class RecoveryRepository(
    private val recoveryDao: RecoveryDao,
    private val episodeRepository: EpisodeRepository
) {
    val allRecoveryLogs: Flow<List<RecoveryLog>> = recoveryDao.getAllRecoveryLogs()
    val latestRecoveryLog: Flow<RecoveryLog?> = recoveryDao.getLatestRecoveryLogFlow()

    suspend fun getLatestRecoveryLogOnce(): RecoveryLog? = recoveryDao.getLatestRecoveryLog()

    suspend fun getRecentRecoveryLogs(limit: Int = 7): List<RecoveryLog> =
        recoveryDao.getRecentRecoveryLogs(limit)

    suspend fun logDailyRecovery(
        fatigueLevel: Int, // 1 to 5
        sorenessLevel: Int, // 1 to 5
        sleepHours: Float,
        sleepQuality: Int, // 1 to 5
        painAreas: List<String>,
        recommendation: String
    ): Long {
        val todayEpochDay = LocalDate.now().toEpochDay()
        val log = RecoveryLog(
            dateEpochDay = todayEpochDay,
            fatigueLevel = fatigueLevel,
            sorenessLevel = sorenessLevel,
            sleepHours = sleepHours,
            sleepQuality = sleepQuality,
            painAreas = painAreas,
            gunRecommendation = recommendation
        )

        if (painAreas.isNotEmpty()) {
            episodeRepository.recordEpisode(
                type = EpisodeType.INJURY_REPORT,
                title = "Discomfort/Pain Noted",
                description = "Reported discomfort in: ${painAreas.joinToString(", ")}. Training volume adjusted.",
                significance = 7
            )
        }

        return recoveryDao.insertRecoveryLog(log)
    }

    suspend fun clearRecoveryLogs() = recoveryDao.clearRecoveryLogs()
}
