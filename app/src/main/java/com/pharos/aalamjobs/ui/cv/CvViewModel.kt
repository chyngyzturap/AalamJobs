package com.pharos.aalamjobs.ui.cv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pharos.aalamjobs.data.local.db.cv.entities.*
import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.Resource
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.ui.base.BaseViewModel
import com.pharos.aalamjobs.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class CvViewModel(
    private val repository: CvRepository
) : BaseViewModel(repository) {


    private val preferences: UserPreferences? = null

    //Personal Info
    val firstName = repository.getFirstName()
    val lastName = repository.getLastName()
    val middleName = repository.getMiddleName()
    val birthDate = repository.getBirthDate()
    val citizenship = repository.getCitizenship()
    val gender = repository.getGender()
    val maritalStatus = repository.getMaritalStatus()
    val allPersonalInfo = repository.getPersonals()

    val personals by lazyDeferred {
        repository.getPersonals()
    }
//    val value = repository.getValues(firstName)

    //Contact Info
    val phone = repository.getPhone()
    val email = repository.getEmail()
    val currentCountry = repository.getCurrentCountry()
    val currentCity = repository.getCurrentCity()
    val livingAddress = repository.getLivingAddress()

    //Education
    val institution = repository.getInstitution()
    val edDateFrom = repository.getEdDateFrom()
    val edDateTo = repository.getEdDateTo()

    //Language
    val motherLanguage = repository.getMotherLanguage()
    val otherLanguage = repository.getOtherLangName()
    val talking = repository.getTalkingSkill()
    val listening = repository.getListeningSkill()
    val reading = repository.getReadingSkill()
    val writing = repository.getWritingSkill()


    //Experience
    val workCompany = repository.getWorkCompany()
    val workDateFrom = repository.getWorkDateFrom()
    val workDateTo = repository.getWorkDateTo()
    val workPosition = repository.getWorkPosition()
    val workSkills = repository.getWorkSkills()

    //Job Requirements
    val salaryExpectation = repository.getSalaryExpect()
    val employmentType = repository.getEmploymentTypeExpect()
    val positionExpectation = repository.getPositionExpect()
    val dataCanStart = repository.getDateCanStartExpect()
    val bio = repository.getBio()


    private var listener: CvListener? = null

    private val _cvModel: MutableLiveData<Resource<CvModel>> = MutableLiveData()
    val cvModel: LiveData<Resource<CvModel>>
        get() = _cvModel

    fun createNewCV(cvModel: CvModel) = viewModelScope.launch {
        when (val response = repository.createCV(cvModel)) {
            is Resource.Success -> {
                listener?.createCvSuccess()
            }
            is Resource.Failure -> {
                listener?.createCvFailed(response.errorCode)
            }
        }
    }

    fun setCvListener(listener: CvListener) {
        this.listener = listener
    }

        fun updateAppLanguage(lang: String) {
        GlobalScope.launch(Dispatchers.Main) {
            preferences?.saveLang(lang)
        }
    }

//    fun getLang() = viewModelScope.launch {
//        preferences?.language?.collectLatest { language ->
//            userDataLocal.role.collectLatest { role ->
//                listener?.getUserData(username, role)
//            }
//        }
//    }


    fun getResumesList(page: Int) = viewModelScope.launch {
        when (val response = repository.getResumes(page)) {
            is Resource.Success -> {
                listener?.setResume(response.value)
            }
            is Resource.Failure -> {
                listener?.getCvError(response.errorCode)
            }
        }
    }


    private fun newCV(
        model: CvModel
    ) = viewModelScope.launch {
        when (val response = repository.createCV(model)) {
            is Resource.Success -> {
//                if (code == 1)
//                    loginListener?.loginSuccess(response.value)
//                else
//                    saveUserData(response.value)
            }
            is Resource.Failure -> {
//                if (code == 1 && response.errorCode != 1)
//                    loginListener?.loginFail(response.errorCode)
//                else
//                    loginListener?.signInFail(response.errorBody, response.errorCode)
            }
        }
    }

    fun insertPersonalInfo(personalInfo: PersonalInfo) = viewModelScope.launch {
        repository.insertPersonalInfo(personalInfo)
    }

    fun insertContactInfo(contactInfo: ContactInfo) = viewModelScope.launch {
        repository.insertContactInfo(contactInfo)
    }

    fun insertEducation(education: Education) = viewModelScope.launch {
        repository.insertEducation(education)
    }

    fun insertExperience(experience: Experience) = viewModelScope.launch {
        repository.insertExperience(experience)
    }

    fun insertJobRequirements(jobRequirements: JobRequirements) = viewModelScope.launch {
        repository.insertJobRequirements(jobRequirements)
    }

    fun insertLanguages(motherLanguage: MotherLanguage) = viewModelScope.launch {
        repository.insertLanguages(motherLanguage)
    }
//
    fun insertOtherLanguages(otherLanguages: OtherLanguages) =
        viewModelScope.launch {
            repository.insertOtherLang(otherLanguages)
        }
//
//    fun insertOtherLanguageSkills(otherLanguageSkills: OtherLanguageSkills) =
//        viewModelScope.launch {
//            repository.insertOtherLanguageSkills(otherLanguageSkills)
//        }
}