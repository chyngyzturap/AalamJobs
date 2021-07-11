package com.pharos.aalamjobs.data.local.prefs

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesImplementation(
    context: Context
) : UserPreferences2 {
    private val applicationContext = context.applicationContext

    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "my_data_store_2"
    )

    override suspend fun saveIsFirstTime(text: String) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_KEY] = text
        }
    }

    override val isFirstTime: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[IS_FIRST_TIME_KEY]
        }

    override suspend fun updateAppLanguage(lang: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE_KEY] = lang
        }
    }

    override val appLanguage: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[APP_LANGUAGE_KEY]
        }

    override suspend fun saveLoggedIn(loggedIn: String) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = loggedIn
        }
    }

    override val isLoggedIn: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN_KEY]
        }


    companion object {
        private val IS_FIRST_TIME_KEY = preferencesKey<String>("is_first_time")
        private val APP_LANGUAGE_KEY = preferencesKey<String>("app_language")
        private var IS_LOGGED_IN_KEY = preferencesKey<String>("is_logged_in")
    }
}