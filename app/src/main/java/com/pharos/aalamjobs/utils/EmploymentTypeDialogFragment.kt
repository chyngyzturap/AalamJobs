package com.pharos.aalamjobs.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.CityResponseItem
import com.pharos.aalamjobs.data.responses.EmploymentTypeResponse
import com.pharos.aalamjobs.data.responses.EmploymentTypeResponseItem
import com.pharos.aalamjobs.databinding.LayoutCountryBinding
import com.pharos.aalamjobs.ui.base.BaseFragment2
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.utils.EmplTypeListener
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EmploymentTypeDialogFragment(private val listener: SearchListener):BaseFragment2<JobsViewModel, LayoutCountryBinding, JobsRepository>(),
    EmplTypeListener, EmploymentTypeAdapter.EmplTypeClickListener{

    private var cityList = mutableListOf<CityResponseItem>()
    private var emplTypeList = mutableListOf<EmploymentTypeResponseItem>()
    private lateinit var emplTypeAdapter: EmploymentTypeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.setEmplTypesListener(this)
        viewModel.getEmploymentTypes()

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

    override fun setEmplTypes(emplTypes: EmploymentTypeResponse) {
        emplTypeList.addAll(emplTypes)
        emplTypeAdapter = EmploymentTypeAdapter(this)
        binding.rvCountry.adapter = emplTypeAdapter
        emplTypeAdapter.submitList(emplTypeList)
    }

    override fun getEmplTypesError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onEmplTypeClick(position: Int) {
        listener.getEmplTypeId(emplTypeList[position].id, emplTypeList[position].name.en)
        dismiss()
    }
}