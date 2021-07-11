package com.pharos.aalamjobs.data.repository

import androidx.lifecycle.MutableLiveData
import com.pharos.aalamjobs.data.local.db.cv.dao.CvDao
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.*
import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.utils.Coroutines


class CvRepository(
    private val api: CvApi
) : BaseRepository() {
//
//    private var cvDatabase: CvDatabase

    private val cvDatabase: CvDatabase? = null

    private val personals = MutableLiveData<List<PersonalInfo>>()

//    init {
//        personals.observeForever{
//            safePersonals(it)
//
//        }
//    }

    private lateinit var cvDao: CvDao
    private var personalDao: PersonalInfoDao? = null


    suspend fun createCV(cvModel: CvModel) = safeApiCall {
            api.createCV(cvModel)
        }

    suspend fun getResumes(page: Int) = safeApiCall {
        api.getResumes(page)
    }



    //Personal Info
    suspend fun insertPersonalInfo(personalInfo: PersonalInfo) = personalDao?.insertPersonalInfo(personalInfo)
    fun getFirstName() = personalDao?.getFirstName()
    fun getLastName() = personalDao?.getLastName()
    fun getMiddleName() = personalDao?.getMiddleName()
    fun getBirthDate() = personalDao?.getBirthDate()
    fun getCitizenship() = personalDao?.getCitizenship()
    fun getMaritalStatus() = personalDao?.getMaritalStatus()
    fun getGender() = personalDao?.getGender()

    fun getPersonals() = personalDao?.getPersonals()

    private fun safePersonals(personalInfo: PersonalInfo) {
        Coroutines.io {
            cvDatabase?.getPersonalInfoDao()?.insertPersonalInfo(personalInfo)
        }
    }

//    suspend fun getPersonals(): LiveData<List<PersonalInfo>>?{
//        return withContext(Dispatchers.IO){
//            cvDatabase?.getPersonalInfoDao()?.getPersonals()
//        }
//    }

//    fun getValues() = cvDatabase?.getPersonalInfoDao()?.getValues()

    //Contact Info
    suspend fun insertContactInfo(contactInfo: ContactInfo) = cvDatabase?.getContactInfoDao()?.insertContactInfo(contactInfo)
    fun getPhone() = cvDatabase?.getContactInfoDao()?.getPhone()
    fun getEmail() = cvDatabase?.getContactInfoDao()?.getEmail()
    fun getCurrentCountry() = cvDatabase?.getContactInfoDao()?.getCurrentCountry()
    fun getCurrentCity() = cvDatabase?.getContactInfoDao()?.getCurrentCity()
    fun getLivingAddress() = cvDatabase?.getContactInfoDao()?.getLivingAddress()

    //Education
    suspend fun insertEducation(education: Education) = cvDatabase?.getEducationDao()?.insertEducation(education)
    fun getInstitution() = cvDatabase?.getEducationDao()?.getInstitution()
    fun getEdDateFrom() = cvDatabase?.getEducationDao()?.getEdDateFrom()
    fun getEdDateTo() = cvDatabase?.getEducationDao()?.getEdDateTo()

    //Languages
    suspend fun insertLanguages(motherLanguage: MotherLanguage) = cvDatabase?.getLangDao()?.insertMotherLang(motherLanguage)
    fun getMotherLanguage() = cvDatabase?.getLangDao()?.getMotherLanguage()
    suspend fun insertOtherLang(otherLanguages: OtherLanguages) = cvDatabase?.getLangDao()?.insertOtherLang(otherLanguages)
    fun getOtherLangName() = cvDatabase?.getLangDao()?.getOtherLangName()
    fun getTalkingSkill() = cvDatabase?.getLangDao()?.getLangSkillTalking()
    fun getListeningSkill() = cvDatabase?.getLangDao()?.getLangSkillListening()
    fun getReadingSkill() = cvDatabase?.getLangDao()?.getLangSkillReading()
    fun getWritingSkill() = cvDatabase?.getLangDao()?.getLangSkillWriting()
//    //OtherLanguage
//    suspend fun insertOtherLanguages(otherLanguages: HashMap<String, HashMap<String, String>>) = cvDatabase
//        .getLangDao().insertOtherLanguages(otherLanguages)
//    = cvDao.insertOtherLanguages(otherLanguages)
//    fun getOtherLanguageSkills() = cvDao.getOtherLanguagesSkills()
//    //OtherLanguageSkills
//    suspend fun insertOtherLanguageSkills(otherLanguageSkills: OtherLanguageSkills) = cvDao.insertOtherLanguageSkills(otherLanguageSkills)
//    fun getTalkingSkill() = cvDao.getMotherLanguage()
//    fun getSpeakingSkill() = cvDao.getOtherLanguages()
//    fun getReadingSkill() = cvDao.getOtherLanguages()
//    fun getWritingSkill() = cvDao.getOtherLanguages()

    //Experience
    suspend fun insertExperience(experience: Experience) = cvDatabase?.getExperienceDao()?.insertExperience(experience)
    fun getWorkCompany() = cvDatabase?.getExperienceDao()?.getWorkCompany()
    fun getWorkDateFrom() = cvDatabase?.getExperienceDao()?.getWorkDateFrom()
    fun getWorkDateTo() = cvDatabase?.getExperienceDao()?.getWorkDateTo()
    fun getWorkPosition() = cvDatabase?.getExperienceDao()?.getWorkPosition()
    fun getWorkSkills() = cvDatabase?.getExperienceDao()?.getWorkResponsibilities()

    //Job Requirements
    suspend fun insertJobRequirements(jobRequirements: JobRequirements) = cvDatabase?.getJobReqDao()?.insertJobRequirement(jobRequirements)
    fun getSalaryExpect() = cvDatabase?.getJobReqDao()?.getSalaryExpect()
    fun getEmploymentTypeExpect() = cvDatabase?.getJobReqDao()?.getEmploymentTypeExpect()
    fun getPositionExpect() = cvDatabase?.getJobReqDao()?.getPositionExpect()
    fun getDateCanStartExpect() = cvDatabase?.getJobReqDao()?.getDateCanStartExpect()
    fun getBio() = cvDatabase?.getJobReqDao()?.getBio()


}