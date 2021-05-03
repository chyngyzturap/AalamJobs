package com.pharos.aalamjobs.data.network

import com.pharos.aalamjobs.data.responses.Jobs
import com.pharos.aalamjobs.data.responses.JobsResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST

interface JobsApi {

    @GET("api/jobs/all/")
    suspend fun getJobs(): JobsResponse

    @POST("logout")
    suspend fun logout(): ResponseBody
}