package com.pharos.aalamjobs.ui.jobs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.databinding.FragmentJobsBinding
import com.pharos.aalamjobs.ui.adapter.JobsAdapter
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class JobsFragment : BaseFragment<JobsViewModel, FragmentJobsBinding, JobsRepository>() {

    private var jobsAdapter: JobsAdapter = JobsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.progressbar.visible(true)
        updateUI()
        viewModel.getJobs()

        viewModel.jobs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visible(false)
                    Log.e("ololo", "onViewCreated success: ${it.value.results}")
                    val list = it.value.results.toMutableList()
                    jobsAdapter.addData(data = list)
                }
                is Resource.Loading -> {
                    binding.progressbar.visible(true)

                }
                is Resource.Failure -> {
                    Log.e("ololo", "onViewCreated error: ${it.errorBody}")
                }
            }
        })
    }

    override fun getViewModel()= JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentJobsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        return JobsRepository(api)
    }

    private fun setupRecyclerView() = binding.rvJobs.apply {
binding.rvJobs.adapter = jobsAdapter
layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateUI() {
        with(binding) {
            setupRecyclerView()
        }
    }


}