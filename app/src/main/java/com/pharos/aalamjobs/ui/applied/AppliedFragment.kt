package com.pharos.aalamjobs.ui.applied

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.FavJobsResponse
import com.pharos.aalamjobs.databinding.FragmentMainBinding
import com.pharos.aalamjobs.ui.applied.model.AppliedJobsResponse
import com.pharos.aalamjobs.ui.applied.model.AppliedJobsResponseItem
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvListener
import com.pharos.aalamjobs.ui.jobs.JobsDetailActivity
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.FavoriteListener
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AppliedFragment : BaseFragment<JobsViewModel, FragmentMainBinding, JobsRepository>(),
    AppliedListener,
    AppliedAdapter.JobClickListener, FavoriteListener, CvListener {

    private var appliedAdapter: AppliedAdapter? = null
    private val appliedList = mutableListOf<AppliedJobsResponseItem>()

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(true)
        viewModel.setAppliedListener(this)
        viewModel.getAppliedList()
        binding.rvJobs.setHasFixedSize(true)
        cleanSearchIntents()

        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                binding.progressbar.visible(true)
                viewModel.getAppliedList()
                binding.progressbar.visible(false)

            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            appliedList.clear()
            viewModel.getAppliedList()
        }
    }

    private fun cleanSearchIntents() {
        val countryIntent = requireActivity().intent.getIntExtra("countryId", 0)
        val cityIntent = requireActivity().intent.getIntExtra("cityId", 0)
        val sectorIntent = requireActivity().intent.getIntExtra("sectorId", 0)
        if (countryIntent != 0 || cityIntent != 0 || sectorIntent != 0) {
            requireActivity().intent.removeExtra("countryId")
            requireActivity().intent.removeExtra("cityId")
            requireActivity().intent.removeExtra("sectorId")
        }
    }

    override fun getViewModel() = JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)
        if (token.isNullOrEmpty()) {
            return JobsRepository(apiNoToken)
        } else {
            return JobsRepository(api)
        }
    }

    override fun onJobClick(jobId: Int) {
        val intent = Intent(requireContext(), JobsDetailActivity::class.java)
        intent.putExtra("jobId", jobId)
        startActivity(intent)
    }

    override fun addToFavorites(position: Int) {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()) {
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            val jobIdFav = JobId(position)
            viewModel.setFavorite(jobIdFav)
        }
    }

    override fun deleteFromFavorites(position: Int) {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            viewModel.deleteFromFav(position)
        }
    }


    override fun setApplied(applied: AppliedJobsResponse) {
        if (applied.isNotEmpty()) {
            binding.tvResumes.visible(false)
            binding.loggedContainer.visible(true)
            binding.swipeRefresh.isRefreshing = false
            binding.progressbar.visible(false)
            appliedList.clear()
            appliedList.addAll(applied)
            appliedAdapter = AppliedAdapter(this)
            binding.rvJobs.adapter = appliedAdapter
            appliedAdapter?.submitList(appliedList)
        }
    }

    override fun getAppliedError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false

    }

    override fun postFavJobSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addToFavFailed(code: Int?) {
        Toast.makeText(requireContext(), "error + $code", Toast.LENGTH_SHORT).show()
    }

    override fun deleteFromFav() {
    }

    override fun setFavoriteJob(jobs: FavJobsResponse) {
    }

    override fun getFavJobError(code: Int?) {
    }

    override fun createCvSuccess(id: Int) {
    }

    override fun createCvFailed(code: Int?) {
    }

    override fun applyCvSuccess() {
    }

    override fun applyCvFailed(code: Int?) {
    }

    override fun setResume(cv: MutableList<CvModelResponse>) {
    }

    override fun getCvError(code: Int?) {
    }


}