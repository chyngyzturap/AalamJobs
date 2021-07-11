package com.pharos.aalamjobs.ui.jobs.model

data class OrganizationX(
    val address: String,
    val country: Country,
    val id: Int,
    val name: String,
    val phone: String,
    val sectors: List<Sector>
)