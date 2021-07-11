package com.pharos.aalamjobs.ui.splash

import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.local.prefs.UserPreferences2
import com.pharos.aalamjobs.data.local.user.UserDataLocal
import com.pharos.aalamjobs.data.repository.ItemRepository
import com.pharos.aalamjobs.ui.base.BaseViewModel
import com.pharos.aalamjobs.utils.MainListener

class UserPrefsViewModel(
    private val userPreferences: UserPreferences2,
    private val repository: ItemRepository,
    private val userDataLocal: UserDataLocal

) : BaseViewModel(repository) {

    private var listener: MainListener? = null




//    val isUserFirstTime = userPreferences.isFirstTime
//    val appLanguage = userPreferences.appLanguage
//
//    fun updateLanguage(lang: String) = viewModelScope.launch {
//        userPreferences.updateAppLanguage(lang)
//    }
//
//    fun saveIsFirstTime(text: String) {
//        GlobalScope.launch(Dispatchers.Main) {
//            userPreferences.saveIsFirstTime(text)
//        }
//    }
//
//    fun setListener(listener: MainListener) {
//        this.listener = listener
//    }
//
//    fun updateAppLanguage(lang: String) {
//        GlobalScope.launch(Dispatchers.Main) {
//            userPreferences.updateAppLanguage(lang)
//        }
//    }
//
//    fun getUserData() = viewModelScope.launch {
//        userDataLocal.username.collectLatest { username ->
//            userDataLocal.role.collectLatest { role ->
//                listener?.getUserData(username, role)
//            }
//        }
//    }
//
//    fun getUserType() = viewModelScope.launch {
//
//        userPreferences.appLanguage.collectLatest { lang ->
//            listener?.setUserType(lang)
//        }
//    }
}



