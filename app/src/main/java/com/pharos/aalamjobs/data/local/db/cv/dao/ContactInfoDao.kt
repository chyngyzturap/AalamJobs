package com.pharos.aalamjobs.data.local.db.cv.dao

import androidx.room.*
import com.pharos.aalamjobs.data.local.db.cv.entities.ContactInfo
import com.pharos.aalamjobs.data.local.db.cv.entities.Social

@Dao
interface ContactInfoDao {
    //ContactInfo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactInfo(contactInfo: ContactInfo)

    @Query("DELETE FROM contact_info_table")
    suspend fun deleteAllFromContacts()

    @Delete
    suspend fun deleteContactInfo(contactInfo: ContactInfo)

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

    //Social
    @Insert
    suspend fun insertSocial(social: Social)

    @Query("SELECT whatsapp FROM social_table")
    fun getWhatsapp(): String

    @Query("SELECT telegram FROM social_table")
    fun getTelegram(): String

    @Query("SELECT facebook FROM social_table")
    fun getFacebook(): String

    @Query("SELECT twitter FROM social_table")
    fun getTwitter(): String

    @Query("SELECT linkedin FROM social_table")
    fun getLinkedIn(): String

}