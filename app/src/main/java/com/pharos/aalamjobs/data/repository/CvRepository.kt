package com.pharos.aalamjobs.data.repository

import com.pharos.aalamjobs.data.local.db.cv.models.ApplicationModel
import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.network.CvApi


class CvRepository(
    private val api: CvApi
) : BaseRepository() {

    suspend fun createCV(cvModel: CvModel) = safeApiCall {
        api.createCV(cvModel)
    }

    suspend fun applyCv(applicationModel: ApplicationModel) = safeApiCall {
        api.applyCv(applicationModel)
    }

    suspend fun getResumes(page: Int) = safeApiCall {
        api.getResumes(page)
    }
}