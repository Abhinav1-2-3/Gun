package com.projectgun.gun.data.repository

import com.projectgun.gun.data.local.dao.UserDao
import com.projectgun.gun.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()

    suspend fun getUserProfileOnce(): UserProfile? = userDao.getUserProfileOnce()

    suspend fun saveProfile(profile: UserProfile) {
        userDao.insertOrUpdate(profile)
    }

    suspend fun clearProfile() {
        userDao.clearProfile()
    }
}
