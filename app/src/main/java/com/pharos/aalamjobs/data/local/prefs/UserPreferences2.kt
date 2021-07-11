package com.pharos.aalamjobs.data.local.prefs

import kotlinx.coroutines.flow.Flow

interface UserPreferences2 {

    suspend fun saveIsFirstTime(text: String)
    val isFirstTime: Flow<String?>

    suspend fun updateAppLanguage(lang: String)
    val appLanguage: Flow<String?>

    suspend fun saveLoggedIn(loggedIn: String)
    val isLoggedIn: Flow<String?>


}