package com.pharos.aalamjobs.ui.jobs.model

data class Vacancies(
    val count: Int,
    val next: String,
    val previous: String,
    val results: MutableList<Result>
)