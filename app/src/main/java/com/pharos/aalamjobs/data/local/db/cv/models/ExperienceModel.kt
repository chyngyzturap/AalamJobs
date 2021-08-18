package com.pharos.aalamjobs.data.local.db.cv.models

data class ExperienceModel(
val company: String?,
val date_from: String?,
val date_to: String?,
val position: String?,
val responsibilities: List<String>?,
 val city: String?,
val country: String?
)