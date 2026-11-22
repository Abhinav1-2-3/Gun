package com.projectgun.gun

import android.app.Application
import com.projectgun.gun.data.local.GunDataStore
import com.projectgun.gun.data.local.GunDatabase
import com.projectgun.gun.data.repository.*
import com.projectgun.gun.domain.context.ContextEngine
import com.projectgun.gun.domain.personality.PersonalityEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class GunApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { GunDatabase.getInstance(this) }
    val dataStore by lazy { GunDataStore(this) }

    val userRepository by lazy { UserRepository(database.userDao()) }
    val goalRepository by lazy { GoalRepository(database.goalDao()) }
    val episodeRepository by lazy { EpisodeRepository(database.episodeDao()) }
    val exerciseRepository by lazy { ExerciseRepository(database.exerciseDao()) }
    val workoutRepository by lazy { WorkoutRepository(database.workoutDao(), episodeRepository) }
    val recoveryRepository by lazy { RecoveryRepository(database.recoveryDao(), episodeRepository) }
    val chatRepository by lazy { ChatRepository(database.chatDao()) }

    val contextEngine by lazy {
        ContextEngine(
            userRepository = userRepository,
            goalRepository = goalRepository,
            workoutRepository = workoutRepository,
            recoveryRepository = recoveryRepository,
            episodeRepository = episodeRepository
        )
    }

    val personalityEngine by lazy { PersonalityEngine() }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            exerciseRepository.ensureSeeded()
        }
    }
}
