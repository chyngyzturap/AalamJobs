package com.pharos.aalamjobs.ui.jobs.search.sector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.dialog.SectorResponse
import com.pharos.aalamjobs.data.responses.dialog.SectorResponseItem
import com.pharos.aalamjobs.databinding.LayoutCountryBinding
import com.pharos.aalamjobs.ui.base.BaseDialogFragment
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import com.pharos.aalamjobs.ui.jobs.utils.SectorListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SectorDialogDialogFragment(private val listener: SearchListener):BaseDialogFragment<JobsViewModel, LayoutCountryBinding, JobsRepository>(),
    SectorListener, SectorAdapter.SectorClickListener {

    private var sectorList = mutableListOf<SectorResponseItem>()
    private lateinit var sectorAdapter: SectorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSectorListener(this)
        viewModel.getSectorsList()
    }

    override fun getViewModel()= JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= LayoutCountryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)

        if (token.isNullOrEmpty()){
            return JobsRepository(apiNoToken )
        } else {
            return JobsRepository(api)
        }
    }

    override fun setSectors(jobs: SectorResponse) {
        sectorList.addAll(jobs)
        sectorAdapter = SectorAdapter(this)
        binding.rvCountry.adapter = sectorAdapter
        sectorAdapter.submitList(sectorList)
    }

    override fun getSectorError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onSectorClick(position: Int) {
        listener.getSectorId(sectorList[position].id, sectorList[position].name)
        dismiss()
    }
}