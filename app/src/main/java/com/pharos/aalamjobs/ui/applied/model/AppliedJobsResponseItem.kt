package com.pharos.aalamjobs.ui.applied.model

data class AppliedJobsResponseItem(
    val created_at: String,
    val id: Int,
    val job: Job,
    val resume: Resume,
    val updated_at: String
)