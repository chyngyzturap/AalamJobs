package com.pharos.aalamjobs.data.responses

data class SectorResponseItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val specializations: List<Specialization>,
    val updated_at: String
)