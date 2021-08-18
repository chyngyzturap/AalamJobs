package com.pharos.aalamjobs.ui.applied

import com.pharos.aalamjobs.ui.applied.model.AppliedJobsResponse

interface AppliedListener {
    fun setApplied(applied: AppliedJobsResponse)
    fun getAppliedError(code: Int?)
}