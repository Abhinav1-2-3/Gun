package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.ExerciseDao
import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.domain.training.ExerciseCatalog
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {
    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()

    suspend fun ensureSeeded() {
        val count = exerciseDao.getExerciseCount()
        if (count == 0) {
            exerciseDao.insertExercises(ExerciseCatalog.allExercises)
        }
    }

    suspend fun getExerciseById(id: String): Exercise? {
        val fromDb = exerciseDao.getExerciseById(id)
        return fromDb ?: ExerciseCatalog.getById(id)
    }

    suspend fun getAllExercisesOnce(): List<Exercise> {
        val list = exerciseDao.getAllExercisesOnce()
        return if (list.isNotEmpty()) list else ExerciseCatalog.allExercises
    }
}
