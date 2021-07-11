package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.entities.*

@Dao
interface JobReqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobRequirement(jobRequirements: JobRequirements)

    @Query("DELETE FROM job_req_table")
    suspend fun deleteAllFromJobReq()

    @Query("SELECT salary_expectations FROM job_req_table")
    fun getSalaryExpect(): Int

    @Delete
    suspend fun deleteJobReqInfo(jobRequirements: JobRequirements)

    @Delete
    suspend fun deleteContactInfo(contactInfo: ContactInfo)

    @Delete
    suspend fun deleteExperienceInfo(experience: Experience)

    @Delete
    suspend fun deleteEducationInfo(education: Education)

    @Delete
    suspend fun deleteLanguages(motherLanguage: MotherLanguage)

    @Delete
    suspend fun deletePersonalInfo(personalInfo: PersonalInfo)

    @Query("SELECT employment_type FROM job_req_table")
    fun getEmploymentTypeExpect(): Int

    @Query("SELECT position FROM job_req_table")
    fun getPositionExpect(): String

    @Query("SELECT date_can_start FROM job_req_table")
    fun getDateCanStartExpect(): String

    @Query("SELECT bio FROM job_req_table")
    fun getBio(): String

}