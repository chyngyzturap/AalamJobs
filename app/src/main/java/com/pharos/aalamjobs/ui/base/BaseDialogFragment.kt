package com.pharos.aalamjobs.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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


abstract class BaseDialogFragment<VM : BaseViewModel, B : ViewBinding, R : BaseRepository> : DialogFragment(), CoroutineScope {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())
        lifecycleScope.launch { userPreferences.tokenAccess.first() }
        return binding.root
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R
}