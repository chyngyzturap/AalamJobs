package com.pharos.aalamjobs.ui.cv

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience2
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience3
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentWorkExperienceBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class WorkExperienceFragment : BaseFragment<CvViewModel, FragmentWorkExperienceBinding, CvRepository>() {

    private val dao: PersonalInfoDao? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            context?.let {
                val workCompany = CvDatabase(it).getExperienceDao().getWorkCompany()
                val workDateFrom = CvDatabase(it).getExperienceDao().getWorkDateFrom()
                val workDateTo = CvDatabase(it).getExperienceDao().getWorkDateTo()
                val workPosition = CvDatabase(it).getExperienceDao().getWorkPosition()
                val workResponsibilities = CvDatabase(it).getExperienceDao().getWorkResponsibilities()
                val workCity = CvDatabase(it).getExperienceDao().getWorkCity()
                val workCountry = CvDatabase(it).getExperienceDao().getWorkCountry()

                if (workCompany!= ""){
                    binding.tvCompany.editText?.setText(workCompany)
                }
                if (workDateFrom!= ""){
                    binding.tvDateFrom.editText?.setText(workDateFrom)
                }
                if (workDateTo!= ""){
                    binding.tvDateTo.editText?.setText(workDateTo)
                }
                if (workPosition!= ""){
                    binding.tvPosition.editText?.setText(workPosition)
                }
                if (workResponsibilities!= ""){
                    binding.tvResponsibilities.editText?.setText(workResponsibilities)
                }
                if (workCity!= ""){
                    binding.tvCity.editText?.setText(workCity)
                }
                if (workCountry!= ""){
                    binding.tvCountry.editText?.setText(workCountry)
                }
            }
        }
        launch {
            context?.let {
                val workCompany2 = CvDatabase(it).getExperienceDao().getWorkCompany2()
                val workDateFrom2 = CvDatabase(it).getExperienceDao().getWorkDateFrom2()
                val workDateTo2 = CvDatabase(it).getExperienceDao().getWorkDateTo2()
                val workPosition2 = CvDatabase(it).getExperienceDao().getWorkPosition2()
                val workResponsibilities2 = CvDatabase(it).getExperienceDao().getWorkResponsibilities2()
                val workCity2 = CvDatabase(it).getExperienceDao().getWorkCity2()
                val workCountry2 = CvDatabase(it).getExperienceDao().getWorkCountry2()

                if (workCompany2!= ""){
                    binding.tvCompany2.editText?.setText(workCompany2)
                }
                if (workDateFrom2!= ""){
                    binding.tvDateFrom2.editText?.setText(workDateFrom2)
                }
                if (workDateTo2!= ""){
                    binding.tvDateTo2.editText?.setText(workDateTo2)
                }
                if (workPosition2!= ""){
                    binding.tvPosition2.editText?.setText(workPosition2)
                }
                if (workResponsibilities2!= ""){
                    binding.tvResponsibilities.editText?.setText(workResponsibilities2)
                }
                if (workCity2!= ""){
                    binding.tvCity.editText?.setText(workCity2)
                }
                if (workCountry2!= ""){
                    binding.tvCountry2.editText?.setText(workCountry2)
                }
            }
        }
        launch {
            context?.let {
                val workCompany3 = CvDatabase(it).getExperienceDao().getWorkCompany3()
                val workDateFrom3 = CvDatabase(it).getExperienceDao().getWorkDateFrom3()
                val workDateTo3 = CvDatabase(it).getExperienceDao().getWorkDateTo3()
                val workPosition3 = CvDatabase(it).getExperienceDao().getWorkPosition3()
                val workResponsibilities3 = CvDatabase(it).getExperienceDao().getWorkResponsibilities3()
                val workCity3 = CvDatabase(it).getExperienceDao().getWorkCity3()
                val workCountry3 = CvDatabase(it).getExperienceDao().getWorkCountry3()

                if (workCompany3!= ""){
                    binding.tvCompany3.editText?.setText(workCompany3)
                }
                if (workDateFrom3!= ""){
                    binding.tvDateFrom3.editText?.setText(workDateFrom3)
                }
                if (workDateTo3!= ""){
                    binding.tvDateTo3.editText?.setText(workDateTo3)
                }
                if (workPosition3!= ""){
                    binding.tvPosition3.editText?.setText(workPosition3)
                }
                if (workResponsibilities3!= ""){
                    binding.tvResponsibilities3.editText?.setText(workResponsibilities3)
                }
                if (workCity3!= ""){
                    binding.tvCity3.editText?.setText(workCity3)
                }
                if (workCountry3!= ""){
                    binding.tvCountry3.editText?.setText(workCountry3)
                }
            }
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

binding.tvCompany.setEndIconOnClickListener {
    val company = binding.etCompany.text.toString().trim()
    val city = binding.etCity.text.toString().trim()
    val country = binding.etCountry.text.toString().trim()
    val dateFromWork = binding.etDateFrom.text.toString().trim()
    val dateToWork = binding.etDateTo.text.toString().trim()
    val position = binding.etPosition.text.toString().trim()
    val responsibilities = binding.etResponsibilities.text.toString().trim()

    val experience = Experience(company, dateFromWork, dateToWork, position, responsibilities, city, country)

    launch {
        context?.let {
            CvDatabase(it).getExperienceDao().insertExperience(experience)

        }
    }

    binding.workExpInfoContainer.visible(false)
    binding.workExpInfoContainer2.visible(true)
}

binding.tvCompany2.setEndIconOnClickListener {
    val company2 = binding.etCompany2.text.toString().trim()
    val city2 = binding.etCity2.text.toString().trim()
    val country2 = binding.etCountry2.text.toString().trim()
    val dateFromWork2 = binding.etDateFrom2.text.toString().trim()
    val dateToWork2 = binding.etDateTo2.text.toString().trim()
    val position2 = binding.etPosition2.text.toString().trim()
    val responsibilities2 = binding.etResponsibilities.text.toString().trim()

    val experience = Experience2(company2, dateFromWork2, dateToWork2, position2, responsibilities2, city2, country2)

    launch {
        context?.let {
            CvDatabase(it).getExperienceDao().insertExperience2(experience)

        }
    }

    binding.workExpInfoContainer.visible(false)
    binding.workExpInfoContainer2.visible(false)
    binding.workExpInfoContainer3.visible(true)
}
        binding.workExpBtnNext.setOnClickListener {

            binding.etCompany.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvCompany.error = R.string.txt_required.toString()
                }
            }

            binding.etDateFrom.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvDateFrom.error = R.string.txt_required.toString()
                }
            }

            binding.etDateTo.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvDateTo.error = R.string.txt_required.toString()
                }
            }
            binding.etPosition.doOnTextChanged { text, start, before, count ->
                if (text.isNullOrEmpty()){
                    binding.tvPosition.error = R.string.txt_required.toString()
                }
            }
            val company = binding.etCompany.text.toString().trim()
            val city = binding.etCity.text.toString().trim()
            val country = binding.etCountry.text.toString().trim()
            val dateFromWork = binding.etDateFrom.text.toString().trim()
            val dateToWork = binding.etDateTo.text.toString().trim()
            val position = binding.etPosition.text.toString().trim()
            val responsibilities = binding.etResponsibilities.text.toString().trim()

            val experience = Experience(company, dateFromWork, dateToWork, position, responsibilities, city, country)

            val company2 = binding.etCompany2.text.toString().trim()
            val city2 = binding.etCity2.text.toString().trim()
            val country2 = binding.etCountry2.text.toString().trim()
            val dateFromWork2 = binding.etDateFrom2.text.toString().trim()
            val dateToWork2 = binding.etDateTo2.text.toString().trim()
            val position2 = binding.etPosition2.text.toString().trim()
            val responsibilities2 = binding.etResponsibilities.text.toString().trim()

            val experience2 = Experience2(company2, dateFromWork2, dateToWork2, position2, responsibilities2, city2, country2)

            val company3 = binding.etCompany3.text.toString().trim()
            val city3 = binding.etCity3.text.toString().trim()
            val country3 = binding.etCountry3.text.toString().trim()
            val dateFromWork3 = binding.etDateFrom3.text.toString().trim()
            val dateToWork3 = binding.etDateTo3.text.toString().trim()
            val position3 = binding.etPosition3.text.toString().trim()
            val responsibilities3 = binding.etResponsibilities3.text.toString().trim()

            val experience3 = Experience3(company3, dateFromWork3, dateToWork3, position3, responsibilities3, city3, country3)

            launch {
                context?.let {
                    CvDatabase(it).getExperienceDao().insertExperience3(experience3)
                    CvDatabase(it).getExperienceDao().insertExperience(experience)
                    CvDatabase(it).getExperienceDao().insertExperience2(experience2)
                }
            }
//            viewModel.insertExperience(experience)

            findNavController().navigate(R.id.action_workExperienceFragment_to_linksFragment)
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

    override fun getViewModel()= CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentWorkExperienceBinding.inflate(inflater,container, false)

    override fun getFragmentRepository() : CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }


        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        return CvRepository(api)
    }
}