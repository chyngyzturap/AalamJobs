package com.pharos.aalamjobs.ui.cv

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.OtherLanguageSkills
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.*
import com.pharos.aalamjobs.data.local.db.cv.models.CvModel
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.data.local.db.cv.models.Experience2
import com.pharos.aalamjobs.data.local.db.cv.models.Salary
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentJobReqiurementsBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.jobs.model.OtherLanguage
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.CurrencyDialogFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class JobRequirementFragment :
    BaseFragment<CvViewModel, FragmentJobReqiurementsBinding, CvRepository>(), SearchListener {

    private val educationList = mutableListOf<Education>()
    private val otherLangList = mutableListOf<OtherLanguage>()
    private val experienceList = mutableListOf<Experience2>()

    private val dao: PersonalInfoDao? = null

    private var cvModel: CvModel? = null
    private var cvModelResponse: CvModelResponse? = null
    private var education: Education? = null
    private var experience: Experience? = null
//    private var otherLanguages: OtherLanguages? = null
    private var otherLanguagesSkills: OtherLanguageSkills? = null
    private var salary: Salary? = null
    private var hasComputer: Boolean? = null
    private var preferences: UserPreferences? = null
    private var mCurrencySign: String = ""
    private var mIdCurrency: Int = 0
    private var mEmplTypeName: String = ""
    private var mIdEmpType: Int = 0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            context?.let {
                val position = CvDatabase(it).getJobReqDao().getPositionExpect()
                if (position!= ""){
                    binding.tvPosition.editText?.setText(position)
                } else {
                    val position = userPreferences.position.first()
                    binding.tvPosition.editText?.setText(position)
                }
            }
        }

        launch {
            context?.let {
                val salaryExpect = CvDatabase(it).getJobReqDao().getSalaryExpect().toString()
                val dateCanStart = CvDatabase(it).getJobReqDao().getDateCanStartExpect()
                val bio = CvDatabase(it).getJobReqDao().getBio()

                if (salaryExpect!= ""){
                    binding.tvSalary.editText?.setText(salaryExpect)
                }
                if (dateCanStart!= ""){
                    binding.tvEarliestDate.editText?.setText(dateCanStart)
                }
                if (bio!= ""){
                    binding.tvInfo.editText?.setText(bio)
                }
            }
        }

        val c = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLable(c)
        }

        binding.tvEarliestDate.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePicker, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvSalaryCurrency.setOnClickListener {
            val currencyDialogFragment = CurrencyDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            currencyDialogFragment.show(manager, "currencyDialog")
        }



        binding.jobReqBtnSave.setOnClickListener {

            val employmentType0 = mIdEmpType
            val positionExpect = binding.etPosition.text.toString().trim()
            val dateCanStart = binding.etEarliestDate.text.toString().trim()
            val profile = binding.etInfo.text.toString().trim()
            val salaryExpect = binding.etSalary.text.toString().trim().toInt()
            val salaryCurrentExpect = mIdCurrency
//            salary = Salary(salaryExpect, salaryCurrentExpect)

            val haveComp = binding.cbHave.isChecked
            val needComp = binding.cbNeed.isChecked
            if (haveComp) hasComputer = true
            if (needComp) hasComputer = false

            val jobReq = JobRequirements(salaryExpect, employmentType0, positionExpect,
            dateCanStart, profile)

            launch {
                context?.let {

                    CvDatabase(it).getJobReqDao().insertJobRequirement(jobReq)

                }
            }



//            salary = Salary(salaryExpect, salaryExpectCurrency)

//            val jobRequirements = JobRequirements(salaryExpect, employmentType0, positionExpect,
//            dateCanStart, bio, hasComputer)
//
//            viewModel.insertJobRequirements(jobRequirements)

            //Personal Info
//            val firstname = viewModel.firstName
//            val lastname = viewModel.lastName
//            val middleName = viewModel.middleName
//            val birthDate = viewModel.birthDate
//            val citizenship = viewModel.citizenship
//            val gender = viewModel.gender
//            val maritalStatus = viewModel.maritalStatus

            //Contact Info
            val phone = viewModel.phone
            val email = viewModel.email
            val currentCountry = viewModel.currentCountry
            val currentCity = viewModel.currentCity
            val livingAddress = viewModel.livingAddress

            //Education
//            val institution = viewModel.institution
//            val edDateFrom = viewModel.edDateFrom
//            val edDateTo = viewModel.edDateTo

            //Language
//            val motherLanguage = viewModel.motherLanguage
//            val otherLanguage = viewModel.otherLanguage

            //Experience
//            val workCompany = viewModel.workCompany
//            val workDateFrom = viewModel.workDateFrom
//            val workDateTo = viewModel.workDateTo
//            val workPosition = viewModel.workPosition
//            val workSkills = viewModel.workSkills
//            val experience = Experience(workCompany, workDateFrom, workDateTo, workPosition,
//            workSkills)


            launch {
                context?.let {

//                    CvDatabase(it).getJobReqDao().insertJobRequirement(jobRequirements)

                }
            }


            @Suppress("DEPRECATION")
launch {
    context?.let {
        val jsonObject=JSONObject()
        val jsonArray=JSONArray()



//        val firstname = viewModel.firstName.toString()
//        val lastname = viewModel.lastName.toString()
//        val middleName = viewModel.middleName.toString()
//        val birthDate = viewModel.birthDate.toString()
//        val citizenship = viewModel.citizenship.toString()
//        val gender = viewModel.gender.toString()
//        val maritalStatus = viewModel.maritalStatus.toString()

//        val lang = runBlocking { userPreferences.language.first() }
        val lang = run { userPreferences.language.first() }

        //Personal Info
        val imageInByte = CvDatabase(it).getPersonalInfoDao().getImageinByte().toByteArray()
        val requestFile: RequestBody =
            RequestBody.Companion.create("image".toMediaTypeOrNull(), imageInByte)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", "image", requestFile)

        val photo = File(runBlocking {userPreferences.photo.first()!!  })
        val idCv = cvModelResponse?.id

        val firstname = CvDatabase(it).getPersonalInfoDao().getFirstName().toString()
        val lastname = CvDatabase(it).getPersonalInfoDao().getLastName().toString()
        val middleName = CvDatabase(it).getPersonalInfoDao().getMiddleName().toString()
        val birthDate = CvDatabase(it).getPersonalInfoDao().getBirthDate().toString()
        val citizenship = CvDatabase(it).getPersonalInfoDao().getCitizenship().toString()
        val gender = CvDatabase(it).getPersonalInfoDao().getGender().toString()
        val maritalStatus = CvDatabase(it).getPersonalInfoDao().getMaritalStatus().toString()

        //Contact Info

        val phone = CvDatabase(it).getContactInfoDao().getPhone().toString()
        val email= CvDatabase(it).getContactInfoDao().getEmail().toString()
        val currentCountry = CvDatabase(it).getContactInfoDao().getCurrentCountry().toString()
        val currentCity = CvDatabase(it).getContactInfoDao().getCurrentCity().toString()
        val livingAddress = CvDatabase(it).getContactInfoDao().getLivingAddress()
        val whatsapp = CvDatabase(it).getContactInfoDao().getWhatsapp()
        val telegram = CvDatabase(it).getContactInfoDao().getTelegram()
        val facebook = CvDatabase(it).getContactInfoDao().getFacebook()
        val twitter = CvDatabase(it).getContactInfoDao().getTwitter()
        val linkedin = CvDatabase(it).getContactInfoDao().getLinkedIn()
        val social = Social (facebook, linkedin, twitter, whatsapp, telegram)
        val contactInfo = ContactInfo(phone, email, currentCountry, currentCity, livingAddress)

        //Education

        val institution = CvDatabase(it).getEducationDao().getInstitution()
        val edDateFrom = CvDatabase(it).getEducationDao().getEdDateFrom()
        val edDateTo = CvDatabase(it).getEducationDao().getEdDateTo()
        val edSpec = CvDatabase(it).getEducationDao().getEdSpecialization()
        val edCity= CvDatabase(it).getEducationDao().getEdCity()
        val edCountry= CvDatabase(it).getEducationDao().getEdCountry()

        val institution2 = CvDatabase(it).getEducationDao().getInstitution2()
        val edDateFrom2 = CvDatabase(it).getEducationDao().getEdDateFrom2()
        val edDateTo2 = CvDatabase(it).getEducationDao().getEdDateTo2()
        val edSpec2 = CvDatabase(it).getEducationDao().getEdSpecialization2()
        val edCity2= CvDatabase(it).getEducationDao().getEdCity2()
        val edCountry2= CvDatabase(it).getEducationDao().getEdCountry2()

        val institution3 = CvDatabase(it).getEducationDao().getInstitution3()
        val edDateFrom3 = CvDatabase(it).getEducationDao().getEdDateFrom3()
        val edDateTo3 = CvDatabase(it).getEducationDao().getEdDateTo3()
        val edSpec3 = CvDatabase(it).getEducationDao().getEdSpecialization3()
        val edCity3= CvDatabase(it).getEducationDao().getEdCity3()
        val edCountry3= CvDatabase(it).getEducationDao().getEdCountry3()

        val education = Education(institution, edDateFrom, edDateTo, edSpec, edCity, edCountry)
        val education2 = Education(institution2, edDateFrom2, edDateTo2, edSpec2, edCity2, edCountry2)
        val education3 = Education(institution3, edDateFrom3, edDateTo3, edSpec3, edCity3, edCountry3)
        educationList.add(education)
        educationList.add(education2)
        educationList.add(education3)

        //Language

        val motherLanguage = CvDatabase(it).getLangDao().getMotherLanguage()
        val otherLangName = CvDatabase(it).getLangDao().getOtherLangName()
        val otherLangReading = CvDatabase(it).getLangDao().getLangSkillReading()
        val otherLangWriting = CvDatabase(it).getLangDao().getLangSkillWriting()
        val otherLangTalking = CvDatabase(it).getLangDao().getLangSkillTalking()
        val otherLangListening = CvDatabase(it).getLangDao().getLangSkillListening()

        val otherLangName2 = CvDatabase(it).getLangDao().getOtherLangName2()
        val otherLangReading2 = CvDatabase(it).getLangDao().getLangSkillReading2()
        val otherLangWriting2 = CvDatabase(it).getLangDao().getLangSkillWriting2()
        val otherLangTalking2 = CvDatabase(it).getLangDao().getLangSkillTalking2()
        val otherLangListening2 = CvDatabase(it).getLangDao().getLangSkillListening2()

        val otherLangName3 = CvDatabase(it).getLangDao().getOtherLangName3()
        val otherLangReading3 = CvDatabase(it).getLangDao().getLangSkillReading3()
        val otherLangWriting3 = CvDatabase(it).getLangDao().getLangSkillWriting3()
        val otherLangTalking3 = CvDatabase(it).getLangDao().getLangSkillTalking3()
        val otherLangListening3 = CvDatabase(it).getLangDao().getLangSkillListening3()

        val otherLanguage = OtherLanguage(otherLangName, otherLangListening, otherLangReading,
            otherLangWriting, otherLangTalking)
        val otherLanguage2 = OtherLanguage(otherLangName2, otherLangListening2, otherLangReading2,
            otherLangWriting2, otherLangTalking2)
        val otherLanguage3 = OtherLanguage(otherLangName3, otherLangListening3, otherLangReading3,
            otherLangWriting3, otherLangTalking3)

        otherLangList.add(otherLanguage)
        otherLangList.add(otherLanguage2)
        otherLangList.add(otherLanguage3)

        //Experience

        val workCompany = CvDatabase(it).getExperienceDao().getWorkCompany()
        val workDateFrom = CvDatabase(it).getExperienceDao().getWorkDateFrom()
        val workDateTo = CvDatabase(it).getExperienceDao().getWorkDateTo()
        val workPosition = CvDatabase(it).getExperienceDao().getWorkPosition()
        val workResponsibilities = CvDatabase(it).getExperienceDao().getWorkResponsibilities()
            .split(",").toMutableList()
        val workCity = CvDatabase(it).getExperienceDao().getWorkCity()
            val workCountry = CvDatabase(it).getExperienceDao().getWorkCountry()
        val experience = Experience2(
            workCompany, workDateFrom, workDateTo, workPosition,
            workResponsibilities, workCity, workCountry)

        val workCompany2 = CvDatabase(it).getExperienceDao().getWorkCompany2()
        val workDateFrom2 = CvDatabase(it).getExperienceDao().getWorkDateFrom2()
        val workDateTo2 = CvDatabase(it).getExperienceDao().getWorkDateTo2()
        val workPosition2 = CvDatabase(it).getExperienceDao().getWorkPosition2()
        val workResponsibilities2 = CvDatabase(it).getExperienceDao().getWorkResponsibilities2()
            .split(",").toMutableList()
        val workCity2 = CvDatabase(it).getExperienceDao().getWorkCity2()
            val workCountry2 = CvDatabase(it).getExperienceDao().getWorkCountry2()
        val experience2 = Experience2(
            workCompany2, workDateFrom2, workDateTo2, workPosition2,
            workResponsibilities2, workCity2, workCountry2)

        val workCompany3 = CvDatabase(it).getExperienceDao().getWorkCompany3()
        val workDateFrom3 = CvDatabase(it).getExperienceDao().getWorkDateFrom3()
        val workDateTo3 = CvDatabase(it).getExperienceDao().getWorkDateTo3()
        val workPosition3 = CvDatabase(it).getExperienceDao().getWorkPosition3()
        val workResponsibilities3 = CvDatabase(it).getExperienceDao().getWorkResponsibilities3()
            .split(",").toMutableList()
        val workCity3 = CvDatabase(it).getExperienceDao().getWorkCity3()
            val workCountry3 = CvDatabase(it).getExperienceDao().getWorkCountry3()
        val experience3 = Experience2(
            workCompany3, workDateFrom3, workDateTo3, workPosition3,
            workResponsibilities3, workCity3, workCountry3)

        experienceList.add(experience)
        experienceList.add(experience2)
        experienceList.add(experience3)

        //Achievements

        val link = CvDatabase(it).getExperienceDao().getLinks().split(",").toMutableList()
        val achievements = CvDatabase(it).getExperienceDao().getAchievements().split(",").toMutableList()
        val skills = CvDatabase(it).getExperienceDao().getSkills().split(",").toMutableList()


        val employmentType = CvDatabase(it).getJobReqDao().getEmploymentTypeExpect().toString()
//        val employmentTypeInt = employmentType.toInt()
//        val positionExpect = CvDatabase(it).getJobReqDao().getPositionExpect().value.toString()
//        val salaryExpect = CvDatabase(it).getJobReqDao().getSalaryExpect().value.toString()
//        val dateCanStart = CvDatabase(it).getJobReqDao().getDateCanStartExpect().value.toString()
//        val bio = CvDatabase(it).getJobReqDao().getBio().value.toString()
//        val hasComputer = CvDatabase(it).getJobReqDao().getHasComputer().value.toString().toBoolean()


        cvModel = CvModel(
            //Locale
            lang,
            //Personal Info
//            photo,
            firstname, lastname, middleName, birthDate, citizenship, maritalStatus, gender,
            //Contact Info
            phone, email, currentCountry, currentCity, livingAddress, social,
            //Education
            educationList,
            //Language
            motherLanguage, otherLangList,
            //Experience
            experienceList,
            //Links
            link, skills, achievements,
            //Job Requirements
            salaryExpect, salaryCurrentExpect, employmentType0, positionExpect, dateCanStart, profile, hasComputer
        )

        viewModel.createNewCV(cvModel!!)

//        CvDatabase(it).getJobReqDao().deletePersonalInfo(personalInfo)
//        CvDatabase(it).getJobReqDao().deleteContactInfo(contactInfo)
////        CvDatabase(it).getJobReqDao().deleteLanguages(languages)
//        CvDatabase(it).getJobReqDao().deleteEducationInfo(education)
//        CvDatabase(it).getJobReqDao().deleteExperienceInfo(experience3)
//        CvDatabase(it).getJobReqDao().deletePersonalInfo(personalInfo)
    }
}



//            viewModel.createNewCV(cvModel!!)

            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnPersonal.setOnClickListener {
            findNavController().navigate(R.id.personalInformationFragment)
        }

        binding.btnContacts.setOnClickListener {
            findNavController().navigate(R.id.contactInformationFragment)
        }

        binding.btnEducation.setOnClickListener {
            findNavController().navigate(R.id.educationFragment)
        }

        binding.btnLanguage.setOnClickListener {
            findNavController().navigate(R.id.languagesFragment)
        }

        binding.btnExperience.setOnClickListener {
            findNavController().navigate(R.id.workExperienceFragment)
        }
        binding.btnAchievements.setOnClickListener {
            findNavController().navigate(R.id.linksFragment)
        }

        binding.btnRequirements.setOnClickListener {
            findNavController().navigate(R.id.jobRequirementFragment)
        }

    }

    private fun initUserData(){

        val position = runBlocking {userPreferences.position.first()  }

        binding.tvPosition.editText?.setText(position)
    }

    private fun updateLable(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etEarliestDate.setText(sdf.format(c.time))
    }

    override fun getViewModel() = CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentJobReqiurementsBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() : CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        val cvDatabase = CvDatabase
        return CvRepository(api)
    }

    override fun getCountryId(idCountry: Int, nameCountry: String) {

    }

    override fun getCityId(idCity: Int, nameCity: String) {

    }

    override fun getSectorId(idSector: Int, nameSector: String) {

    }

    override fun getCurrencySign(idCurrency: Int, currencySign: String) {
        mIdCurrency = idCurrency
        mCurrencySign = currencySign
        binding.tvSalaryCurrency.text = mCurrencySign
    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {
        mIdEmpType = idEmplType
        mEmplTypeName = nameEmplType
        binding.tvEmploymentType.editText?.setText(mEmplTypeName)
    }
}