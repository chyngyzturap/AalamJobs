package com.pharos.aalamjobs.ui.jobs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.Jobs
import com.pharos.aalamjobs.data.responses.JobsResponse
import com.pharos.aalamjobs.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class JobsViewModel (
    private val repository: JobsRepository
    ) : BaseViewModel(repository) {

        private val _jobs: MutableLiveData<Resource<JobsResponse>> = MutableLiveData()
    val jobs: MutableLiveData<Resource<JobsResponse>> get() = _jobs

        fun getJobs() = viewModelScope.launch {
            _jobs.value = Resource.Loading
            _jobs.value = repository.getJobs()
        }
}