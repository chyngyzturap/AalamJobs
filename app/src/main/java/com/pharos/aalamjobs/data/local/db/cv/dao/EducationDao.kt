package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.Education2
import com.pharos.aalamjobs.data.local.db.cv.entities.Education3

@Dao
interface EducationDao {
    //Education
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation(education: Education)
    //Education2
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation2(education: Education2)
    //Education3
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation3(education: Education3)

    @Query("DELETE FROM education_table")
    suspend fun deleteAllFromEducation()
    @Query("DELETE FROM education_table2")
    suspend fun deleteAllFromEducation2()
    @Query("DELETE FROM education_table3")
    suspend fun deleteAllFromEducation3()

    @Delete
    suspend fun deleteExperienceInfo(education: Education)


    @Query("SELECT * FROM education_table")
    fun getList(): List<Education>

    @Query("SELECT institution FROM education_table")
    fun getInstitution(): String?

    @Query("SELECT date_from FROM education_table")
    fun getEdDateFrom(): String?

    @Query("SELECT date_to FROM education_table")
    fun getEdDateTo(): String?

    @Query("SELECT specialization FROM education_table")
    fun getEdSpecialization(): String?

    @Query("SELECT city FROM education_table")
    fun getEdCity(): String?

    @Query("SELECT country FROM education_table")
    fun getEdCountry(): String?


    @Query("SELECT * FROM education_table2")
    fun getList2(): List<Education>

    @Query("SELECT institution FROM education_table2")
    fun getInstitution2(): String?

    @Query("SELECT date_from FROM education_table2")
    fun getEdDateFrom2(): String?

    @Query("SELECT date_to FROM education_table2")
    fun getEdDateTo2(): String?

    @Query("SELECT specialization FROM education_table2")
    fun getEdSpecialization2(): String?

    @Query("SELECT city FROM education_table2")
    fun getEdCity2(): String?

    @Query("SELECT country FROM education_table2")
    fun getEdCountry2(): String?


    @Query("SELECT * FROM education_table3")
    fun getList3(): List<Education>

    @Query("SELECT institution FROM education_table3")
    fun getInstitution3(): String?

    @Query("SELECT date_from FROM education_table3")
    fun getEdDateFrom3(): String?

    @Query("SELECT date_to FROM education_table3")
    fun getEdDateTo3(): String?

    @Query("SELECT specialization FROM education_table3")
    fun getEdSpecialization3(): String?

    @Query("SELECT city FROM education_table3")
    fun getEdCity3(): String?

    @Query("SELECT country FROM education_table3")
    fun getEdCountry3(): String?
}