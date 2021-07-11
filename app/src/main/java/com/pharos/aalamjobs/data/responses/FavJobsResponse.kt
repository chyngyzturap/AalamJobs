package com.pharos.aalamjobs.data.responses

import com.pharos.aalamjobs.data.model.FavJobs

data class FavJobsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<FavJobs>
)