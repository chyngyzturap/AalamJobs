package com.pharos.aalamjobs.data.responses

data class JobsResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Jobs>
)