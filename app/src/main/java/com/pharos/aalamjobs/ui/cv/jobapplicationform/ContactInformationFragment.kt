package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.ContactInfo
import com.pharos.aalamjobs.data.local.db.cv.entities.Social
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentContactInformationBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ContactInformationFragment :
    BaseFragment<CvViewModel, FragmentContactInformationBinding, CvRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserData()
        setFromDb()



        binding.contactBtnNext.setOnClickListener {

            binding.etPhone.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()) {
                    binding.tvPhone.error = R.string.txt_required.toString()
                }
            }

            binding.etEmail.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()) {
                    binding.tvEmail.error = R.string.txt_required.toString()
                }
            }

            binding.etCountry.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()) {
                    binding.tvCountry.error = R.string.txt_required.toString()
                }
            }

            val phone = binding.etPhone.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val country = binding.etCountry.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val whatsapp = binding.etWhatsapp.text.toString().trim()
            val facebook = binding.etFacebook.text.toString().trim()
            val telegram = binding.etTelegram.text.toString().trim()
            val twitter = binding.etTwitter.text.toString().trim()
            val linkedin = binding.etLinkedin.text.toString().trim()

            val social = Social(facebook, linkedin, twitter, whatsapp, telegram)
            val contactInfo = ContactInfo(phone, email, country, city, address)

            if (phone != "" && email != "" && country != "") {
                launch {
                    context?.let {
                        CvDatabase(it).getContactInfoDao().insertContactInfo(contactInfo)
                        CvDatabase(it).getContactInfoDao().insertSocial(social)
                        findNavController().navigate(R.id.action_contactInformationFragment_to_educationFragment)
                    }
                }
            } else {
                if (phone != "") {
                    binding.tvPhone.error = null
                } else {
                    binding.tvPhone.error = R.string.txt_required.toString()
                }
                if (email != "") {
                    binding.tvEmail.error = null
                } else {
                    binding.tvEmail.error = resources.getString(R.string.txt_required).toString()
                }
                if (country != "") {
                    binding.tvCountry.error = null
                } else {
                    binding.tvCountry.error =
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

    private fun initUserData() {

        val phone = runBlocking { userPreferences.username.first() }
        val email = runBlocking { userPreferences.email.first() }
        val city = runBlocking { userPreferences.city.first() }
        val country = runBlocking { userPreferences.country.first() }

        binding.tvCity.editText?.setText(city)
        binding.tvCountry.editText?.setText(country)
        binding.tvPhone.editText?.setText(phone)
        binding.tvWhatsapp.editText?.setText(phone)
        binding.tvTelegram.editText?.setText(phone)
        binding.tvEmail.editText?.setText(email)
    }

    private fun setFromDb() {
        launch {
            context?.let {
                val phone = CvDatabase(it).getContactInfoDao().getPhone()

                if (phone != "") {
                    binding.tvPhone.editText?.setText(phone)
                } else {
                    val phone = userPreferences.username.first()
                    binding.tvPhone.editText?.setText(phone)
                }
            }
        }
        launch {
            context?.let {
                val whatsapp = CvDatabase(it).getContactInfoDao().getWhatsapp()

                if (whatsapp != "") {
                    binding.tvWhatsapp.editText?.setText(whatsapp)
                } else {
                    val phone = userPreferences.username.first()
                    binding.tvWhatsapp.editText?.setText(phone)
                }
            }
        }
        launch {
            context?.let {
                val telegram = CvDatabase(it).getContactInfoDao().getTelegram()
                if (telegram != "") {
                    binding.tvTelegram.editText?.setText(telegram)
                } else {
                    val phone = userPreferences.username.first()
                    binding.tvTelegram.editText?.setText(phone)
                }
            }
        }
        launch {
            context?.let {
                val facebook = CvDatabase(it).getContactInfoDao().getFacebook()
                if (facebook != "") {
                    binding.tvFacebook.editText?.setText(facebook)
                }
            }
        }
        launch {
            context?.let {
                val twitter = CvDatabase(it).getContactInfoDao().getTwitter()
                if (twitter != "") {
                    binding.tvTwitter.editText?.setText(twitter)
                }
            }
        }
        launch {
            context?.let {
                val linkedin = CvDatabase(it).getContactInfoDao().getLinkedIn()
                if (linkedin != "") {
                    binding.tvLinkedin.editText?.setText(linkedin)
                }
            }
        }
        launch {
            context?.let {
                val email = CvDatabase(it).getContactInfoDao().getEmail()
                if (email != "") {
                    binding.tvEmail.editText?.setText(email)
                }
            }
        }
        launch {
            context?.let {
                val currentCountry = CvDatabase(it).getContactInfoDao().getCurrentCountry()
                if (currentCountry != "") {
                    binding.tvCountry.editText?.setText(currentCountry)
                }
            }
        }

        launch {
            context?.let {
                val currentCity = CvDatabase(it).getContactInfoDao().getCurrentCity()
                if (currentCity != "") {
                    binding.tvCity.editText?.setText(currentCity)
                }
            }
        }
        launch {
            context?.let {
                val livingAddress = CvDatabase(it).getContactInfoDao().getLivingAddress()
                if (livingAddress != "") {
                    binding.tvAddress.editText?.setText(livingAddress)
                }
            }
        }
    }

    override fun getViewModel() = CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentContactInformationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        val cvDatabase = CvDatabase
        return CvRepository(api)
    }
}