package com.pharos.aalamjobs.ui.base

import androidx.lifecycle.ViewModel
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.BaseRepository


abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {
    suspend fun logout(api: JobsApi) = repository.logout(api)
}