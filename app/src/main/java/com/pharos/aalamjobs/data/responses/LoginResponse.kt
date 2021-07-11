package com.pharos.aalamjobs.data.responses

data class LoginResponse(
    val access: String,
    val refresh: String,
    val email: String,
    val fullname: String,
    val username: String,
    val first_name: String,
    val last_name: String,
    val role: String

)