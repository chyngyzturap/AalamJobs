package com.pharos.aalamjobs.ui.favorites

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.FavJobs
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.FavJobsResponse
import com.pharos.aalamjobs.databinding.FragmentFavoritesBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.favorites.adapter.FavoriteAdapter
import com.pharos.aalamjobs.ui.jobs.JobsDetailActivity
import com.pharos.aalamjobs.ui.jobs.JobsViewModel
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.FavoriteListener
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.SignUpDialogFragment
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


    }


    private fun setupRecyclerView() = binding.rvJobs.apply {
        binding.rvJobs.adapter = favAdapter
        layoutManager = LinearLayoutManager(requireContext())
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
            binding.tvFavorites.visible(false)
            binding.loggedContainer.visible(true)
            binding.progressbar.visible(false)
            if (page == 1)
                jobsFavList.clear()
            jobsFavList.addAll(jobs.results)
            favAdapter = FavoriteAdapter(this)
            binding.rvJobs.adapter = favAdapter
            favAdapter?.submitList(jobsFavList)
        }


    }

    override fun getFavJobError(code: Int?) {
        TODO("Not yet implemented")
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
        favAdapter?.notifyDataSetChanged()
    }

    private fun goToRegister() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(intent)
    }
}