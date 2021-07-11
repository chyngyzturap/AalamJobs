package com.pharos.aalamjobs.data.local.user

import kotlinx.coroutines.flow.Flow

interface UserDataLocal {

    suspend fun saveUserData(
        username: String,
        role: String,
        token: String,
    )

    suspend fun clearUserData()

    val token: Flow<String?>
    val username: Flow<String?>
    val role: Flow<String?>
}