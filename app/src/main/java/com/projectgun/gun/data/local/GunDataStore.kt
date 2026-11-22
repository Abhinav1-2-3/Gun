package com.projectgun.gun.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gun_preferences")

class GunDataStore(private val context: Context) {

    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val STRICTNESS = intPreferencesKey("personality_strictness")
        val DIRECTNESS = intPreferencesKey("personality_directness")
        val ENCOURAGEMENT = intPreferencesKey("personality_encouragement")
        val CHALLENGE_INTENSITY = intPreferencesKey("personality_challenge_intensity")
        val EMOTIONAL_RESTRAINT = intPreferencesKey("personality_emotional_restraint")
        val ACTIVE_SPLIT = stringPreferencesKey("active_split")
        val DEFAULT_REST_TIMER_SEC = intPreferencesKey("default_rest_timer_sec")
        val AUTO_START_REST_TIMER = booleanPreferencesKey("auto_start_rest_timer")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED] ?: false
    }

    val strictness: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[STRICTNESS] ?: 85
    }

    val directness: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DIRECTNESS] ?: 90
    }

    val activeSplit: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ACTIVE_SPLIT] ?: "FULL_BODY"
    }

    val defaultRestTimerSec: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DEFAULT_REST_TIMER_SEC] ?: 90
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun updatePersonality(strictnessVal: Int, directnessVal: Int, challengeVal: Int) {
        context.dataStore.edit { preferences ->
            preferences[STRICTNESS] = strictnessVal
            preferences[DIRECTNESS] = directnessVal
            preferences[CHALLENGE_INTENSITY] = challengeVal
        }
    }

    suspend fun setActiveSplit(split: String) {
        context.dataStore.edit { preferences ->
            preferences[ACTIVE_SPLIT] = split
        }
    }

    suspend fun setDefaultRestTimerSec(seconds: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_REST_TIMER_SEC] = seconds
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
