package com.projectgun.gun.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projectgun.gun.data.local.converters.GunTypeConverters
import com.projectgun.gun.data.local.dao.*
import com.projectgun.gun.data.model.*

@Database(
    entities = [
        UserProfile::class,
        Goal::class,
        Episode::class,
        Exercise::class,
        WorkoutSession::class,
        WorkoutSet::class,
        RecoveryLog::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(GunTypeConverters::class)
abstract class GunDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalDao(): GoalDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun recoveryDao(): RecoveryDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: GunDatabase? = null

        fun getInstance(context: Context): GunDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GunDatabase::class.java,
                    "gun_database.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
