package com.pharos.aalamjobs.ui.cv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.local.db.cv.models.ApplicationModel
import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class CvViewModel(
    private val repository: CvRepository
) : BaseViewModel(repository) {

    private var listener: CvListener? = null
    private val preferences: UserPreferences? = null

    private val _cvModel: MutableLiveData<Resource<CvModel>> = MutableLiveData()
    val cvModel: LiveData<Resource<CvModel>>
        get() = _cvModel

    private val _cvModelResponse: MutableLiveData<Resource<CvModelResponse>> = MutableLiveData()
    val cvModelResponse: LiveData<Resource<CvModelResponse>>
        get() = _cvModelResponse

    fun createNewCV(cvModel: CvModel) = viewModelScope.launch {
        when (val response = repository.createCV(cvModel)) {
            is Resource.Success -> {
                listener?.createCvSuccess(response.value.id)
            }
            is Resource.Failure -> {
                listener?.createCvFailed(response.errorCode)
            }
        }
        }

    fun applyCv(applicationModel: ApplicationModel) = viewModelScope.launch {
        when (val response = repository.applyCv(applicationModel)) {
            is Resource.Success -> {
                listener?.applyCvSuccess()
            }
            is Resource.Failure -> {
                listener?.applyCvFailed(response.errorCode)
            }
        }
    }

    fun setCvListener(listener: CvListener) {
        this.listener = listener
    }

    fun getResumesList(page: Int) = viewModelScope.launch {
        when (val response = repository.getResumes(page)) {
            is Resource.Success -> {
                listener?.setResume(response.value)
            }
            is Resource.Failure -> {
                listener?.getCvError(response.errorCode)
            }
        }
    }
}