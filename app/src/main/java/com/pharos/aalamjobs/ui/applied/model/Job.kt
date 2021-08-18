package com.pharos.aalamjobs.ui.applied.model

import com.pharos.aalamjobs.data.model.City
import com.pharos.aalamjobs.data.model.Currency
import com.pharos.aalamjobs.ui.jobs.model.Organization

data class Job(
    val city: City,
    val currency: Currency,
    val deadline: String,
    val description: String,
//    val employment_type: Int,
    val extra_benefits: String,
    val id: Int,
    val organization: Organization,
//    val owner: Int,
//    val payment_type: Int,
    val position: String,
    val published_date: String,
    val requirements: List<String>,
    val responsibilities: List<String>,
    val salary: com.pharos.aalamjobs.ui.jobs.model.Salary,
    val schedule: String,
//    val specialization: Int,
    val start_date: String,
    val title: String,
    val updated_date: String
)