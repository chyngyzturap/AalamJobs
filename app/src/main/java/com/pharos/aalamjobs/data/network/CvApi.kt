package com.pharos.aalamjobs.data.network

import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.responses.CurrencyResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CvApi {
    @POST("/api/resumes/")
    suspend fun createCV(@Body cvModel: CvModel): CvModelResponse

    @GET("/api/resumes/me/")
    suspend fun getResumes(
        @Query("page") page: Int
    ): MutableList<CvModelResponse>


}