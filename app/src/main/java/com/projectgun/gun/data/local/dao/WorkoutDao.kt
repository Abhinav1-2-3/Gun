package com.projectgun.gun.data.local.dao

import androidx.room.*
import com.projectgun.gun.data.model.WorkoutSession
import com.projectgun.gun.data.model.WorkoutSet
import kotlinx.coroutines.flow.Flow

data class WorkoutWithSets(
    @Embedded val session: WorkoutSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutSessionId"
    )
    val sets: List<WorkoutSet>
)

@Dao
interface WorkoutDao {
    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY startTimeMs DESC")
    fun getAllWorkoutsWithSets(): Flow<List<WorkoutWithSets>>

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getWorkoutWithSetsById(sessionId: Long): WorkoutWithSets?

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY endTimeMs DESC LIMIT 1")
    suspend fun getLastCompletedWorkout(): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY endTimeMs DESC LIMIT :limit")
    suspend fun getRecentCompletedWorkouts(limit: Int): List<WorkoutSession>

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY endTimeMs DESC LIMIT :limit")
    suspend fun getRecentCompletedWorkoutsWithSets(limit: Int): List<WorkoutWithSets>

    @Query("SELECT * FROM workout_sessions WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getWorkoutForDate(epochDay: Long): WorkoutSession?

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY startTimeMs DESC LIMIT 1")
    suspend fun getActiveWorkoutSession(): WorkoutSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSession): Long

    @Update
    suspend fun updateSession(session: WorkoutSession)

    @Delete
    suspend fun deleteSession(session: WorkoutSession)

    @Query("DELETE FROM workout_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)

    @Query("DELETE FROM workout_sessions")
    suspend fun clearSessions()

    // Sets
    @Query("SELECT * FROM workout_sets WHERE workoutSessionId = :sessionId ORDER BY setOrder ASC")
    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>>

    @Query("SELECT * FROM workout_sets WHERE workoutSessionId = :sessionId ORDER BY setOrder ASC")
    suspend fun getSetsForSessionOnce(sessionId: Long): List<WorkoutSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: WorkoutSet): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<WorkoutSet>)

    @Update
    suspend fun updateSet(set: WorkoutSet)

    @Delete
    suspend fun deleteSet(set: WorkoutSet)

    @Query("DELETE FROM workout_sets WHERE id = :id")
    suspend fun deleteSetById(id: Long)

    // PRs and analytics queries
    @Query("SELECT MAX(weightKg) FROM workout_sets WHERE exerciseId = :exerciseId AND isCompleted = 1")
    suspend fun getMaxWeightForExercise(exerciseId: String): Float?

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId AND isCompleted = 1 ORDER BY weightKg DESC, actualReps DESC LIMIT 1")
    suspend fun getBestSetForExercise(exerciseId: String): WorkoutSet?

    @Query("SELECT * FROM workout_sets WHERE isCompleted = 1 AND isPr = 1 ORDER BY id DESC")
    suspend fun getAllPrSets(): List<WorkoutSet>

    @Query("SELECT COUNT(*) FROM workout_sessions WHERE isCompleted = 1")
    suspend fun getCompletedWorkoutCount(): Int

    @Query("SELECT SUM(totalVolumeKg) FROM workout_sessions WHERE isCompleted = 1")
    suspend fun getTotalVolumeMoved(): Float?

    @Query("SELECT * FROM workout_sets WHERE exerciseId = :exerciseId AND isCompleted = 1 ORDER BY id ASC")
    suspend fun getExerciseHistory(exerciseId: String): List<WorkoutSet>
}
