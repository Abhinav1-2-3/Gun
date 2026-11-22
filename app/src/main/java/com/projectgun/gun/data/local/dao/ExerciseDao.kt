package com.projectgun.gun.data.local.dao

import androidx.room.*
import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.data.model.ExerciseCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    suspend fun getAllExercisesOnce(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getExerciseById(id: String): Exercise?

    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name ASC")
    suspend fun getExercisesByCategory(category: ExerciseCategory): List<Exercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int
}
