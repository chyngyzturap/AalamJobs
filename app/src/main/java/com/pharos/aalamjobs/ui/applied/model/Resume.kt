package com.pharos.aalamjobs.ui.applied.model

import com.pharos.aalamjobs.data.model.Currency

data class Resume(
//    val achievements: Achievements,
    val bio: String,
    val birthdate: String,
    val citizenship: String,
    val created_at: String,
    val currency: Currency,
    val current_city: String,
    val current_country: String,
    val date_can_start: String,
    val email: String,
//    val employment_type: Int,
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
//    val other_languages: OtherLanguages,
//    val owner: Int,
    val phone: String,
    val photo: String,
//    val portfolio: Portfolio,
    val position: String,
    val profile: String,
    val salary: Int,
//    val skills: Skills,
    val social: Social,
    val updated_at: String
)