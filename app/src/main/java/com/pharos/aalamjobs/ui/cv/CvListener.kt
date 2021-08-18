package com.pharos.aalamjobs.ui.cv

import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse

interface CvListener {
    fun createCvSuccess(id: Int)
    fun createCvFailed(code: Int?)

    fun applyCvSuccess()
    fun applyCvFailed(code: Int?)

    fun setResume(cv: MutableList<CvModelResponse>)
    fun getCvError(code: Int?)
}