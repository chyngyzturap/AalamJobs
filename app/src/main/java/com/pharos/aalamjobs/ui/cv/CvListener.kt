package com.pharos.aalamjobs.ui.cv

import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse

interface CvListener {

    fun createCvSuccess()
    fun createCvFailed(code: Int?)

    fun setResume(cv: MutableList<CvModelResponse>)
    fun getCvError(code: Int?)


    fun setUserId(id: Int?)

}