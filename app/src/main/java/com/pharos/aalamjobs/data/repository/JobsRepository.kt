package com.pharos.aalamjobs.data.repository

import com.pharos.aalamjobs.data.network.JobsApi

class JobsRepository (
    private val api: JobsApi
    ) : BaseRepository() {

        suspend fun getJobs() = safeApiCall {
            api.getJobs()
        }
}