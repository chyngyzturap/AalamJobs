package com.pharos.aalamjobs.ui.jobs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.data.model.TokenAccess
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.data.responses.FavJobsResponse
import com.pharos.aalamjobs.data.responses.JobsResponse
import com.pharos.aalamjobs.databinding.FragmentJobsBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvListener
import com.pharos.aalamjobs.ui.jobs.adapter.JobsAdapter
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.FavoriteListener
import com.pharos.aalamjobs.ui.jobs.utils.JobsListener
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*

class JobsFragment : BaseFragment<JobsViewModel, FragmentJobsBinding, JobsRepository>(),
    JobsListener,
    JobsAdapter.JobClickListener, FavoriteListener, CvListener {
    private var jobsAdapter: JobsAdapter? = null
    private val jobs = mutableListOf<Jobs>()
    private var page: Int = 1
    private var search: String = ""
    private var countryIntent: Int = 0
    private var cityIntent: Int = 0
    private var sectorIntent: Int = 0
    private var file: File? = null
    var requestImage: MultipartBody.Part? = null

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyToken()
        goToResumeFromSendCvBtn()
        binding.progressbar.visible(true)
        viewModel.setJobsListener(this)
        binding.rvJobs.setHasFixedSize(true)
        filterList()
        uploadPhotoToCreatedCv()

        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                binding.progressbar.visible(true)
                page++
                verifyToken()
                viewModel.getJobsList(page, search)
                binding.progressbar.visible(false)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            page = 1
            jobs.clear()
            verifyToken()
            viewModel.getJobsList(page, search)
        }

        binding.ivSearchDetail.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()) {
                showSignUpDialog()
            } else {
                val intent = Intent(requireContext(), JobSearchDetailActivity::class.java)
                startActivity(intent)
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                getJobsList()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun goToResumeFromSendCvBtn() {
        val jobIdForApply = requireActivity().intent.getIntExtra("jobIdForApply", 0)
        if (jobIdForApply != 0) {
            findNavController().navigate(R.id.nav_resume)
        }
    }

    private fun verifyToken() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()){
        } else {
            val access = TokenAccess(token)
            viewModel.verify(access)
        }
    }

    private fun showSignUpDialog() {
        val signUpDialogFragment = SignUpDialogFragment()
        val manager = requireActivity().supportFragmentManager
        signUpDialogFragment.show(manager, "signUpDialog")
    }

    private fun uploadPhotoToCreatedCv() {
        val cvIdForPatch = requireActivity().intent.getIntExtra("cvId", 0)
        if (cvIdForPatch != 0) {
            launch {
                context?.let {
                    val photoUri = CvDatabase(it).getPersonalInfoDao().getPhoto()
                    file = fileFromContentUri(requireContext(), photoUri)
                    val requestFile: RequestBody =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file!!)
                    requestImage =
                        MultipartBody.Part.createFormData("photo", file!!.name, requestFile)
                    viewModel.uploadPhoto(cvIdForPatch, requestImage)
                }
            }
        }
    }

    private fun filterList() {
        countryIntent = requireActivity().intent.getIntExtra("countryId", 0)
        val countryArray = "[$countryIntent]"

        cityIntent = requireActivity().intent.getIntExtra("cityId", 0)
        val cityArray = "[$cityIntent]"

        sectorIntent = requireActivity().intent.getIntExtra("sectorId", 0)
        val sectorArray = "[$sectorIntent]"

        if (countryIntent == 0 && cityIntent == 0 && sectorIntent == 0) {
            viewModel.getJobsList(page, search)
        }

        val options: HashMap<String, String> = HashMap<String, String>()
        options.put("page", "$page")
        if (countryIntent != 0) {
            options.put("country", countryArray)
            viewModel.getJobsFilteredList(options)
        }
        if (cityIntent != 0) {
            options.put("city", cityArray)
            viewModel.getJobsFilteredList(options)
        }
        if (sectorIntent != 0) {
            options.put("sector", sectorArray)
            viewModel.getJobsFilteredList(options)
        }
    }

    private fun getJobsList() {
        viewModel.getJobsList(page, binding.etSearch.text.toString())
        jobsAdapter = JobsAdapter(this)
        binding.rvJobs.setHasFixedSize(true)
        binding.rvJobs.adapter = jobsAdapter
        binding.swipeRefresh.isRefreshing = false
        jobsAdapter?.submitList(jobs)
    }

    override fun getViewModel() = JobsViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentJobsBinding.inflate(inflater, container, false)

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
            showSignUpDialog()
        } else {
            val jobIdFav = JobId(position)
            viewModel.setFavorite(jobIdFav)
        }
    }

    override fun deleteFromFavorites(position: Int) {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            showSignUpDialog()
        } else {
            viewModel.deleteFromFav(position)
        }
    }

    override fun setJob(jobs: JobsResponse) {
        binding.swipeRefresh.isRefreshing = false
        binding.progressbar.visible(false)
        if (page == 1)
            this.jobs.clear()
        this.jobs.addAll(jobs.results)
        jobsAdapter = JobsAdapter(this)
        binding.rvJobs.adapter = jobsAdapter
        jobsAdapter?.submitList(this.jobs)
        jobsAdapter?.notifyDataSetChanged()
    }

    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)
        binding.swipeRefresh.isRefreshing = false
        if (code != 404){
            Toast.makeText(requireContext(), "Error $code ", Toast.LENGTH_SHORT).show()
        }
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

    private fun fileFromContentUri(context: Context, uri: Uri?): File {
        val fileExtension = getFileExtension(context, uri)
        val fileName = "temp_file.jpg" + if (fileExtension != null) ".$fileExtension" else ""
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val osStream = FileOutputStream(tempFile)
            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }

            inputStream?.let {
                copy(inputStream, osStream)
            }
            osStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri?): String? {
        val fileType: String? = uri?.let { context.contentResolver.getType(it) }
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
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
    }

    override fun getCvError(code: Int?) {
    }

}