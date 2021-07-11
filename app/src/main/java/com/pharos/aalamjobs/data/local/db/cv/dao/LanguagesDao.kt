package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.Converters
import com.pharos.aalamjobs.data.local.db.cv.entities.MotherLanguage
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages2
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages3

@Dao
interface LanguagesDao {
    //Languages
    @TypeConverters(Converters::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMotherLang(motherLanguage: MotherLanguage)

    @Delete
    suspend fun deleteLanguages(motherLanguage: MotherLanguage)

    @Query("DELETE FROM mother_languages_table")
    suspend fun deleteAllFromMotherLanguage()

    @Query("DELETE FROM other_languages_table")
    suspend fun deleteAllFromOtherLanguage()
    @Query("DELETE FROM other_languages_table2")
    suspend fun deleteAllFromOtherLanguage2()
    @Query("DELETE FROM other_languages_table3")
    suspend fun deleteAllFromOtherLanguage3()

    @Query("SELECT mother_language FROM mother_languages_table")
    fun getMotherLanguage(): String

//    @TypeConverters(Converters::class)
//    @Query("SELECT * FROM languages_table")
//    fun getOtherLanguages(): Languages

    //OtherLanguages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherLang(otherLanguages: OtherLanguages)
    //OtherLanguages2
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherLang2(otherLanguages: OtherLanguages2)
    //OtherLanguages3
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherLang3(otherLanguages: OtherLanguages3)

    @Query("SELECT name FROM other_languages_table")
    fun getOtherLangName(): String

    @Query("SELECT talking FROM other_languages_table")
    fun getLangSkillTalking(): String
    @Query("SELECT listening FROM other_languages_table")
    fun getLangSkillListening(): String
    @Query("SELECT reading FROM other_languages_table")
    fun getLangSkillReading(): String
    @Query("SELECT writing FROM other_languages_table")
    fun getLangSkillWriting(): String

    @Query("SELECT name FROM other_languages_table2")
    fun getOtherLangName2(): String

    @Query("SELECT talking FROM other_languages_table2")
    fun getLangSkillTalking2(): String
    @Query("SELECT listening FROM other_languages_table2")
    fun getLangSkillListening2(): String
    @Query("SELECT reading FROM other_languages_table2")
    fun getLangSkillReading2(): String
    @Query("SELECT writing FROM other_languages_table2")
    fun getLangSkillWriting2(): String

    @Query("SELECT name FROM other_languages_table3")
    fun getOtherLangName3(): String

    @Query("SELECT talking FROM other_languages_table3")
    fun getLangSkillTalking3(): String
    @Query("SELECT listening FROM other_languages_table3")
    fun getLangSkillListening3(): String
    @Query("SELECT reading FROM other_languages_table3")
    fun getLangSkillReading3(): String
    @Query("SELECT writing FROM other_languages_table3")
    fun getLangSkillWriting3(): String



}