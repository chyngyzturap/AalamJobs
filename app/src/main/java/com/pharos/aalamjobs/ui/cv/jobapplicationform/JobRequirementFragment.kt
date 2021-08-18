package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.JobRequirements
import com.pharos.aalamjobs.data.local.db.cv.entities.Social
import com.pharos.aalamjobs.data.local.db.cv.models.*
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentJobReqiurementsBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvListener
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.ui.jobs.model.OtherLanguage
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.dialogfragments.CurrencyDialogDialogFragment
import com.pharos.aalamjobs.utils.dialogfragments.EmploymentTypeDialogDialogFragment
import com.pharos.aalamjobs.utils.RequiredSectionsDialogFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class JobRequirementFragment :
    BaseFragment<CvViewModel, FragmentJobReqiurementsBinding, CvRepository>(), SearchListener,
    CvListener {

    private val educationList = mutableListOf<EducationModel>()
    private val otherLangList = mutableListOf<OtherLanguage>()
    private val experienceList = mutableListOf<ExperienceModel>()

    private val dao: PersonalInfoDao? = null

    private var cvModel: CvModel? = null
    private var salary: Salary? = null
    private var hasComputer: Boolean? = null
    private var preferences: UserPreferences? = null
    private var mCurrencySign: String = ""
    private var mIdCurrency: Int = 0
    private var mEmplTypeName: String = ""
    private var mIdEmpType: Int = 0

    private var profileCode = 0
    private var contactCode = 0
    private var educationCode = 0
    private var educationCode2 = 0
    private var educationCode3 = 0
    private var languageCode = 0
    private var experienceCode = 0
    private var experienceCode2 = 0
    private var experienceCode3 = 0
    private var jobreqCode = 0

    private var idCv = 0
    private var langLocale = ""
    private var locale = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setCvListener(this)

        binding.tvEmploymentType.editText?.setOnClickListener {
            val emplType = EmploymentTypeDialogDialogFragment(this)
            val manager = requireActivity().supportFragmentManager
            emplType.show(manager, "emplTypeDialog")
        }

        launch {
            context?.let {
                val position = CvDatabase(it).getJobReqDao().getPositionExpect()
                if (position != "") {
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

                if (salaryExpect != "null") {
                    binding.tvSalary.editText?.setText(salaryExpect)
                }
                if (dateCanStart != "") {
                    binding.tvEarliestDate.editText?.setText(dateCanStart)
                }
                if (bio != "") {
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

        binding.tvEarliestDate.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvSalaryCurrency.setOnClickListener {
            val currencyDialogFragment = CurrencyDialogDialogFragment(this)
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

            val jobReq = JobRequirements(
                salaryExpect, employmentType0, positionExpect,
                dateCanStart, profile
            )

            jobreqCode =
                if (salaryExpect == 0 || employmentType0 == 0 || positionExpect == "null") {
                    0
                } else {
                    1
                }


            if (employmentType0 != 0 && positionExpect != "" && salaryExpect != 0) {
                launch {
                    context?.let {
                        CvDatabase(it).getJobReqDao().insertJobRequirement(jobReq)
                    }
                }
            } else {

                if (employmentType0 != 0) {
                    binding.tvEmploymentType.error = null
                } else {
                    binding.tvEmploymentType.error = R.string.txt_required.toString()
                }

                if (positionExpect != "") {
                    binding.tvPosition.error = null

                } else {
                    binding.tvPosition.error = resources.getString(R.string.txt_required).toString()
                }
                if (salaryExpect != 0) {
                    binding.tvSalary.error = null
                } else {

                    binding.tvSalary.error =
                        resources.getString(R.string.txt_required).toString()
                }
            }

            val lang = runBlocking { userPreferences.language.first() }
            if (lang != null) {
                langLocale = lang
            }


            @Suppress("DEPRECATION")
            launch {
                context?.let {

                    val language = CvDatabase(it).getPersonalInfoDao().getLocale().toString()
                    locale = if (language == "null") {
                        langLocale
                    } else {
                        language
                    }

                    //Personal Info
                    val firstname = CvDatabase(it).getPersonalInfoDao().getFirstName().toString()
                    val lastname = CvDatabase(it).getPersonalInfoDao().getLastName().toString()
                    val middleName = CvDatabase(it).getPersonalInfoDao().getMiddleName().toString()
                    val birthDate = CvDatabase(it).getPersonalInfoDao().getBirthDate().toString()
                    val citizenship =
                        CvDatabase(it).getPersonalInfoDao().getCitizenship().toString()
                    val gender: String? = CvDatabase(it).getPersonalInfoDao().getGender().toString()
                    val maritalStatus: String? =
                        CvDatabase(it).getPersonalInfoDao().getMaritalStatus().toString()

                    profileCode =
                        if (firstname == "null" || lastname == "null" || birthDate == "null" || citizenship == "null"
                            || gender == "null"
                            || maritalStatus == "null"
                        ) {
                            0
                        } else {
                            1
                        }

                    //Contact Info
                    val phone = CvDatabase(it).getContactInfoDao().getPhone().toString()
                    val email = CvDatabase(it).getContactInfoDao().getEmail().toString()
                    val currentCountry =
                        CvDatabase(it).getContactInfoDao().getCurrentCountry().toString()
                    val currentCity = CvDatabase(it).getContactInfoDao().getCurrentCity()
                    val livingAddress = CvDatabase(it).getContactInfoDao().getLivingAddress()
                    val whatsapp = CvDatabase(it).getContactInfoDao().getWhatsapp()
                    val telegram = CvDatabase(it).getContactInfoDao().getTelegram()
                    val facebook = CvDatabase(it).getContactInfoDao().getFacebook()
                    val twitter = CvDatabase(it).getContactInfoDao().getTwitter()
                    val linkedin = CvDatabase(it).getContactInfoDao().getLinkedIn()
                    val social = Social(facebook, linkedin, twitter, whatsapp, telegram)

                    contactCode =
                        if (phone == "null" || email == "null" || currentCountry == "null") {
                            0
                        } else {
                            1
                        }

                    //Education
                    val institution = CvDatabase(it).getEducationDao().getInstitution().toString()
                    val edDateFrom = CvDatabase(it).getEducationDao().getEdDateFrom()
                    val edDateTo = CvDatabase(it).getEducationDao().getEdDateTo()
                    val edSpec = CvDatabase(it).getEducationDao().getEdSpecialization()
                    val edCity = CvDatabase(it).getEducationDao().getEdCity()
                    val edCountry = CvDatabase(it).getEducationDao().getEdCountry()
                    //Education3
                    val institution2 = CvDatabase(it).getEducationDao().getInstitution2().toString()
                    val edDateFrom2 = CvDatabase(it).getEducationDao().getEdDateFrom2()
                    val edDateTo2 = CvDatabase(it).getEducationDao().getEdDateTo2()
                    val edSpec2 = CvDatabase(it).getEducationDao().getEdSpecialization2()
                    val edCity2 = CvDatabase(it).getEducationDao().getEdCity2()
                    val edCountry2 = CvDatabase(it).getEducationDao().getEdCountry2()
                    //Education3
                    val institution3 = CvDatabase(it).getEducationDao().getInstitution3().toString()
                    val edDateFrom3 = CvDatabase(it).getEducationDao().getEdDateFrom3()
                    val edDateTo3 = CvDatabase(it).getEducationDao().getEdDateTo3()
                    val edSpec3 = CvDatabase(it).getEducationDao().getEdSpecialization3()
                    val edCity3 = CvDatabase(it).getEducationDao().getEdCity3()
                    val edCountry3 = CvDatabase(it).getEducationDao().getEdCountry3()

                    val education =
                        EducationModel(institution, edDateFrom, edDateTo, edSpec, edCity, edCountry)
                    val education2 = EducationModel(institution2, edDateFrom2, edDateTo2, edSpec2, edCity2, edCountry2
                    )
                    val education3 = EducationModel(institution3, edDateFrom3, edDateTo3, edSpec3, edCity3, edCountry3
                    )

                    if (institution != "null") {
                        educationList.add(education)
                    }
                    if (institution2 != "null") {
                        educationList.add(education2)
                    }
                    if (institution3 != "null") {
                        educationList.add(education3)
                    }

                    //Language
                    val motherLanguage = CvDatabase(it).getLangDao().getMotherLanguage().toString()
                    languageCode = if (motherLanguage == "null") {
                        0
                    } else {
                        1
                    }

                    //OtherLang
                    val otherLangName = CvDatabase(it).getLangDao().getOtherLangName().toString()
                    val otherLangReading = CvDatabase(it).getLangDao().getLangSkillReading()
                    val otherLangWriting = CvDatabase(it).getLangDao().getLangSkillWriting()
                    val otherLangTalking = CvDatabase(it).getLangDao().getLangSkillTalking()
                    val otherLangListening = CvDatabase(it).getLangDao().getLangSkillListening()
                    //OtherLang2
                    val otherLangName2 = CvDatabase(it).getLangDao().getOtherLangName2().toString()
                    val otherLangReading2 = CvDatabase(it).getLangDao().getLangSkillReading2()
                    val otherLangWriting2 = CvDatabase(it).getLangDao().getLangSkillWriting2()
                    val otherLangTalking2 = CvDatabase(it).getLangDao().getLangSkillTalking2()
                    val otherLangListening2 = CvDatabase(it).getLangDao().getLangSkillListening2()
                    //OtherLang3
                    val otherLangName3 = CvDatabase(it).getLangDao().getOtherLangName3().toString()
                    val otherLangReading3 = CvDatabase(it).getLangDao().getLangSkillReading3()
                    val otherLangWriting3 = CvDatabase(it).getLangDao().getLangSkillWriting3()
                    val otherLangTalking3 = CvDatabase(it).getLangDao().getLangSkillTalking3()
                    val otherLangListening3 = CvDatabase(it).getLangDao().getLangSkillListening3()

                    val otherLanguage = OtherLanguage(
                        otherLangName, otherLangReading, otherLangListening,
                        otherLangTalking, otherLangWriting,
                    )
                    val otherLanguage2 = OtherLanguage(
                        otherLangName2, otherLangReading2, otherLangListening2,
                        otherLangTalking2, otherLangWriting2,
                    )
                    val otherLanguage3 = OtherLanguage(
                        otherLangName3, otherLangReading3, otherLangListening3,
                        otherLangTalking3, otherLangWriting3,
                    )

                    if (otherLangName != "null") {
                        otherLangList.add(otherLanguage)
                    }
                    if (otherLangName2 != "null") {
                        otherLangList.add(otherLanguage2)
                    }
                    if (otherLangName3 != "null") {
                        otherLangList.add(otherLanguage3)
                    }

                    //Experience
                    val workCompany = CvDatabase(it).getExperienceDao().getWorkCompany().toString()
                    val workDateFrom =
                        CvDatabase(it).getExperienceDao().getWorkDateFrom().toString()
                    val workDateTo = CvDatabase(it).getExperienceDao().getWorkDateTo().toString()
                    val workPosition =
                        CvDatabase(it).getExperienceDao().getWorkPosition().toString()
                    val workResponsibilities =
                        CvDatabase(it).getExperienceDao().getWorkResponsibilities()?.split(",")
                    val workResp = workResponsibilities?.map { it.split("\n")[0] }
                    val workCity = CvDatabase(it).getExperienceDao().getWorkCity().toString()
                    val workCountry = CvDatabase(it).getExperienceDao().getWorkCountry().toString()
                    //Experience2
                    val workCompany2 =
                        CvDatabase(it).getExperienceDao().getWorkCompany2().toString()
                    val workDateFrom2 =
                        CvDatabase(it).getExperienceDao().getWorkDateFrom2().toString()
                    val workDateTo2 = CvDatabase(it).getExperienceDao().getWorkDateTo2().toString()
                    val workPosition2 =
                        CvDatabase(it).getExperienceDao().getWorkPosition2().toString()
                    val workResponsibilities2 =
                        CvDatabase(it).getExperienceDao().getWorkResponsibilities2()
                            ?.split(",")?.map { it.split("\n")[0] }
                    val workCity2 = CvDatabase(it).getExperienceDao().getWorkCity2().toString()
                    val workCountry2 =
                        CvDatabase(it).getExperienceDao().getWorkCountry2().toString()
                    //Experience3
                    val workCompany3 =
                        CvDatabase(it).getExperienceDao().getWorkCompany3().toString()
                    val workDateFrom3 =
                        CvDatabase(it).getExperienceDao().getWorkDateFrom3().toString()
                    val workDateTo3 = CvDatabase(it).getExperienceDao().getWorkDateTo3().toString()
                    val workPosition3 =
                        CvDatabase(it).getExperienceDao().getWorkPosition3().toString()
                    val workResponsibilities3 =
                        CvDatabase(it).getExperienceDao().getWorkResponsibilities3()
                            ?.split(",")?.map { it.split("\n")[0] }
                    val workCity3 = CvDatabase(it).getExperienceDao().getWorkCity3().toString()
                    val workCountry3 =
                        CvDatabase(it).getExperienceDao().getWorkCountry3().toString()

                    val experience = ExperienceModel(
                        workCompany, workDateFrom, workDateTo, workPosition,
                        workResp, workCity, workCountry
                    )

                    val experience2 = ExperienceModel(
                        workCompany2, workDateFrom2, workDateTo2, workPosition2,
                        workResponsibilities2, workCity2, workCountry2
                    )

                    val experience3 = ExperienceModel(
                        workCompany3, workDateFrom3, workDateTo3, workPosition3,
                        workResponsibilities3, workCity3, workCountry3
                    )

                    if (workCompany != "null") {
                        experienceList.add(experience)
                    }
                    if (workCompany2 != "null") {
                        experienceList.add(experience2)
                    }
                    if (workCompany3 != "null") {
                        experienceList.add(experience3)
                    }

                    //Achievements
                    val link = CvDatabase(it).getExperienceDao().getLinks()?.split(",")
                        ?.map { it.split("\n")[0] }
                    val achievements =
                        CvDatabase(it).getExperienceDao().getAchievements()?.split(",")
                            ?.map { it.split("\n")[0] }
                    val skills = CvDatabase(it).getExperienceDao().getSkills()?.split(",")
                        ?.map { it.split("\n")[0] }

                    if (profileCode == 0
                        || contactCode == 0 || languageCode == 0
                        || jobreqCode == 0
                    ) {
                        val reqSectionsDialogFragment = RequiredSectionsDialogFragment(
                            profileCode, contactCode,
                            languageCode, jobreqCode
                        )
                        val manager = requireActivity().supportFragmentManager
                        reqSectionsDialogFragment.show(manager, "reqSecDialog")
                    } else {
                        cvModel = CvModel(
                            //Locale
                            locale,
                            //Personal Info
                            firstname,
                            lastname,
                            middleName,
                            birthDate,
                            citizenship,
                            maritalStatus,
                            gender,
                            //Contact Info
                            phone,
                            email,
                            currentCountry,
                            currentCity,
                            livingAddress,
                            social,
                            //Education
                            educationList,
                            //Language
                            motherLanguage,
                            otherLangList,
                            //Experience
                            experienceList,
                            //Links
                            link,
                            skills,
                            achievements,
                            //Job Requirements
                            salaryExpect,
                            salaryCurrentExpect,
                            employmentType0,
                            positionExpect,
                            dateCanStart,
                            profile,
                            hasComputer
                        )
                        viewModel.createNewCV(cvModel!!)
                    }
                }
            }
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

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        val api = remoteDataSource.buildApi(CvApi::class.java, token)
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

    override fun createCvSuccess(id: Int) {
        Log.d("ololo", id.toString())
        idCv = id
        if (id != 0) {
            Toast.makeText(requireContext(), "✔", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("cvId", id)
            startActivity(intent)
        }
    }

    override fun createCvFailed(code: Int?) {
    }

    override fun applyCvSuccess() {
    }

    override fun applyCvFailed(code: Int?) {
    }

    override fun setResume(cv: MutableList<CvModelResponse>) {
    }

    override fun getCvError(code: Int?) {
    }
}