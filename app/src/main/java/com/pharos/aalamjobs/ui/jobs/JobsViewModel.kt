package com.pharos.aalamjobs.ui.jobs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.model.TokenRefresh
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.JobsResponse
import com.pharos.aalamjobs.ui.applied.AppliedListener
import com.pharos.aalamjobs.ui.base.BaseViewModel
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.*
import com.pharos.aalamjobs.utils.CurrencyListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class JobsViewModel(
    private val repository: JobsRepository
) : BaseViewModel(repository) {
    private var currencyListener: CurrencyListener? = null
    private var jobsListener: JobsListener? = null
    private var appliedListener: AppliedListener? = null
    private var favListener: FavoriteListener? = null
    private var countryListener: CountryListener? = null
    private var cityListener: CityListener? = null
    private var sectorListener: SectorListener? = null
    private var emplTypesListener: EmplTypeListener? = null
    private var jobListener: JobListener? = null
    private val userPreferences: UserPreferences? = null
    private val _jobs: MutableLiveData<Resource<JobsResponse>> = MutableLiveData()
    val jobs: MutableLiveData<Resource<JobsResponse>> get() = _jobs

    fun uploadPhoto(id: Int, photo: MultipartBody.Part?) = viewModelScope.launch {
        repository.uploadPhoto(id, photo)
    }

    fun getJobsList(page: Int, search: String) = viewModelScope.launch {
        when (val response = repository.getJobs(page, search)) {
            is Resource.Success -> {
                jobsListener?.setJob(response.value)
            }
            is Resource.Failure -> {
                jobsListener?.getJobError(response.errorCode)
            }
        }
    }
    fun verify(verify: TokenAccess) = viewModelScope.launch {
        when (val response = repository.verify(verify)) {
            is Resource.Success -> {
            }
            is Resource.Failure -> {
                if (response.errorCode == 401) {
                    val tokenRef =  userPreferences?.tokenRefresh?.first()
                    val refresh = TokenRefresh(tokenRef)
                    refresh(refresh)
                }
            }
        }
    }
    private fun refresh(refresh: TokenRefresh) = viewModelScope.launch {
        when (val response = repository.refresh(refresh)) {
            is Resource.Success -> {
                saveAccessToken(response.value.access)
            }
            is Resource.Failure -> {

            }
        }
    }

    private suspend fun saveAccessToken(tokenAccess: String?) {
        viewModelScope.launch {
            if (tokenAccess != null) {
                repository.saveAuthToken(tokenAccess)
            }
        }
    }

    fun getAppliedList() = viewModelScope.launch {
        when (val response = repository.getApplied()) {
            is Resource.Success -> {
                appliedListener?.setApplied(response.value)
            }
            is Resource.Failure -> {
                appliedListener?.getAppliedError(response.errorCode)
            }
        }
    }

    fun getJobsFilteredList(
        options: Map<String, String>
    ) = viewModelScope.launch {
        when (val response = repository.getJobsFiltered(options)) {
            is Resource.Success -> {
                jobsListener?.setJob(response.value)
            }
            is Resource.Failure -> {
                jobsListener?.getJobError(response.errorCode)
            }
        }
    }

    fun getFavJobsList(page: Int) = viewModelScope.launch {
        when (val response = repository.getFavoriteJobs(page)) {
            is Resource.Success -> {
                favListener?.setFavoriteJob(response.value)
            }
            is Resource.Failure -> {
                favListener?.getFavJobError(response.errorCode)
            }
        }
    }

    fun getCountryList() = viewModelScope.launch {
        when (val response = repository.getCountries()) {
            is Resource.Success -> {
                countryListener?.setCountry(response.value)
            }
            is Resource.Failure -> {
                countryListener?.getCountryError(response.errorCode)
            }
        }
    }

    fun getCitiesList() = viewModelScope.launch {
        when (val response = repository.getCities()) {
            is Resource.Success -> {
                cityListener?.setCities(response.value)
            }
            is Resource.Failure -> {
                cityListener?.getCityError(response.errorCode)
            }
        }
    }

    fun getSectorsList() = viewModelScope.launch {
        when (val response = repository.getSectors()) {
            is Resource.Success -> {
                sectorListener?.setSectors(response.value)
            }
            is Resource.Failure -> {
                sectorListener?.getSectorError(response.errorCode)
            }
        }
    }


    fun getCurrencyList() = viewModelScope.launch {
        when (val response = repository.getCurrencies()) {
            is Resource.Success -> {
                currencyListener?.setCurrency(response.value)
            }
            is Resource.Failure -> {
                currencyListener?.getCurrencyError(response.errorCode)
            }
        }
    }

    fun getEmploymentTypes() = viewModelScope.launch {
        when (val response = repository.getEmploymentTypes()) {
            is Resource.Success -> {
                emplTypesListener?.setEmplTypes(response.value)
            }
            is Resource.Failure -> {
                emplTypesListener?.getEmplTypesError(response.errorCode)
            }
        }
    }

    fun setCurrencyListener(listener: CurrencyListener) {
        this.currencyListener = listener
    }

    fun setEmplTypesListener(listener: EmplTypeListener) {
        this.emplTypesListener = listener
    }


    fun setJobsListener(listener: JobsListener) {
        this.jobsListener = listener
    }

    fun setAppliedListener(listener: AppliedListener) {
        this.appliedListener = listener
    }

    fun setCountryListener(listener: CountryListener) {
        this.countryListener = listener
    }

    fun setCityListener(listener: CityListener) {
        this.cityListener = listener
    }

    fun setSectorListener(listener: SectorListener) {
        this.sectorListener = listener
    }

    fun setFavJobsListener(listener: FavoriteListener) {
        this.favListener = listener
    }

    fun setJobListener(listener: JobListener) {
        this.jobListener = listener
    }

    fun getJobData(jobId: Int) = viewModelScope.launch {
        when (val response = repository.getJobById(jobId)) {
            is Resource.Success -> {
                jobListener?.setJob(response.value)
            }
            is Resource.Failure -> {
                jobListener?.getJobError(response.errorCode)
            }
        }
    }

    fun setFavorite(jobId: JobId) = viewModelScope.launch {
        when (val response = repository.setFavorite(jobId)) {
            is Resource.Success -> {
                favListener?.postFavJobSuccess()
            }
            is Resource.Failure -> {
                favListener?.addToFavFailed(response.errorCode)
            }
        }
    }

    fun deleteFromFav(jobId: Int) = viewModelScope.launch {
        when (val response = repository.deleteFavorite(jobId)) {
            is Resource.Success -> favListener?.deleteFromFav()
            is Resource.Failure -> favListener?.addToFavFailed(response.errorCode)
        }
    }
}
