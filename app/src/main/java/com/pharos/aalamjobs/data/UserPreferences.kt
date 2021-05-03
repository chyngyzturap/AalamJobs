package com.pharos.aalamjobs.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.Key

class UserPreferences (
    context: Context
        ){
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "my_data_store"
        )
    }

    val tokenAccess: Flow<String?>
    get() = dataStore.data.map { preferences ->
        preferences[KEY_AUTH]
    }

    suspend fun saveAuthToken(tokenAccess: String/*, tokenRefresh: String*/){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = tokenAccess
            /*preferences[KEY_AUTH] = tokenRefresh*/

        }
    }

    suspend fun clear(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val KEY_AUTH = preferencesKey<String>("key_auth")
    }
}