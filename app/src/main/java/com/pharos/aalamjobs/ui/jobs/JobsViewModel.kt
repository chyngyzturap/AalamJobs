package com.pharos.aalamjobs.ui.jobs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.local.user.UserDataLocal
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.JobsResponse
import com.pharos.aalamjobs.ui.base.BaseViewModel
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.*
import com.pharos.aalamjobs.utils.CurrencyListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class JobsViewModel(
    private val repository: JobsRepository
) : BaseViewModel(repository) {

    private var currencyListener: CurrencyListener? = null
    private var jobsListener: JobsListener? = null
    private var favListener: FavoriteListener? = null
    private var countryListener: CountryListener? = null
    private var cityListener: CityListener? = null
    private var sectorListener: SectorListener? = null
    private var emplTypesListener: EmplTypeListener? = null
    private var jobListener: JobListener? = null
    private val userDataLocal: UserDataLocal? = null
    private val userPreferences: UserPreferences? = null


    private val _jobs: MutableLiveData<Resource<JobsResponse>> = MutableLiveData()
    val jobs: MutableLiveData<Resource<JobsResponse>> get() = _jobs

//        fun getJobs() = viewModelScope.launch {
//            _jobs.value = Resource.Loading
//            _jobs.value = repository.getJobs()
//        }

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

    fun getJobsFilteredList(page: Int, country: String, city: String, sector: String
    ) = viewModelScope.launch {
        when (val response = repository.getJobsFiltered(page, country, city, sector)) {
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
        when (val response = repository.getCountries()){
            is Resource.Success -> {
                countryListener?.setCountry(response.value)
            }
            is Resource.Failure -> {
                countryListener?.getCountryError(response.errorCode)
            }
        }
    }

    fun getCitiesList() = viewModelScope.launch {
        when (val response = repository.getCities()){
            is Resource.Success -> {
                cityListener?.setCities(response.value)
            }
            is Resource.Failure -> {
                cityListener?.getCityError(response.errorCode)
            }
        }
    }

    fun getSectorsList() = viewModelScope.launch {
        when (val response = repository.getSectors()){
            is Resource.Success -> {
                sectorListener?.setSectors(response.value)
            }
            is Resource.Failure -> {
                sectorListener?.getSectorError(response.errorCode)
            }
        }
    }


    fun getCurrencyList() = viewModelScope.launch {
        when (val response = repository.getCurrencies()){
            is Resource.Success -> {
                currencyListener?.setCurrency(response.value)
            }
            is Resource.Failure -> {
                currencyListener?.getCurrencyError(response.errorCode)
            }
        }
    }

    fun getEmploymentTypes() = viewModelScope.launch {
        when (val response = repository.getEmploymentTypes()){
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
//            userPreferences.token.collectLatest { token ->

        when (val response = repository.setFavorite(jobId)) {
            is Resource.Success -> {
                favListener?.postFavJobSuccess()
            }
            is Resource.Failure -> {
                favListener?.addToFavFailed(response.errorCode)
            }
        }

//                    favListener?.mustLogin()
    }

    fun setFavoriteFromDetail(jobId: JobId) = viewModelScope.launch {

        val token = userPreferences?.tokenAccess?.first()


        when (val response = repository.setFavoriteFromDetail(jobId, token!!)) {
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

    fun deleteFromFavDetail(jobId: Int) = viewModelScope.launch {
        val token = userPreferences?.tokenAccess?.first()
            when (val response = repository.deleteFavoriteFromDetail(jobId, token!!)) {
                is Resource.Success -> favListener?.deleteFromFav()
                is Resource.Failure -> favListener?.addToFavFailed(response.errorCode)
            }
        }
    }




//    fun getJobData(jobId: Int) = viewModelScope.launch {
//        when (val response = repository.getStoreById(jobId)) {
//            is Resource.Success -> {
//                listener?.setJob(response.value)
//            }
//            is Resource.Failure -> {
//                listener?.getJobError(response.errorCode)
//            }
//        }
//    }
