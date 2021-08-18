package com.pharos.aalamjobs.data.local.db.cv.models

import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.Social
import com.pharos.aalamjobs.ui.jobs.model.OtherLanguage
import com.pharos.aalamjobs.ui.jobs.model.Owner

data class CvModelResponse(
    val achievements: List<String>,
    val bio: String,
    val birthdate: String,
    val citizenship: String,
    val created_at: String,
    val current_city: String,
    val current_country: String,
    val date_can_start: String,
    val education: List<Education>,
    val email: String,
    val experience: List<ExperienceModel>,
    val firstname: String,
    val gender: String,
    val has_computer: Boolean,
    val id: Int,
    val lastname: String,
    val living_address: String,
    val locale: String,
    val marital_status: String,
    val middlename: String,
    val mother_language: String,
    val other_languages: List<OtherLanguage>,
    val owner: Owner,
    val phone: String,
    val portfolio: List<String>,
    val position: String,
    val profile: String,
    val salary_expectations: Salary,
    val skills: List<String>,
    val social: Social,
    val updated_at: String
)