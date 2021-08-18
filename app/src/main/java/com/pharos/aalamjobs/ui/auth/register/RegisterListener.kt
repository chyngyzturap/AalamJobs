package com.pharos.aalamjobs.ui.auth.register

interface RegisterListener {
    fun createUserSuccess(username: String)
    fun createUserFailed(code: Int?)
}