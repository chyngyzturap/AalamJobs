package com.pharos.aalamjobs.ui.jobs.search.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.CityResponseItem
import com.pharos.aalamjobs.data.responses.CountryResponse
import com.pharos.aalamjobs.data.responses.CountryResponseItem
import com.pharos.aalamjobs.databinding.LayoutCountryBinding
import com.pharos.aalamjobs.ui.base.BaseFragment2
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.utils.CountryListener
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CountryDialogFragment(private val listener: SearchListener):BaseFragment2<JobsViewModel, LayoutCountryBinding, JobsRepository>(),
    CountryListener,
    CountryAdapter.CountryClickListener {

    private var cityList = mutableListOf<CityResponseItem>()
    private var countryList = mutableListOf<CountryResponseItem>()
    private lateinit var countryAdapter: CountryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.setCountryListener(this)
        viewModel.getCountryList()

    }

    override fun setCountry(jobs: CountryResponse) {

        countryList.addAll(jobs)
        countryAdapter = CountryAdapter(this)
        binding.rvCountry.adapter = countryAdapter
        countryAdapter.submitList(countryList)
    }

    override fun getCountryError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
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

    override fun onCountryClick(position: Int) {
        listener.getCountryId(countryList[position].id, countryList[position].name.en)
        dismiss()
    }
}