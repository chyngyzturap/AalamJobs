package com.pharos.aalamjobs.ui.resume

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentResumeBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvListener
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.ui.resume.adapter.ResumeAdapter
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ResumeFragment : BaseFragment<CvViewModel, FragmentResumeBinding, CvRepository>(), CvListener,
ResumeAdapter.CvClickListener{
    private var resumeAdapter: ResumeAdapter? = null
    private val resumes = mutableListOf<CvModelResponse>()
    private var page: Int = 1
    private var idForApply = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jobIdForApply = requireActivity().intent.getIntExtra("jobIdForApply", 0)
        if (jobIdForApply != 0) {
            binding.jobsToolbar.visible(true)
            binding.ivCancel.setOnClickListener {
                idForApply = 0
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("jobIdForApply", idForApply)
                startActivity(intent)
            }
        }

        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
            showSignUpDialog()
        } else {
            binding.progressbar.visible(true)
            viewModel.setCvListener(this)
            viewModel.getResumesList(page)
            binding.rvJobs.setHasFixedSize(true)
        }

        binding.swipeRefresh.setOnRefreshListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()){
                showSignUpDialog()
            } else {
                page = 1
                resumes.clear()
                viewModel.getResumesList(page)
            }
        }
    }

    private fun showSignUpDialog() {
        val signUpDialogFragment = SignUpDialogFragment()
        val manager = requireActivity().supportFragmentManager
        signUpDialogFragment.show(manager, "signUpDialog")
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


    override fun createCvSuccess(id: Int) {

    }

    override fun createCvFailed(code: Int?) {

    }

    override fun applyCvSuccess() {

    }

    override fun applyCvFailed(code: Int?) {
    }

    override fun setResume(cv: MutableList<CvModelResponse>) {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = requireActivity().supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            if (cv.isNotEmpty()){
                binding.swipeRefresh.isRefreshing = false
                binding.tvResumes.visible(false)
                binding.loggedContainer.visible(true)
                binding.progressbar.visible(false)
                if (page == 1)
                    resumes.clear()
                resumes.addAll(cv)
                resumeAdapter = ResumeAdapter(this)
                binding.rvJobs.adapter = resumeAdapter
                resumeAdapter?.submitList(resumes)
            }
        }
    }

    override fun getCvError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun onCvClick(cvId: Int) {
        val jobIdForApply = requireActivity().intent.getIntExtra("jobIdForApply", 0)
        val intent = Intent(requireContext(), CvWebPreviewActivity::class.java)
        intent.putExtra("cvId", cvId)
        intent.putExtra("jobIdForApply", jobIdForApply)
        startActivity(intent)
    }
}