package com.pharos.aalamjobs.ui.jobs.model

import com.pharos.aalamjobs.data.model.Country

data class Organization(
    val address: String,
    val country: com.pharos.aalamjobs.ui.jobs.model.Country,
    val id: Int,
    val name: String,
    val phone: String,
    val sectors: List<Sector>
)