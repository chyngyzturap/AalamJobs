package com.pharos.aalamjobs.ui.base

import androidx.lifecycle.ViewModel
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.BaseRepository


abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(api: AuthApi)
    = repository.logout(api)
}