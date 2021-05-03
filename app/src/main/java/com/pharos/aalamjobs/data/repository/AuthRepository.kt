package com.pharos.aalamjobs.data.repository

import com.pharos.aalamjobs.data.TokenObtainPair
import com.pharos.aalamjobs.data.UserPreferences
import com.pharos.aalamjobs.data.network.AuthApi

class AuthRepository (
    private val api: AuthApi,
    private val preferences: UserPreferences
        ) : BaseRepository(){

                suspend fun login(tokenObtainPair: TokenObtainPair) = safeApiCall {
                api.login(tokenObtainPair)
            }

    suspend fun saveAuthToken(tokenAccess: String/*, tokenRefresh: String*/){
        preferences.saveAuthToken(tokenAccess/*, tokenRefresh*/)
    }
        }