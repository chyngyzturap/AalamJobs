package com.pharos.aalamjobs.ui.auth.utils

import com.pharos.aalamjobs.data.responses.UserResponse


interface UserListener {

    fun setUserData(userResponse: UserResponse)
    fun dataError(code: Int?)

    fun setUserId(id: Int?, logo: String?)

    fun updateUserSuccess()

    fun quitDone()
}