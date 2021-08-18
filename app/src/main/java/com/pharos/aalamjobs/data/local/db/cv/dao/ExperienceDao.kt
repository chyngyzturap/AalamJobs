package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.entities.Achievements
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience2
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience3

@Dao
interface ExperienceDao {
    //Experience
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience(experience: Experience)

    //Experience2
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience2(experience: Experience2)

    //Experience3
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience3(experience: Experience3)

    @Query("DELETE FROM experience_table")
    suspend fun deleteAllFromExperience()
    @Query("DELETE FROM experience_table2")
    suspend fun deleteAllFromExperience2()
    @Query("DELETE FROM experience_table3")
    suspend fun deleteAllFromExperience3()

    @Delete
    suspend fun deleteExperienceInfo(experience: Experience)

    @Query("SELECT company FROM experience_table")
    fun getWorkCompany(): String?

    @Query("SELECT date_from FROM experience_table")
    fun getWorkDateFrom(): String?

    @Query("SELECT date_to FROM experience_table")
    fun getWorkDateTo(): String?

    @Query("SELECT position FROM experience_table")
    fun getWorkPosition(): String?

    @Query("SELECT responsibilities FROM experience_table")
    fun getWorkResponsibilities(): String?

    @Query("SELECT city FROM experience_table")
    fun getWorkCity(): String?

    @Query("SELECT country FROM experience_table")
    fun getWorkCountry(): String?


    @Query("SELECT company FROM experience_table2")
    fun getWorkCompany2(): String?

    @Query("SELECT date_from FROM experience_table2")
    fun getWorkDateFrom2(): String?

    @Query("SELECT date_to FROM experience_table2")
    fun getWorkDateTo2(): String?

    @Query("SELECT position FROM experience_table2")
    fun getWorkPosition2(): String?

    @Query("SELECT responsibilities FROM experience_table2")
    fun getWorkResponsibilities2(): String?

    @Query("SELECT city FROM experience_table2")
    fun getWorkCity2(): String?

    @Query("SELECT country FROM experience_table2")
    fun getWorkCountry2(): String?


    @Query("SELECT company FROM experience_table3")
    fun getWorkCompany3(): String?

    @Query("SELECT date_from FROM experience_table3")
    fun getWorkDateFrom3(): String?

    @Query("SELECT date_to FROM experience_table3")
    fun getWorkDateTo3(): String?

    @Query("SELECT position FROM experience_table3")
    fun getWorkPosition3(): String?

    @Query("SELECT responsibilities FROM experience_table3")
    fun getWorkResponsibilities3(): String?

    @Query("SELECT city FROM experience_table3")
    fun getWorkCity3(): String?

    @Query("SELECT country FROM experience_table3")
    fun getWorkCountry3(): String?

    //Links
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(achievements: Achievements)

    @Query("SELECT links FROM links_table")
    fun getLinks(): String?

    @Query("SELECT achievements FROM links_table")
    fun getAchievements(): String?

    @Query("SELECT skills FROM links_table")
    fun getSkills(): String?
}