package com.pharos.aalamjobs.data.model

import com.pharos.aalamjobs.ui.jobs.model.Organization
import com.pharos.aalamjobs.ui.jobs.model.Owner
import com.pharos.aalamjobs.ui.jobs.model.Salary

data class Jobs(
    val city: City,
    val deadline: String,
    val deadline_string: String,
    val description: String,
//    val employment_type: Int,
    val extra_benefits: String,
    val id: Int,
    val owner: Owner,
//    val payment_type: Int,
    val position: String,
    val published: String,
    val published_date: String,
    val requirements: List<String>,
    val responsibilities: List<String>,
    val salary: Salary,
    val currency: Currency,
    val schedule: String,
    val start_date: String,
    val title: String,
    val updated: String,
    val updated_date: String,
    val organization: Organization,
    var favorite: Boolean

)