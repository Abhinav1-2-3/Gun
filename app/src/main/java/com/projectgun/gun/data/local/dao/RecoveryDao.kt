package com.projectgun.gun.data.local.dao

import androidx.room.*
import com.projectgun.gun.data.model.RecoveryLog
import kotlinx.coroutines.flow.Flow

@Dao
interface RecoveryDao {
    @Query("SELECT * FROM recovery_logs ORDER BY dateEpochDay DESC")
    fun getAllRecoveryLogs(): Flow<List<RecoveryLog>>

    @Query("SELECT * FROM recovery_logs ORDER BY dateEpochDay DESC LIMIT 1")
    fun getLatestRecoveryLogFlow(): Flow<RecoveryLog?>

    @Query("SELECT * FROM recovery_logs ORDER BY dateEpochDay DESC LIMIT 1")
    suspend fun getLatestRecoveryLog(): RecoveryLog?

    @Query("SELECT * FROM recovery_logs WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getRecoveryLogForDate(epochDay: Long): RecoveryLog?

    @Query("SELECT * FROM recovery_logs ORDER BY dateEpochDay DESC LIMIT :limit")
    suspend fun getRecentRecoveryLogs(limit: Int): List<RecoveryLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecoveryLog(log: RecoveryLog): Long

    @Query("DELETE FROM recovery_logs")
    suspend fun clearRecoveryLogs()
}
