package com.pharos.aalamjobs.data.model

data class ChangePasswordModel(
    val current_password: String,
    val new_password: String
)