package com.pharos.aalamjobs.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.model.FavJobs
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.FavJobsResponse
import com.pharos.aalamjobs.databinding.FragmentFavoritesBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.favorites.adapter.FavoriteAdapter
import com.pharos.aalamjobs.ui.jobs.JobsDetailActivity
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.FavoriteListener
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class FavoritesFragment : BaseFragment<JobsViewModel, FragmentFavoritesBinding, JobsRepository>(),
    FavoriteAdapter.JobClickListener, FavoriteListener {

    private var favAdapter: FavoriteAdapter? = null
    private val jobsFavList = mutableListOf<FavJobs>()
    private var page: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(true)
        viewModel.setFavJobsListener(this)
        viewModel.getFavJobsList(page)
        binding.rvJobs.setHasFixedSize(true)
        cleanSearchIntents()
    }

    private fun cleanSearchIntents(){
        val countryIntent = requireActivity().intent.getIntExtra("countryId", 0)
        val cityIntent = requireActivity().intent.getIntExtra("cityId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("sectorId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("countryId")
            requireActivity().intent.removeExtra("cityId")
            requireActivity().intent.removeExtra("sectorId")
        }
    }

    override fun getViewModel()= JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)

        if (token.isNullOrEmpty()){
            return JobsRepository(apiNoToken)
        } else {
            return JobsRepository(api)
        }
    }

    override fun setFavoriteJob(jobs: FavJobsResponse) {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            if (jobs.count != 0){
                binding.tvFavorites.visible(false)
                binding.loggedContainer.visible(true)
                binding.progressbar.visible(false)
                if (page == 1)
                    jobsFavList.clear()
                jobsFavList.addAll(jobs.results)
                favAdapter = FavoriteAdapter(this)
                binding.rvJobs.adapter = favAdapter
                favAdapter?.submitList(jobsFavList)
                favAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun getFavJobError(code: Int?) {
    }

    override fun onJobClick(jobId: Int) {
        val intent = Intent(requireContext(), JobsDetailActivity::class.java)
        intent.putExtra("jobId", jobId)
        startActivity(intent)
    }

    override fun addToFavorites(position: Int) {
        val jobIdFav = JobId(position)
        viewModel.setFavorite(jobIdFav)
    }

    override fun deleteFromFavorites(position: Int) {
        viewModel.deleteFromFav(position)
    }

    override fun postFavJobSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addToFavFailed(code: Int?) {
        Toast.makeText(requireContext(), "error + $code", Toast.LENGTH_SHORT).show()
    }

    override fun deleteFromFav() {
    }
}