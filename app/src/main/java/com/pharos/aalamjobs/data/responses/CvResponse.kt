package com.pharos.aalamjobs.data.responses

import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse

data class CvResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<CvModelResponse>
)