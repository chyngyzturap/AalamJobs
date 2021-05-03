package com.pharos.aalamjobs.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.TokenObtainPair
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel(repository){

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
    get() = _loginResponse


    fun login(tokenObtainPair: TokenObtainPair
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(tokenObtainPair)
    }

    suspend fun saveAuthToken(tokenAccess: String/*, tokenRefresh: String*/){
        repository.saveAuthToken(tokenAccess/*, tokenRefresh*/)
    }
}