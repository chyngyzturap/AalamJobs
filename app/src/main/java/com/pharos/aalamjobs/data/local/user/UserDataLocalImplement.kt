package com.pharos.aalamjobs.data.local.user

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserDataLocalImplement(
    context: Context
) : UserDataLocal {

    private val applicationContext = context.applicationContext

    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "user_data"
        )
    }


    override suspend fun clearUserData() {
        dataStore.edit {
            it[USER_ROLE_KEY] = ""
            it[USER_NAME_KEY] = ""
//            it[USER_EMAIL_KEY] = ""
            it[USER_TOKEN_KEY] = ""
        }
    }

    override suspend fun saveUserData(
        username: String,
//        email: String,
        role: String,
        token: String
    ) {
        dataStore.edit {
            if (!token.isNullOrEmpty())
                it[USER_ROLE_KEY] = role
            it[USER_NAME_KEY] = username
//            it[USER_EMAIL_KEY] = email
            it[USER_TOKEN_KEY] = token
        }
    }

    //    override val email: Flow<String?>
//        get() = dataStore.data.map {
//            it[USER_EMAIL_KEY]
//        }
    override val role: Flow<String?>
        get() = dataStore.data.map {
            it[USER_ROLE_KEY]
        }


    override val token: Flow<String?>
        get() = dataStore.data.map {
            it[USER_TOKEN_KEY]
        }

    override val username: Flow<String?>
        get() = dataStore.data.map {
            it[USER_NAME_KEY]
        }


    companion object {
        private val USER_TOKEN_KEY = preferencesKey<String>("user_token")
        private val USER_ROLE_KEY = preferencesKey<String>("user_role")
        private val USER_ID_KEY = preferencesKey<Int>("user_id")
        private val USER_NAME_KEY = preferencesKey<String>("user_name")
        private val USER_EMAIL_KEY = preferencesKey<String>("user_email")
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}