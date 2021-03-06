package com.pharos.aalamjobs.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.RemoteDataSource
import com.pharos.aalamjobs.data.repository.BaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity <VM: ViewModel, B : ViewBinding, R : BaseRepository> : AppCompatActivity(),
CoroutineScope{

    private lateinit var job: Job
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    protected val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        userPreferences = UserPreferences(this)
        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        lifecycleScope.launch { userPreferences.tokenAccess.first() }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getActivityBinding(inflater: LayoutInflater): B

    abstract fun getActivityRepository(): R
}