package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.JobsResponse


interface JobsListener {

    fun setJob(jobs: JobsResponse)
    fun getJobError(code: Int?)

//    fun postFavJobSuccess()
//fun addToFavFailed(code: Int?)
//
//    fun mustLogin()

}