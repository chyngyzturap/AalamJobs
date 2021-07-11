package com.pharos.aalamjobs.data.responses

data class CountryResponseItem(
    val country_code: String,
    val created_at: String,
    val id: Int,
    val is_active: Boolean,
    val name: Name,
    val updated_at: String
)