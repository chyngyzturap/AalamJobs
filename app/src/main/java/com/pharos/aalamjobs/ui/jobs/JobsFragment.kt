package com.pharos.aalamjobs.ui.jobs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.FavJobsResponse
import com.pharos.aalamjobs.data.responses.JobsResponse
import com.pharos.aalamjobs.databinding.FragmentJobsBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.jobs.adapter.JobsAdapter
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.FavoriteListener
import com.pharos.aalamjobs.ui.jobs.utils.JobsListener
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class JobsFragment : BaseFragment<JobsViewModel, FragmentJobsBinding, JobsRepository>(), JobsListener,
JobsAdapter.JobClickListener, FavoriteListener{

    private var jobsAdapter: JobsAdapter? = null
    private val jobs = mutableListOf<Jobs>()
    private var page: Int = 1

    private var search: String = ""
    private var country: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.progressbar.visible(true)
        viewModel.setJobsListener(this)
        binding.rvJobs.setHasFixedSize(true)

        val countryIntent = requireActivity().intent.getIntExtra("countryId", 0)
        val countryArray = "[$countryIntent]"

        val cityIntent = requireActivity().intent.getIntExtra("cityId", 0)
        val cityArray = "[$cityIntent]"

        val sectorIntent = requireActivity().intent.getIntExtra("sectorId", 0)
        val sectorArray = "[$sectorIntent]"




        if (countryIntent != 0){
            viewModel.getJobsFilteredList(page, countryArray, cityArray, sectorArray)
        } else {
            viewModel.getJobsList(page, search)

        }

        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                binding.progressbar.visible(true)
                page++
                viewModel.getJobsList(page, search)
                binding.progressbar.visible(false)

            }
        }


        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            jobs.clear()
            viewModel.getJobsList(page, search)
        }


        binding.ivSearchDetail.setOnClickListener {

            val token = runBlocking { userPreferences.tokenAccess.first() }

            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                val intent = Intent(requireContext(), JobSearchDetailActivity::class.java)
                startActivity(intent)
            }
        }




        binding.etSearch.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getJobsList()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    private fun getJobsList (){
        viewModel.getJobsList(page, binding.etSearch.text.toString())
        jobsAdapter = JobsAdapter(this)
        binding.rvJobs.setHasFixedSize(true)
        binding.rvJobs.adapter = jobsAdapter
        binding.swipeRefresh.isRefreshing = false
        jobsAdapter?.submitList(jobs)
    }


    override fun getViewModel()= JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentJobsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)

//            return JobsRepository(api)

        if (token.isNullOrEmpty()){
            return JobsRepository(apiNoToken)
        } else {
            return JobsRepository(api)
        }

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

    override fun onJobClick(jobId: Int) {
//        val bundle = Bundle()
//        bundle.putInt("jobId", jobId)
//        findNavController().navigate(R.id.action_nav_jobs_to_jobsDetailFragment, bundle)
        val intent = Intent(requireContext(), JobsDetailActivity::class.java)
        intent.putExtra("jobId", jobId)
        startActivity(intent)
    }

    override fun addToFavorites(position: Int) {
//        val current = jobsAdapter!!.getItemAtPos(position)
        val token = runBlocking { userPreferences.tokenAccess.first() }


        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            val jobIdFav = JobId(position)
            viewModel.setFavorite(jobIdFav)
        }
    }

    override fun deleteFromFavorites(position: Int) {
        val jobIdFav = JobId(position)


        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            viewModel.deleteFromFav(position)
        }
    }


    override fun setJob(jobsResponse: JobsResponse) {
        binding.swipeRefresh.isRefreshing = false
        binding.progressbar.visible(false)
        if (page == 1)
            jobs.clear()
        jobs.addAll(jobsResponse.results)
        jobsAdapter = JobsAdapter(this)
        binding.rvJobs.adapter = jobsAdapter
        jobsAdapter?.submitList(jobs)
    }

    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun postFavJobSuccess() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addToFavFailed(code: Int?) {
        Toast.makeText(requireContext(), "error + $code", Toast.LENGTH_SHORT).show()
    }

    override fun deleteFromFav() {
        TODO("Not yet implemented")
    }

    override fun setFavoriteJob(jobs: FavJobsResponse) {
        TODO("Not yet implemented")
    }

    override fun getFavJobError(code: Int?) {
        TODO("Not yet implemented")
    }

    override fun mustLogin() {
        Snackbar.make(binding.root, "For this function you need to log in or register", Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.txt_register_login)) {
                goToRegister()
            }
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setActionTextColor(Color.parseColor("#E4FB01"))
            .setDuration(5000)
            .show()
        jobsAdapter?.notifyDataSetChanged()
    }

    private fun goToRegister() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(intent)
    }
    private fun authAlertDialog(){
        val mAlertDialog = AlertDialog.Builder(requireContext())
//            mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon

        mAlertDialog.setPositiveButtonIcon(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_done_24_green
            )
        )
        mAlertDialog.setNegativeButtonIcon(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_cancel_24_red
            )
        )
        mAlertDialog.setTitle("Please sign up to get access") //set alertdialog title
//        mAlertDialog.setMessage(R.string.txt_logout_before_remind) //set alertdialog message
        mAlertDialog.setPositiveButton("Sign Up ") { dialog, id ->
            ActivityCompat.finishAffinity(requireActivity())
            val intent = Intent(requireContext(), SplashActivity::class.java)
            startActivity(intent)
        }
        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
            null
        }
        mAlertDialog.show()
    }

}