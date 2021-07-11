package com.pharos.aalamjobs.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.CityResponseItem
import com.pharos.aalamjobs.data.responses.CurrencyResponse
import com.pharos.aalamjobs.data.responses.CurrencyResponseItem
import com.pharos.aalamjobs.databinding.LayoutCountryBinding
import com.pharos.aalamjobs.ui.base.BaseFragment2
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CurrencyDialogFragment(private val listener: SearchListener):BaseFragment2<JobsViewModel, LayoutCountryBinding, JobsRepository>(),
    CurrencyListener, CurrencyAdapter.CurrencyClickListener{

    private var cityList = mutableListOf<CityResponseItem>()
    private var currencyList = mutableListOf<CurrencyResponseItem>()
    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.setCurrencyListener(this)
        viewModel.getCurrencyList()

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

    override fun setCurrency(currency: CurrencyResponse) {
        currencyList.addAll(currency)
        currencyAdapter = CurrencyAdapter(this)
        binding.rvCountry.adapter = currencyAdapter
        currencyAdapter.submitList(currencyList)
    }

    override fun getCurrencyError(code: Int?) {
        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()

    }

    override fun onCurrencyCLick(position: Int) {
        listener.getCurrencySign(currencyList[position].id, currencyList[position].sign)
        dismiss()
    }
}