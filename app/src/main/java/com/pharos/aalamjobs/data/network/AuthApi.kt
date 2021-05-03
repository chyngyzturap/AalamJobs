package com.pharos.aalamjobs.data.network

import com.pharos.aalamjobs.data.TokenObtainPair
import com.pharos.aalamjobs.data.responses.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/jwt/create")
    suspend fun login(@Body tokenObtainPair: TokenObtainPair) : LoginResponse
}