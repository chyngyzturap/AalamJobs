package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.LangLocale
import com.pharos.aalamjobs.data.local.db.cv.entities.PersonalInfo
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentPersonalInformationBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.wajahatkarim3.roomexplorer.RoomExplorer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class PersonalInformationFragment :
    BaseFragment<CvViewModel, FragmentPersonalInformationBinding, CvRepository>() {
    var bitmap: Bitmap? = null
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(1, 1).getIntent(requireActivity())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private var uri2: Uri? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.langChange.setOnClickListener {
            showChangeLang()
        }

        cropActivityResultLauncher =
            requireActivity().registerForActivityResult(cropActivityResultContract) {
                it?.let { uri ->
                    binding.ivPhoto.setImageURI(uri)
                    uri2 = uri
                }
            }
        setFromDb()
        initUserData()

        binding.ivAddProfilePhoto.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }

        val c = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLable(c)
        }

        binding.tvBirthDate.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.openRoomData.setOnClickListener {
            RoomExplorer.show(context, CvDatabase::class.java, "resume_database")
        }

        binding.personalBtnNext.setOnClickListener {
            val firstName =
                binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val middleName =
                binding.etMiddleName.text.toString().trim()
            val birthDate =
                binding.etBirthDate.text.toString().trim()
            val citizenship =
                binding.etCitizenship.text.toString().trim()
            val maritalStatus =
                binding.spinnerMaritalStatus.selectedItem.toString()

            val radioSexGroup =
                binding.radioGroup.findViewById<RadioGroup>(R.id.radioGroup)
            val selectedId = radioSexGroup.checkedRadioButtonId
            val radioSexButton =
                radioSexGroup.findViewById<RadioButton>(selectedId)
            val gender = radioSexButton.text.toString()

            val personalInfo = PersonalInfo(
                uri2,
                firstName, lastName, middleName, birthDate,
                citizenship, maritalStatus, gender
            )

            if (firstName != "" && lastName != "" && birthDate != "" && citizenship != "") {
                launch {
                    context?.let {
                        CvDatabase(it).getPersonalInfoDao()
                            .insertPersonalInfo(personalInfo)
                        findNavController().navigate(R.id.action_personalInformationFragment_to_contactInformationFragment)
                    }
                }
            } else {

                if (firstName != "") {
                    binding.tvFirstName.error = null
                } else {
                    binding.tvFirstName.error = R.string.txt_required.toString()
                }

                if (lastName != "") {
                    binding.tvLastName.error = null

                } else {
                    binding.tvLastName.error = resources.getString(R.string.txt_required).toString()
                }
                if (birthDate != "") {
                    binding.tvBirthDate.error = null
                } else {

                    binding.tvBirthDate.error =
                        resources.getString(R.string.txt_required).toString()
                }

                if (citizenship != "") {
                    binding.tvCitizenship.error = null
                } else {

                    binding.tvCitizenship.error =
                        resources.getString(R.string.txt_required).toString()
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

    private fun setFromDb() {
        launch {
            context?.let {
                val firstname = CvDatabase(it).getPersonalInfoDao().getFirstName()
                if (firstname != "") {
                    binding.tvFirstName.editText?.setText(firstname)
                } else {
                    val fullname = userPreferences.fullname.first()
                    binding.tvFirstName.editText?.setText(fullname)
                }
            }
        }
        launch {
            context?.let {
                val lastname = CvDatabase(it).getPersonalInfoDao().getLastName()
                if (lastname != "") {
                    binding.tvLastName.editText?.setText(lastname)
                }
            }
        }
        launch {
            context?.let {
                val middleName = CvDatabase(it).getPersonalInfoDao().getMiddleName()
                if (middleName != "") {
                    binding.tvMiddleName.editText?.setText(middleName)
                }
            }
        }
        launch {
            context?.let {
                val birthDate = CvDatabase(it).getPersonalInfoDao().getBirthDate()
                if (birthDate != "") {
                    binding.tvBirthDate.editText?.setText(birthDate)
                }
            }
        }
        launch {
            context?.let {
                val citizenship = CvDatabase(it).getPersonalInfoDao().getCitizenship()
                if (citizenship != "") {
                    binding.tvCitizenship.editText?.setText(citizenship)
                }
            }
        }
    }

    private fun updateLable(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etBirthDate.setText(sdf.format(c.time))
    }

    private fun initUserData() {
        val photo = runBlocking { userPreferences.photo.first() }
        if (uri2.toString() == "") {
            uri2 = photo?.toUri()
        }
        val fullname = runBlocking { userPreferences.fullname.first() }
        binding.tvFirstName.editText?.setText(fullname)
        if (photo?.isNotEmpty() == true)
            Glide.with(binding.root).load(photo)
                .into(binding.ivPhoto)
    }

    override fun getViewModel() = CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPersonalInformationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        return CvRepository(api)
    }

    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")
        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle(getString(R.string.choose_lang))
        mBuilder.setSingleChoiceItems(langList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    val langEn = LangLocale("en")
                    launch {
                        context?.let {
                            CvDatabase(it).getPersonalInfoDao()
                                .insertLangLocale(langEn)
                        }
                    }
                    setLocale(requireContext(), "en")
                }
                1 -> {
                    val langKy = LangLocale("en")
                    launch {
                        context?.let {
                            CvDatabase(it).getPersonalInfoDao()
                                .insertLangLocale(langKy)
                        }
                    }
                    setLocale(requireContext(), "ky")
                }
                2 -> {
                    val langRu = LangLocale("ru")
                    launch {
                        context?.let {
                            CvDatabase(it).getPersonalInfoDao()
                                .insertLangLocale(langRu)
                        }
                    }
                    setLocale(requireContext(), "ru")
                }
                3 -> {
                    val langTr = LangLocale("tr")
                    launch {
                        context?.let {
                            CvDatabase(it).getPersonalInfoDao()
                                .insertLangLocale(langTr)
                        }
                    }
                    setLocale(requireContext(), "tr")
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    @Suppress("DEPRECATION")
    private fun setLocale(context: Context, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        ActivityCompat.recreate(requireActivity())
    }
}



