package com.pharos.aalamjobs.ui.resume

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentResumeBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvListener
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.ui.resume.adapter.ResumeAdapter
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ResumeFragment : BaseFragment<CvViewModel, FragmentResumeBinding, CvRepository>(), CvListener,
ResumeAdapter.CvClickListener{

    private var resumeAdapter: ResumeAdapter? = null
    private val resumes = mutableListOf<CvModelResponse>()
    private var page: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = runBlocking { userPreferences.tokenAccess.first() }


        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            binding.progressbar.visible(true)
            viewModel.setCvListener(this)
            viewModel.getResumesList(page)
            binding.rvJobs.setHasFixedSize(true)
        }



        binding.swipeRefresh.setOnRefreshListener {

            val token = runBlocking { userPreferences.tokenAccess.first() }

            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = requireActivity().supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                page = 1
                resumes.clear()
                viewModel.getResumesList(page)
            }
        }
    }

    override fun getViewModel()= CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentResumeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(CvApi::class.java, token)
        val api = remoteDataSource.buildApi(CvApi::class.java, token)


        if (token.isNullOrEmpty()){
            return CvRepository(apiNoToken)
        } else {
            return CvRepository(api)
        }
    }

    override fun createCvSuccess() {
        TODO("Not yet implemented")
    }

    override fun createCvFailed(code: Int?) {
        TODO("Not yet implemented")
    }

    override fun setResume(cv: MutableList<CvModelResponse>) {
        val token = runBlocking { userPreferences.tokenAccess.first() }


        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            binding.swipeRefresh.isRefreshing = false
            binding.progressbar.visible(false)
            if (page == 1)
                resumes.clear()
            resumes.addAll(cv)
            resumeAdapter = ResumeAdapter(this)
            binding.rvJobs.adapter = resumeAdapter
            resumeAdapter?.submitList(resumes)
        }

    }

    override fun getCvError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun setUserId(id: Int?) {
        TODO("Not yet implemented")
    }

    override fun onCvClick(cvId: Int) {
        val intent = Intent(requireContext(), CvWebPreviewActivity::class.java)
        intent.putExtra("cvId", cvId)
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