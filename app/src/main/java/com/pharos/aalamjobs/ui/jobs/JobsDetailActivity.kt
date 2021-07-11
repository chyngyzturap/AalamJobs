package com.pharos.aalamjobs.ui.jobs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.data.network.JobsApi
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.databinding.ActivityJobsDetailBinding
import com.pharos.aalamjobs.ui.base.BaseActivity
import com.pharos.aalamjobs.ui.base.ViewModelFactory
import com.pharos.aalamjobs.ui.cv.CvActivity
import com.pharos.aalamjobs.ui.jobs.adapter.JobsAdapter
import com.pharos.aalamjobs.ui.jobs.model.JobId
import com.pharos.aalamjobs.ui.jobs.utils.JobListener
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.ui.splash.SplashActivity
import com.pharos.aalamjobs.utils.SignUpDialogFragment
import com.pharos.aalamjobs.utils.startNewActivity
import com.pharos.aalamjobs.utils.visible
import kotlinx.android.synthetic.main.activity_jobs_detail.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class JobsDetailActivity : BaseActivity<JobsViewModel, ActivityJobsDetailBinding, JobsRepository>(), KodeinAware,
    JobListener, JobsAdapter.JobClickListener {
    override val kodein: Kodein by closestKodein()
//    private lateinit var binding: ActivityJobsDetailBinding
//    private lateinit var jobsViewModel: JobsViewModel
    private val jobViewModelFactory: ViewModelFactory by instance()
    private lateinit var job: Jobs
    private var adapter: JobsAdapter? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressbar.visible(true)

//        jobsViewModel =
//            ViewModelProvider(this, jobViewModelFactory).get(JobsViewModel::class.java)


        viewModel.setJobListener(this)


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
                Toast.makeText(this, "Job not found", Toast.LENGTH_SHORT).show()
                startNewActivity(MainActivity::class.java)
            }
        }
        iv_backpressed.setOnClickListener {
            onBackPressed()
        }
    }


    override fun setJob(jobs: Jobs) {
        binding.progressbar.visible(false)

        this.job = jobs


        val linkUri = "http://165.227.143.167:9000/api/jobs/${jobs.id}"

        binding.jobTitle.text = jobs.title

//        if (!jobs.avatar.isNullOrEmpty())
//            Glide.with(this).load(jobs.avatar)
//                .error(ContextCompat.getDrawable(this, R.drawable.def_ava))
//                .into(binding.logoView)

        binding.jobNameCompany.text = jobs.organization.name
        binding.jobNameLocation.text = jobs.city.name.en.toString() + ", " +
                jobs.city.country.name.en
        binding.jobsDate.text = jobs.published_date.split("T")[0]

        binding.tvPosition.text = jobs.position
        binding.tvSchedule.text = jobs.schedule
        binding.tvSalary.text = jobs.salary.min.toString() + "-" + jobs.salary.max + jobs.salary.currency.value
        binding.tvDescription.text = jobs.description
        binding.tvResponsibilities.text = jobs.responsibilities.toString()
        binding.tvRequirements.text = jobs.requirements.toString()

//        binding.storeDescription.setOnClickListener {
//            val dialog = CustomAboutStore(jobs.description)
//            dialog.show(supportFragmentManager, "storeInfo")
//        }


        binding.ivShare.setOnClickListener {
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle("Поделиться через...")
                .setText(linkUri)
                .startChooser()
        }

        binding.btnSendCv.setOnClickListener {
            val token = runBlocking { userPreferences.tokenAccess.first() }

            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
            } else {
                startNewActivity(CvActivity::class.java)
            }

        }


        viewModel.getJobData(job.id)

        if (jobs.favorite){
            binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)

        } else {

            binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)

        }

        val jobIdInt = JobId(job.id)
        binding.ivJobFavorite.setOnClickListener{
//            val jobIdInt = intent.getIntExtra("jobId", 0)
            val token = runBlocking { userPreferences.tokenAccess.first() }

            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
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


            if (token.isNullOrEmpty()){
                val signUpDialogFragment = SignUpDialogFragment()
                val manager = supportFragmentManager
                signUpDialogFragment.show(manager, "signUpDialog")
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



    override fun getJobError(code: Int?) {
        binding.progressbar.visible(false)

        Toast.makeText(this, "Error $code ", Toast.LENGTH_SHORT).show()

    }

//    override fun postFavJobSuccess() {
//        TODO("Not yet implemented")
//    }
//
//    override fun addToFavFailed(code: Int?) {
//
//    }
//
//    override fun deleteFromFav() {
//        TODO("Not yet implemented")
//    }
//
//    override fun mustLogin() {
//        TODO("Not yet implemented")
//    }

    override fun onJobClick(jobId: Int) {

    }

    override fun addToFavorites(position: Int) {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        if (token.isNullOrEmpty()){
            val signUpDialogFragment = SignUpDialogFragment()
            val manager = supportFragmentManager
            signUpDialogFragment.show(manager, "signUpDialog")
        } else {
            val current = adapter!!.getItemAtPos(position)
            val jobIdFav = JobId(job.id)
            viewModel.setFavorite(jobIdFav)
        }
        }


    override fun deleteFromFavorites(position: Int) {
        TODO("Not yet implemented")
    }

    override fun getViewModel()= JobsViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater)= ActivityJobsDetailBinding.inflate(layoutInflater)

    override fun getActivityRepository(): JobsRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(JobsApi::class.java, token)
        val api = remoteDataSource.buildApi(JobsApi::class.java, token)

        if (token.isNullOrEmpty()){
            return JobsRepository(apiNoToken)
        } else {
            return JobsRepository(api)
        }

    }

    private fun authAlertDialog(){
        val mAlertDialog = AlertDialog.Builder(this)
//            mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon

        mAlertDialog.setPositiveButtonIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_done_24_green
            )
        )
        mAlertDialog.setNegativeButtonIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_cancel_24_red
            )
        )
        mAlertDialog.setTitle("Please sign up to get access") //set alertdialog title
//        mAlertDialog.setMessage(R.string.txt_logout_before_remind) //set alertdialog message
        mAlertDialog.setPositiveButton("Sign Up ") { dialog, id ->
            ActivityCompat.finishAffinity(this)
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
            null
        }
        mAlertDialog.show()
    }

}