package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.GoalDao
import com.projectgun.gun.data.model.Goal
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: GoalDao) {
    val allGoals: Flow<List<Goal>> = goalDao.getAllGoals()

    suspend fun getPrimaryGoal(): Goal? = goalDao.getPrimaryGoal()

    suspend fun getAllGoalsOnce(): List<Goal> = goalDao.getAllGoalsOnce()

    suspend fun addGoal(goal: Goal): Long = goalDao.insertGoal(goal)

    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)

    suspend fun deleteGoal(goal: Goal) = goalDao.deleteGoal(goal)

    suspend fun clearGoals() = goalDao.clearGoals()
}
