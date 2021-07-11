package com.pharos.aalamjobs.ui.cv

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.Education2
import com.pharos.aalamjobs.data.local.db.cv.entities.Education3
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentEducationBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class EducationFragment : BaseFragment<CvViewModel, FragmentEducationBinding, CvRepository>() {

    private val dao: PersonalInfoDao? = null
    private val list = mutableListOf<Education>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        launch {
            context?.let {
                val institution = CvDatabase(it).getEducationDao().getInstitution()
                val edDateFrom = CvDatabase(it).getEducationDao().getEdDateFrom()
                val edDateTo = CvDatabase(it).getEducationDao().getEdDateTo()
                val edSpec = CvDatabase(it).getEducationDao().getEdSpecialization()
                val edCity = CvDatabase(it).getEducationDao().getEdCity()
                val edCountry = CvDatabase(it).getEducationDao().getEdCountry()

                if (institution != "") {
                    binding.tvEducation.editText?.setText(institution)
                }
                if (edDateFrom != "") {
                    binding.tvDateFrom.editText?.setText(edDateFrom)
                }
                if (edDateTo != "") {
                    binding.tvDateTo.editText?.setText(edDateTo)
                }
                if (edSpec != "") {
                    binding.tvSpecialization.editText?.setText(edSpec)
                }
                if (edCountry != "") {
                    binding.tvCountry.editText?.setText(edCountry)
                }
                if (edCity != "") {
                    binding.tvCity.editText?.setText(edCity)
                }
            }

            launch {
                context?.let {
                    val institution2 = CvDatabase(it).getEducationDao().getInstitution2()
                    val edDateFrom2 = CvDatabase(it).getEducationDao().getEdDateFrom2()
                    val edDateTo2 = CvDatabase(it).getEducationDao().getEdDateTo2()
                    val edSpec2 = CvDatabase(it).getEducationDao().getEdSpecialization2()
                    val edCity2 = CvDatabase(it).getEducationDao().getEdCity2()
                    val edCountry2 = CvDatabase(it).getEducationDao().getEdCountry2()

                    if (institution2 != "") {
                        binding.tvEducation2.editText?.setText(institution2)
                    }
                    if (edDateFrom2 != "") {
                        binding.tvDateFrom2.editText?.setText(edDateFrom2)
                    }
                    if (edDateTo2 != "") {
                        binding.tvDateTo2.editText?.setText(edDateTo2)
                    }
                    if (edSpec2 != "") {
                        binding.tvSpecialization2.editText?.setText(edSpec2)
                    }
                    if (edCountry2 != "") {
                        binding.tvCountry2.editText?.setText(edCountry2)
                    }
                    if (edCity2 != "") {
                        binding.tvCity2.editText?.setText(edCity2)
                    }
                }
            }

            launch {
                context?.let {
                    val institution3 = CvDatabase(it).getEducationDao().getInstitution3()
                    val edDateFrom3 = CvDatabase(it).getEducationDao().getEdDateFrom3()
                    val edDateTo3 = CvDatabase(it).getEducationDao().getEdDateTo3()
                    val edSpec3 = CvDatabase(it).getEducationDao().getEdSpecialization3()
                    val edCity3 = CvDatabase(it).getEducationDao().getEdCity3()
                    val edCountry3 = CvDatabase(it).getEducationDao().getEdCountry3()

                    if (institution3 != "") {
                        binding.tvEducation3.editText?.setText(institution3)
                    }
                    if (edDateFrom3 != "") {
                        binding.tvDateFrom3.editText?.setText(edDateFrom3)
                    }
                    if (edDateTo3 != "") {
                        binding.tvDateTo3.editText?.setText(edDateTo3)
                    }
                    if (edSpec3 != "") {
                        binding.tvSpecialization3.editText?.setText(edSpec3)
                    }
                    if (edCountry3 != "") {
                        binding.tvCountry3.editText?.setText(edCountry3)
                    }
                    if (edCity3 != "") {
                        binding.tvCity3.editText?.setText(edCity3)
                    }
                }

            }
        }



        binding.tvAttach.setOnClickListener {
            Toast.makeText(requireContext(), R.string.txt_click_here_to_choose_file, Toast.LENGTH_SHORT).show()
        }

        val c = Calendar.getInstance()
        val datePickerDateFrom = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLableDateFrom(c)
        }
        val datePickerDateTo = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.YEAR, year)
            updateLableDateTo(c)
        }
        val c2 = Calendar.getInstance()
        val datePickerDateFrom2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c2.set(Calendar.MONTH, month)
            c2.set(Calendar.YEAR, year)
            updateLableDateFrom2(c2)
        }
        val datePickerDateTo2 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c2.set(Calendar.MONTH, month)
            c2.set(Calendar.YEAR, year)
            updateLableDateTo2(c)
        }
        val c3 = Calendar.getInstance()
        val datePickerDateFrom3 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c3.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c3.set(Calendar.MONTH, month)
            c3.set(Calendar.YEAR, year)
            updateLableDateFrom3(c)
        }
        val datePickerDateTo3 = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c3.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            c3.set(Calendar.MONTH, month)
            c3.set(Calendar.YEAR, year)
            updateLableDateTo3(c)
        }

        binding.tvDateFrom.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateFrom, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvDateTo.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateTo, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.tvDateFrom2.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateFrom2, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
                c2.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvDateTo2.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateTo2, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
                c2.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.tvDateFrom3.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateFrom3, c3.get(Calendar.YEAR), c3.get(Calendar.MONTH),
                c3.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvDateTo3.setEndIconOnClickListener {
            DatePickerDialog(requireContext(), datePickerDateTo3, c3.get(Calendar.YEAR), c3.get(Calendar.MONTH),
                c3.get(Calendar.DAY_OF_MONTH)).show()
        }



        binding.tvEducation.setEndIconOnClickListener {
            val educationTitle = binding.etEducation.text.toString().trim()
            val dateFrom = binding.etDateFrom.text.toString().trim()
            val dateTo = binding.etDateTo.text.toString().trim()
            val specialization = binding.etSpecialization.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val country = binding.etCountry.text.toString().trim()

            val education = Education(educationTitle, dateFrom, dateTo, specialization, city, country)

            launch {
                context?.let {
                    CvDatabase(it).getEducationDao().insertEducation(education)
                }
            }
            binding.educationInfoContainer.visible(false)
            binding.educationInfoContainer2.visible(true)

        }
        binding.tvEducation2.setEndIconOnClickListener {

            val educationTitle2 = binding.etEducation2.text.toString().trim()
            val dateFrom2 = binding.etDateFrom2.text.toString().trim()
            val dateTo2 = binding.etDateTo2.text.toString().trim()
            val specialization2 = binding.etSpecialization2.text.toString().trim()
            val city2 = binding.etCity2.text.toString().trim()
            val country2 = binding.etCountry2.text.toString().trim()

            val education2 = Education2(educationTitle2, dateFrom2, dateTo2, specialization2, city2, country2)


            launch {
                context?.let {

                    CvDatabase(it).getEducationDao().insertEducation2(education2)
                }
            }
            binding.educationInfoContainer.visible(false)
            binding.educationInfoContainer2.visible(false)
            binding.educationInfoContainer3.visible(true)

        }
        binding.tvEducation3.setEndIconOnClickListener {
            Toast.makeText(requireContext(), "Next", Toast.LENGTH_SHORT).show()
        }


        binding.edBtnNext.setOnClickListener {

            binding.etEducation.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvEducation.error = resources.getString(R.string.txt_required)
                }
            }

            binding.etDateFrom.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvDateFrom.error = resources.getString(R.string.txt_required)
                }
            }

            binding.etDateTo.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvDateTo.error = resources.getString(R.string.txt_required)
                }
            }
            val educationTitle = binding.etEducation.text.toString().trim()
            val dateFrom = binding.etDateFrom.text.toString().trim()
            val dateTo = binding.etDateTo.text.toString().trim()
            val specialization = binding.etSpecialization.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val country = binding.etCountry.text.toString().trim()

            val education = Education(educationTitle, dateFrom, dateTo, specialization, city, country)

            val educationTitle2 = binding.etEducation2.text.toString().trim()
            val dateFrom2 = binding.etDateFrom2.text.toString().trim()
            val dateTo2 = binding.etDateTo2.text.toString().trim()
            val specialization2 = binding.etSpecialization2.text.toString().trim()
            val city2 = binding.etCity2.text.toString().trim()
            val country2 = binding.etCountry2.text.toString().trim()

            val education2 = Education2(educationTitle2, dateFrom2, dateTo2, specialization2, city2, country2)

            val educationTitle3 = binding.etEducation3.text.toString().trim()
            val dateFrom3 = binding.etDateFrom3.text.toString().trim()
            val dateTo3 = binding.etDateTo3.text.toString().trim()
            val specialization3 = binding.etSpecialization3.text.toString().trim()
            val city3 = binding.etCity3.text.toString().trim()
            val country3 = binding.etCountry3.text.toString().trim()

            val education3 = Education3(educationTitle3, dateFrom3, dateTo3, specialization3, city3, country3)

            launch {
                context?.let {
                    CvDatabase(it).getEducationDao().insertEducation(education)

                    CvDatabase(it).getEducationDao().insertEducation2(education2)

                    CvDatabase(it).getEducationDao().insertEducation3(education3)
                }
            }
//            viewModel.insertEducation(education)


//            val bundle = Bundle()
//            bundle.putString("education", educationTitle)
//            bundle.putString("dateFrom", dateFrom)
//            bundle.putString("dateTo", dateTo)
//            bundle.putString("gender", gender.toString())
//            bundle.putString("firstName", firstName)
//            bundle.putString("lastName", lastName)
//            bundle.putString("middleName", middleName)
//            bundle.putString("birthDate", birthDate)
//            bundle.putString("citizenship", citizenship)
//            bundle.putString("gender", gender.toString())
//            bundle.putString("maritalStatus", maritalStatus.toString())
//            bundle.putString("phone", phone)
//            bundle.putString("email", email)
//            bundle.putString("country", country)
//            bundle.putString("city", city)
//            bundle.putString("address", address)
//
//            val fragment: Fragment = LanguagesFragment()
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.education_container, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()


            findNavController().navigate(R.id.action_educationFragment_to_languagesFragment)
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
    private fun updateLableDateFrom(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateFrom.setText(sdf.format(c.time))
    }
    private fun updateLableDateTo(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateTo.setText(sdf.format(c.time))
    }
    private fun updateLableDateFrom2(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateFrom2.setText(sdf.format(c.time))
    }
    private fun updateLableDateTo2(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateTo2.setText(sdf.format(c.time))
    }
    private fun updateLableDateFrom3(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateFrom3.setText(sdf.format(c.time))
    }
    private fun updateLableDateTo3(c: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.etDateTo3.setText(sdf.format(c.time))
    }

    override fun getViewModel() = CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentEducationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() : CvRepository {
        val token = runBlocking {userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        val cvDatabase = CvDatabase
        return CvRepository(api)
    }
}