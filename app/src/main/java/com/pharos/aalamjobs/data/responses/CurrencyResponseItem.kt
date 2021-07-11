package com.pharos.aalamjobs.data.responses

data class CurrencyResponseItem(
    val created_at: String,
    val id: Int,
    val name: Name,
    val sign: String,
    val updated_at: String
)