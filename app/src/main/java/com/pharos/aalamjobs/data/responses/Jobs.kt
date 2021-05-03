package com.pharos.aalamjobs.data.responses

data class Jobs(
    val deadline: String,
    val deadline_string: String,
    val description: String,
    val employment_type: String,
    val extra_benefits: String,
    val id: Int,
    val location: String,
    val payment_type: String,
    val position: String,
    val published: String,
    val published_date: String,
    val requirements: String,
    val responsibilities: String,
    val salary: Int,
    val schedule: String,
    val start_date: String,
    val title: String,
    val updated: String,
    val updated_date: String
)