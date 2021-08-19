package com.pharos.aalamjobs.ui.jobs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.text.isDigitsOnly
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.databinding.ActivityJobsDetailBinding
import com.pharos.aalamjobs.ui.base.BaseActivity
import com.pharos.aalamjobs.ui.jobs.adapter.JobsAdapter
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.JobListener
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.dialogfragments.SignUpDialogFragment
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class JobsDetailActivity : BaseActivity<JobsViewModel, ActivityJobsDetailBinding, JobsRepository>(),
    JobListener, JobsAdapter.JobClickListener {
    private lateinit var job: Jobs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressbar.visible(true)
        viewModel.setJobListener(this)

        getThisJob()

        binding.ivBackpressed.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getThisJob() {
        val jobId = intent.getIntExtra("jobId", 0)
        if (jobId != 0) {
            viewModel.getJobData(jobId)
        } else {
            val uri = intent.data.toString()
            var last = uri.lastIndex
            var id = ""
            while (uri[last] != '/') {
                id += uri[last]
                last--
            }
            val jobIdStr = id.reversed()
            if (jobIdStr.isDigitsOnly()) {
                viewModel.getJobData(jobIdStr.toInt())
            } else {
                Toast.makeText(this, getString(R.string.job_not_found), Toast.LENGTH_SHORT).show()
                startNewActivity(MainActivity::class.java)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setJob(jobs: Jobs) {
        binding.progressbar.visible(false)
        this.job = jobs
        val linkUri = "http://aalamjobs.com/vacancies/vacancyDetail/${jobs.id}"

        val respList = jobs.responsibilities
        var respText = ""
        val reqList = jobs.requirements
        var reqText = ""
        for (text in respList) {
            respText += "\n • $text"
        }
        for (text in reqList) {
            reqText += "\n • $text"
        }
        binding.jobTitle.text = jobs.title
        binding.tvResponsibilities.text = respText
        binding.tvRequirements.text = reqText
        binding.jobNameCompany.text = jobs.organization.name
        binding.jobNameLocation.text = jobs.city.name.en.toString() + ", " +
                jobs.city.country.name.en
        binding.jobsDate.text = jobs.published_date.split("T")[0]
        binding.tvPosition.text = jobs.title
        binding.tvSchedule.text = jobs.schedule
        binding.tvDescription.text = jobs.description
        binding.tvSalary.text =
            jobs.salary.min.toString() + "-" + jobs.salary.max + jobs.currency.sign

        binding.ivShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(getString(R.string.share_via))
                .setText(linkUri)
                .startChooser()
        }

        binding.btnSendCv.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()) {
                showSignUpDialog()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("jobIdForApply", job.id)
                startActivity(intent)
                finish()
            }
        }

        if (jobs.favorite) {
            binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
        } else {
            binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
        }

        val jobIdInt = JobId(job.id)
        binding.ivJobFavorite.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }
            if (token.isNullOrEmpty()) {
                showSignUpDialog()
            } else {
                if (!jobs.favorite) {
                    job.favorite
                    binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                    viewModel.setFavorite(jobIdInt)
                } else {
                    !jobs.favorite
                    binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                    viewModel.deleteFromFav(job.id)
                }
            }

            if (token.isNullOrEmpty()) {
                showSignUpDialog()
            } else {
                if (!jobs.favorite) {
                    job.favorite
                    binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                    viewModel.setFavorite(jobIdInt)
                } else {
                    !jobs.favorite
                    binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                    viewModel.deleteFromFav(job.id)
                }
            }
        }
    }

    private fun showSignUpDialog() {
        val signUpDialogFragment = SignUpDialogFragment()
        val manager = supportFragmentManager
        signUpDialogFragment.show(manager, "signUpDialog")
    }

    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)
        Toast.makeText(this, "Error $code ", Toast.LENGTH_SHORT).show()
    }

    override fun onJobClick(jobId: Int) {
    }

    override fun addToFavorites(position: Int) {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            showSignUpDialog()
        } else {
            val jobIdFav = JobId(job.id)
            viewModel.setFavorite(jobIdFav)
        }
    }

    override fun deleteFromFavorites(position: Int) {
    }

    override fun getViewModel() = JobsViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivityJobsDetailBinding.inflate(layoutInflater)

    override fun getActivityRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)

        if (token.isNullOrEmpty()) {
            return JobsRepository(apiNoToken)
        } else {
            return JobsRepository(api)
        }
    }
}