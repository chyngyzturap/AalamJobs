package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.model.Jobs

interface JobListener {
    fun setJob(jobs: Jobs)
    fun getJobError(code: Int?)
}