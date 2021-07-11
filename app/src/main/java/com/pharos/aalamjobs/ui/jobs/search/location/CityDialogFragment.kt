package com.pharos.aalamjobs.ui.jobs.search.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.CityResponse
import com.pharos.aalamjobs.data.responses.CityResponseItem
import com.pharos.aalamjobs.data.responses.CountryResponseItem
import com.pharos.aalamjobs.databinding.LayoutCityBinding
import com.pharos.aalamjobs.ui.base.BaseFragment2
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.utils.CityListener
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CityDialogFragment(private val listener: SearchListener):BaseFragment2<JobsViewModel, LayoutCityBinding, JobsRepository>(),
    CityListener,
    CityAdapter.CityClickListener {

    private var countryList = mutableListOf<CountryResponseItem>()
    private var cityList = mutableListOf<CityResponseItem>()
    private lateinit var cityAdapter: CityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setCityListener(this)
        viewModel.getCitiesList()

    }


    override fun getViewModel()= JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= LayoutCityBinding.inflate(inflater, container, false)

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

    override fun setCities(jobs: CityResponse) {

        cityList.addAll(jobs)
        cityAdapter = CityAdapter(this)
        binding.rvCountry.adapter = cityAdapter
        cityAdapter.submitList(cityList)
    }

    override fun getCityError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onCityClick(position: Int) {
        listener.getCityId(cityList[position].id,cityList[position].name.en)
        dismiss()
    }
}