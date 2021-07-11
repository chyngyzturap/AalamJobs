package com.pharos.aalamjobs.data.responses

import com.pharos.aalamjobs.data.model.Jobs

data class JobsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Jobs>
)