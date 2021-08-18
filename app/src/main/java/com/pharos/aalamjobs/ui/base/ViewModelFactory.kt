package com.pharos.aalamjobs.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.repository.BaseRepository
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.ui.jobs.JobsViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(JobsViewModel::class.java) -> JobsViewModel(repository as JobsRepository) as T
            modelClass.isAssignableFrom(CvViewModel::class.java) -> CvViewModel(repository as CvRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}