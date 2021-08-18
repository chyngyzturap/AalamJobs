package com.pharos.aalamjobs.data.repository

import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.model.TokenRefresh
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.ui.jobs.model.JobId
import okhttp3.MultipartBody

class JobsRepository(
    private val api: JobsApi
) : BaseRepository() {
    private val preferences: UserPreferences? = null

    suspend fun uploadPhoto(id: Int, photo: MultipartBody.Part?) = safeApiCall {
        api.uploadImage(id, photo)
    }

    suspend fun getJobs(page: Int, search: String) = safeApiCall {
        api.getJobs(search, page)
    }
    suspend fun verify(verify: TokenAccess) = safeApiCall {
        api.verify(verify)
    }
    suspend fun refresh(refresh: TokenRefresh) = safeApiCall {
        api.refresh(refresh)
    }

    suspend fun getApplied() = safeApiCall {
        api.getApplied()
    }

    suspend fun getJobsFiltered(options: Map<String, String>) = safeApiCall {
        api.getJobsFiltered(options)
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

    suspend fun setFavorite(jobId: JobId) = safeApiCall {
        api.setFavorite(jobId)
    }

    suspend fun deleteFavorite(jobId: Int) = safeApiCall {
        api.deleteFavorite(jobId)
    }

    suspend fun saveAuthToken(tokenAccess: String) {
        preferences?.saveAccessToken(tokenAccess)
    }
}