package com.pharos.aalamjobs.data.local.db.cv.models

import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.Social
import com.pharos.aalamjobs.ui.jobs.model.OtherLanguage

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
    val employment_type: Int,
    val experience: List<Experience2>,
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
    val owner: Int,
    val phone: String,
    val portfolio: List<String>,
    val position: String,
    val profile: String,
    val salary_expectations: Salary,
    val skills: List<String>,
    val social: Social,
    val updated_at: String


//    //Locale
//    val locale: String,
//
//    //PersonalInfo
//    val firstname: String?,
//    val lastname: String?,
//    val middlename: String?,
//    val birthdate: String?,
//    val citizenship: String?,
//    val marital_status: String?,
//    val gender: String?,
//
//    //ContactInfo
//    val phone: String?,
//    val email: String?,
//    val current_country: String?,
//    val current_city: String?,
//    val living_address: String?,
//    val social: Social,
//
//    //Education
//    val education: List<Education>,
//
//    //Language
//    val mother_language: String?,
//    val other_languages: List<OtherLanguages>,
//
//    //Experience
//    val experience: List<Experience2>,
//
//    //Links
//    val portfolio: List<String>,
//    val skills: List<String>,
//    val achievements: List<String>,
//
//    //JobRequirements
//    val salary_expectations: Salary?,
//    val employment_type: Int,
//    val position: String?,
//    val date_can_start: String?,
//    val profile: String?,
//    val has_computer: Boolean?,
//
//    //Response
//    val created_at: String,
//    val id: Int,
//    val updated_at: String
)