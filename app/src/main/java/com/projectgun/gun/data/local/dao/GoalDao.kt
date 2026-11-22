package com.projectgun.gun.data.local.dao

import androidx.room.*
import com.projectgun.gun.data.model.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY isPrimary DESC, createdAt DESC")
    fun getAllGoals(): Flow<List<Goal>>

    @Query("SELECT * FROM goals WHERE isPrimary = 1 LIMIT 1")
    suspend fun getPrimaryGoal(): Goal?

    @Query("SELECT * FROM goals ORDER BY isPrimary DESC, createdAt DESC")
    suspend fun getAllGoalsOnce(): List<Goal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal): Long

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("DELETE FROM goals")
    suspend fun clearGoals()
}
