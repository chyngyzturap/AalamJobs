package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pharos.aalamjobs.data.local.db.cv.entities.*

@Dao
interface CvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    //PersonalInfo
    suspend fun insertPersonalInfo(personalInfo: PersonalInfo)

    @Query("SELECT firstname FROM personal_info_table")
    fun getFirstName(): String

    @Query("SELECT lastname FROM personal_info_table")
    fun getLastName(): String

    @Query("SELECT middlename FROM personal_info_table")
    fun getMiddleName(): String

    @Query("SELECT birthdate FROM personal_info_table")
    fun getBirthDate(): String

    @Query("SELECT citizenship FROM personal_info_table")
    fun getCitizenship(): String

    @Query("SELECT marital_status FROM personal_info_table")
    fun getMaritalStatus(): String

    @Query("SELECT gender FROM personal_info_table")
    fun getGender(): String


    //ContactInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactInfo(contactInfo: ContactInfo)

    @Query("SELECT phone FROM contact_info_table")
    fun getPhone(): String

    @Query("SELECT email FROM contact_info_table")
    fun getEmail(): String

    @Query("SELECT current_country FROM contact_info_table")
    fun getCurrentCountry(): String

    @Query("SELECT current_city FROM contact_info_table")
    fun getCurrentCity(): String

    @Query("SELECT living_address FROM contact_info_table")
    fun getLivingAddress(): String


    //Education
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation(education: Education)

    @Query("SELECT institution FROM education_table")
    fun getInstitution(): String

    @Query("SELECT date_from FROM education_table")
    fun getEdDateFrom(): String

    @Query("SELECT date_to FROM education_table")
    fun getEdDateTo(): String

//    //Languages
//    @TypeConverters(Converters::class)
//    @Insert()
//    suspend fun insertLanguages(languages: Languages)
//
//    @Query("SELECT mother_language FROM languages_table")
//    fun getMotherLanguage(): String
//
//    @TypeConverters(Converters::class)
//
//    @Query("SELECT other_languages FROM languages_table")
//    fun getOtherLanguages(): HashMap<String?, String?>?

//    //OtherLanguages
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOtherLanguages(otherLanguages: HashMap<String, HashMap<String, String>>)
//
//    @Query("SELECT otherLanguageSkills FROM other_languages_table")
//    fun getOtherLanguagesSkills(): String
//
//    //OtherLanguageSkills
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOtherLanguageSkills(otherLanguageSkills: OtherLanguageSkills)
//
//    @Query("SELECT talking FROM other_languages_skills_table")
//    fun getLangSkillTalking(): String
//    @Query("SELECT speaking FROM other_languages_skills_table")
//    fun getLangSkillSpeaking(): String
//    @Query("SELECT reading FROM other_languages_skills_table")
//    fun getLangSkillReading(): String
//    @Query("SELECT writing FROM other_languages_skills_table")
//    fun getLangSkillWriting(): String

    //Experience
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience(experience: Experience)

    @Query("SELECT company FROM experience_table")
    fun getWorkCompany(): String

    @Query("SELECT date_from FROM experience_table")
    fun getWorkDateFrom(): String

    @Query("SELECT date_to FROM experience_table")
    fun getWorkDateTo(): String

    @Query("SELECT position FROM experience_table")
    fun getWorkPosition(): String

    @Query("SELECT responsibilities FROM experience_table")
    fun getWorkSkills(): String


    //JobRequirement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobRequirement(jobRequirements: JobRequirements)

    @Query("SELECT salary_expectations FROM job_req_table")
    fun getSalaryExpect(): Int

    @Query("SELECT employment_type FROM job_req_table")
    fun getEmploymentTypeExpect(): Int

    @Query("SELECT position FROM job_req_table")
    fun getPositionExpect(): String

    @Query("SELECT date_can_start FROM job_req_table")
    fun getDateCanStartExpect(): String

    @Query("SELECT bio FROM job_req_table")
    fun getBio(): String

}