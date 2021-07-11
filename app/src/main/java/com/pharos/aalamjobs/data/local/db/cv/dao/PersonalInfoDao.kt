package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.entities.PersonalInfo

@Dao
interface PersonalInfoDao {
    //PersonalInfo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalInfo(personalInfo: PersonalInfo)

    @Delete
    suspend fun deletePersonalInfo(personalInfo: PersonalInfo)

    @Query("DELETE FROM personal_info_table")
    suspend fun deleteAllFromPersonal()

    @Query("SELECT photo FROM personal_info_table")
    fun getImageinByte(): String
    //    fun getFirstName(): LiveData<String>

    @Query("SELECT firstname FROM personal_info_table")
    fun getFirstName(): String
//    fun getFirstName(): LiveData<String>


    @Query("SELECT lastname FROM personal_info_table")
    fun getLastName(): String
//    fun getLastName(): LiveData<String>

    @Query("SELECT middlename FROM personal_info_table")
    fun getMiddleName(): String
//    fun getMiddleName(): LiveData<String>

    @Query("SELECT birthdate FROM personal_info_table")
    fun getBirthDate(): String
//    fun getBirthDate(): LiveData<String>

    @Query("SELECT citizenship FROM personal_info_table")
    fun getCitizenship(): String
//    fun getCitizenship(): LiveData<String>

    @Query("SELECT marital_status FROM personal_info_table")
    fun getMaritalStatus(): String
//    fun getMaritalStatus(): LiveData<String>

    @Query("SELECT gender FROM personal_info_table")
    fun getGender(): String
//    fun getGender(): LiveData<String>

    @Query("SELECT * FROM personal_info_table")
    fun getPersonals() : LiveData<List<PersonalInfo>>

//    @Query("SELECT firstname, lastname, middlename, birthdate, citizenship, marital_status, gender FROM personal_info_table")
//    fun getValues(): String
}