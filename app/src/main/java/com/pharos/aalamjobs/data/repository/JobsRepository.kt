package com.pharos.aalamjobs.data.repository

import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.ui.jobs.model.JobId

class JobsRepository (
    private val api: JobsApi
    ) : BaseRepository() {

        suspend fun getJobs(page: Int, search: String) = safeApiCall {
            api.getJobs(search, page)
        }
        suspend fun getJobsFiltered(page: Int, country: String, city: String, sector: String) = safeApiCall {
            api.getJobsFiltered(page, country, city, sector)
        }
        suspend fun getFavoriteJobs(page: Int) = safeApiCall {
            api.getFavoriteJobs()
        }

        suspend fun getCountries() = safeApiCall {
            api.getCountries()
        }

        suspend fun getCities() = safeApiCall {
            api.getCities()
        }

        suspend fun getSectors() = safeApiCall {
            api.getSectors()
        }
    suspend fun getCurrencies() = safeApiCall {
        api.getCurrencies()
    }
    suspend fun getEmploymentTypes() = safeApiCall {
        api.getEmploymentTypes()
    }


    suspend fun getJobById(jobId: Int) = safeApiCall {
        api.getJobById(jobId)
    }

    suspend fun setFavorite(jobId: JobId ) = safeApiCall {
        api.setFavorite(jobId)
    }

    suspend fun setFavoriteFromDetail (jobId: JobId, token: String) = safeApiCall {
        api.setFavoriteFromDetail(token, jobId)
    }

    suspend fun deleteFavorite(jobId: Int) = safeApiCall {
        api.deleteFavorite(jobId)
    }

    suspend fun deleteFavoriteFromDetail(jobId: Int, token: String) = safeApiCall {
        api.deleteFavoriteFromDetail(jobId, token)
    }
}