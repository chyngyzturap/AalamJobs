package com.pharos.aalamjobs.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.local.prefs.UserPreferences2
import com.pharos.aalamjobs.data.local.user.UserDataLocal
import com.pharos.aalamjobs.data.model.ChangePasswordModel
import com.pharos.aalamjobs.data.model.CreateUserModel
import com.pharos.aalamjobs.data.model.ForgotPasswordModel
import com.pharos.aalamjobs.data.model.TokenObtainPair
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.responses.LoginResponse
import com.pharos.aalamjobs.ui.auth.password.ResetPasswordListener
import com.pharos.aalamjobs.ui.auth.utils.LoginListener
import com.pharos.aalamjobs.ui.auth.utils.UserListener
import com.pharos.aalamjobs.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AuthViewModel(
    private val repository: AuthRepository,
//    private val userDataLocal: UserDataLocal,
//    private val userPreferences: UserPreferences2

) : BaseViewModel(repository){
    private var userDataLocal: UserDataLocal? = null


    private var userPreferences: UserPreferences2? = null

//    private val _userDataLocal: MutableLiveData<Resource<UserDataLocal>> = MutableLiveData()
//    val userDataLocal: LiveData<Resource<UserDataLocal>>
//    get() = _userDataLocal


    private val _userPreferences2: MutableLiveData<Resource<UserPreferences2>> = MutableLiveData()
    val userPreferences2: LiveData<Resource<UserPreferences2>>
        get() = _userPreferences2

    private var loginListener: LoginListener? = null
    private var userListener: UserListener? = null
    private var resetPwdListener: ResetPasswordListener? = null


    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
    get() = _loginResponse

    private val _user: MutableLiveData<Resource<TokenObtainPair>> = MutableLiveData()
    val user: LiveData<Resource<TokenObtainPair>>
    get() = _user


    fun login(tokenObtainPair: TokenObtainPair
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(tokenObtainPair)
    }

    fun login2(tokenObtainPair: TokenObtainPair, code: Int
    ) = viewModelScope.launch {
        when (val response = repository.login(tokenObtainPair)) {
            is Resource.Success -> {
                if (code == 1) {
                    loginListener?.loginSuccess(response.value)
                    saveAuthToken(response.value.access)
                }

                else {
                    saveUserData(response.value)
                    Log.d("ololo", response.value.toString())
                }
            }
            is Resource.Failure -> {
                if (code == 1 && response.errorCode != 1)
                    loginListener?.loginFail(response.errorCode)
                else
                    loginListener?.signInFail(response.errorBody, response.errorCode)
            }
        }
    }

//    fun saveUserData(loginResponse: LoginResponse) =
//        viewModelScope.launch {
//
//            userDataLocal.saveUserData(
//                loginResponse.id,loginResponse.username, loginResponse.email
//            )
//            updateLoggedIn()
//        }
//
//    private fun updateLoggedIn() =
//        viewModelScope.launch {
//            userPreferences2.saveLoggedIn("DONE")
//            loginListener?.userDataSavedLogin()
//        }

    fun createNewUser(createUserModel: CreateUserModel
    ) = viewModelScope.launch {
        when (val response = repository.createUser(createUserModel)) {
            is Resource.Success -> {
                val modelLogin =
                    CreateUserModel(createUserModel.username, createUserModel.password,
                    createUserModel.role, createUserModel.fullname)

                val modelUser =
                    TokenObtainPair( modelLogin.username,
                            modelLogin.password)

//                login2(modelUser, 2)
                login(modelUser)
            }
            is Resource.Failure -> {
                loginListener?.signInFail(response.errorBody, response.errorCode)
            }
        }
    }

    fun checkPhone(phone: String
    ) = viewModelScope.launch {
        when (val response = repository.checkPhone(phone)) {
            is Resource.Success -> {
                    loginListener?.isUserExists(true)
            }
            is Resource.Failure -> {
                loginListener?.signInFail(response.errorBody, response.errorCode)
            }
        }
    }

    fun changePassword(changePasswordModel: ChangePasswordModel) = viewModelScope.launch {
     repository.changePassword(changePasswordModel)
    }

    fun forgotPassword(forgotPasswordModel: ForgotPasswordModel) = viewModelScope.launch {
     repository.forgotPassword(forgotPasswordModel)
    }


    fun updateProfile(photo: MultipartBody.Part?, email: RequestBody, fullname: RequestBody,
                      position: RequestBody, city: RequestBody, country: RequestBody)
    = viewModelScope.launch {
     repository.updateProfile(photo, email, fullname, position, city, country)
    }

    fun getProfileInfo() = viewModelScope.launch {
        when (val response = repository.getProfileInfo()) {
            is Resource.Success -> {
                userListener?.setUserData(response.value)
            }
            is Resource.Failure -> {
                userListener?.dataError(response.errorCode)
            }
        }
    }

    private suspend fun saveUserData(modelLoginResponse: LoginResponse) =
        viewModelScope.launch {

//            val modelUser = modelLoginResponse.data[0]
            userDataLocal?.saveUserData(
                modelLoginResponse.username,
                modelLoginResponse.role,
                modelLoginResponse.access
            )
            updateLoggedIn()
        }

    private fun updateLoggedIn() =
        viewModelScope.launch {
            userPreferences?.saveLoggedIn("DONE")
            loginListener?.userDataSavedLogin()
        }

    suspend fun saveAuthToken(tokenAccess: String){

        viewModelScope.launch {
        repository.saveAuthToken(tokenAccess)

        }
    }

    suspend fun saveLang(lang: String){

        viewModelScope.launch {
        repository.saveLang(lang)

        }
    }

    suspend fun saveUser(email: String, id: Int, username: String, photo: String,
                         city: String, country: String, position: String, fullname: String){
        viewModelScope.launch {
            repository.saveUser(email, id, username, photo, city, country, position, fullname)
        }
    }

    fun setResetPasswordListener(listener: ResetPasswordListener) {
        this.resetPwdListener = listener
    }

    fun setLoginListener(listener: LoginListener) {
        this.loginListener = listener
    }
    fun setUserListener(listener: UserListener) {
        this.userListener = listener
    }

    fun logout() = viewModelScope.launch {
        repository.logout()
    }

}