package com.pharos.aalamjobs.data.local.db.cv.models

import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.Social
import com.pharos.aalamjobs.ui.jobs.model.OtherLanguage

data class CvModel(
    //Locale
    val locale: String?,

    //PersonalInfo
//    val photo: File,
    val firstname: String?,
    val lastname: String?,
    val middlename: String?,
    val birthdate: String?,
    val citizenship: String?,
    val marital_status: String?,
    val gender: String?,

    //ContactInfo
    val phone: String?,
    val email: String?,
    val current_country: String?,
    val current_city: String?,
    val living_address: String?,
    val social: Social,

    //Education
    val education: List<Education>,

    //Language
    val mother_language: String?,
    val other_languages: MutableList<OtherLanguage>,

    //Experience
    val experience: List<Experience2>,

    //Links
    val portfolio: List<String>,
    val skills: List<String>,
    val achievements: List<String>,

    //JobRequirements
    val salary: Int,
    val currency: Int,
    val employment_type: Int,
    val position: String?,
    val date_can_start: String?,
    val profile: String?,
    val has_computer: Boolean?,
)