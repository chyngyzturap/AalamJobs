package com.pharos.aalamjobs.ui.cv

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.dao.CvDao
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.PersonalInfo
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentPersonalInformationBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.CHOOSE_IMAGE_REQUEST
import com.wajahatkarim3.roomexplorer.RoomExplorer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PersonalInformationFragment :
    BaseFragment<CvViewModel, FragmentPersonalInformationBinding, CvRepository>() {
    var bitmap: Bitmap? = null


    private var dao: PersonalInfoDao? = null

    private val cvDao: CvDao? = null

    private var radioSexButton: RadioButton? = null

    private lateinit var gender: String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        launch {
            context?.let {
                val firstname = CvDatabase(it).getPersonalInfoDao().getFirstName()
                if (firstname != ""){
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
                if (lastname!= ""){
                    binding.tvLastName.editText?.setText(lastname)
                }
            }
        }
        launch {
            context?.let {
                val middleName = CvDatabase(it).getPersonalInfoDao().getMiddleName()
                if (middleName!= ""){
                    binding.tvMiddleName.editText?.setText(middleName)
                }
            }
        }
        launch {
            context?.let {
                val birthDate = CvDatabase(it).getPersonalInfoDao().getBirthDate()
                if (birthDate!= ""){
                    binding.tvBirthDate.editText?.setText(birthDate)
                }
            }
        }
        launch {
            context?.let {
                val citizenship = CvDatabase(it).getPersonalInfoDao().getCitizenship()
                if (citizenship != ""){
                    binding.tvCitizenship.editText?.setText(citizenship)
                }
            }
        }

        initUserData()

        binding.ivAddProfilePhoto.setOnClickListener {
            openImageChooser()
        }

        val c = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLable(c)
        }

        binding.tvBirthDate.setEndIconOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.openRoomData.setOnClickListener {
            RoomExplorer.show(context, CvDatabase::class.java, "resume_database")
        }


        binding.personalBtnNext.setOnClickListener {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val imageInByte = byteArrayOutputStream.toByteArray()



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

            // find the radiobutton by returned id
            val radioSexButton =
                radioSexGroup.findViewById<RadioButton>(selectedId)
            val gender = radioSexButton.text.toString()


            val personalInfo = PersonalInfo(
                imageInByte.toString(),
                firstName, lastName, middleName, birthDate,
                citizenship, maritalStatus, gender
            )


            val myList = arrayOf(firstName, lastName, birthDate, citizenship)
            val notNulls = myList.filter { true }

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && birthDate.isNotEmpty()
                && citizenship.isNotEmpty()
            ) {
                launch {
                    context?.let {
                        CvDatabase(it).getPersonalInfoDao()
                            .insertPersonalInfo(personalInfo)

                    }
                }
            } else {

                Toast.makeText(requireContext(), "Fill all reqiured fields", Toast.LENGTH_SHORT)
                    .show()

                if (firstName.isEmpty()) {
                    binding.tvFirstName.error = R.string.txt_required.toString()
                } else if (firstName.isNotEmpty()) {
                    binding.tvLastName.error = null
                }


                if (lastName.isEmpty()) {
                    binding.tvLastName.error = resources.getString(R.string.txt_required)
                } else if (lastName.isNotEmpty()) {
                    binding.tvLastName.error = null
                }
                if (birthDate.isEmpty()) {
                    binding.tvBirthDate.error =
                        resources.getString(R.string.txt_required)
                } else if (birthDate.isNotEmpty()) {
                    binding.tvLastName.error = null
                }

                if (citizenship.isEmpty()) {
                    binding.tvCitizenship.error =
                        resources.getString(R.string.txt_required)
                } else if (citizenship.isNotEmpty()) {
                    binding.tvLastName.error = null
                }
            }
//
//            launch {
//                context?.let {
//                    viewModel.insertPersonalInfo(personalInfo)
//
//
//                    Toast.makeText(requireContext(), "Saved, swipe to go", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//
//            viewModel.insertPersonalInfo(personalInfo)

            findNavController().navigate(R.id.action_personalInformationFragment_to_contactInformationFragment)

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
        binding.etBirthDate.setText(sdf.format(c.time))
    }

    private fun openImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выберите фото профиля!"),
            CHOOSE_IMAGE_REQUEST
        )
    }

    private fun initUserData(){

        val photo = runBlocking {userPreferences.photo.first()  }
        val fullname = runBlocking {userPreferences.fullname.first()  }

            binding.tvFirstName.editText?.setText(fullname)

            if (photo?.isNotEmpty() == true)
                Glide.with(binding.root).load(photo)
                    .error(
                        ContextCompat.getDrawable(
                            binding.root.context, R.drawable.logo
                        )
                    ).into(binding.ivPhoto)

    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_IMAGE_REQUEST)

        {
            val path = data!!.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, path)
                binding.ivPhoto.setImageBitmap(bitmap)


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

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
}



