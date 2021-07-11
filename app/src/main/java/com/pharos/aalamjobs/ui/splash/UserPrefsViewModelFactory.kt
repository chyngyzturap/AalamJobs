package com.pharos.aalamjobs.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.local.prefs.UserPreferences2
import com.pharos.aalamjobs.data.local.user.UserDataLocal
import com.pharos.aalamjobs.data.repository.ItemRepository

class UserPrefsViewModelFactory(
    private val userPreferences: UserPreferences2,
    private val userDataLocal: UserDataLocal,
    private val itemRepository: ItemRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserPrefsViewModel(userPreferences, itemRepository, userDataLocal) as T
    }
}