package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience2
import com.pharos.aalamjobs.data.local.db.cv.entities.Experience3
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentWorkExperienceBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.utils.enable
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class WorkExperienceFragment :
    BaseFragment<CvViewModel, FragmentWorkExperienceBinding, CvRepository>() {
    private var s = ""
    private var s2 = ""
    private var s3 = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entryChip()
        entryChip2()
        entryChip3()
        setFromDB()

        binding.checkboxDateTo.setOnCheckedChangeListener { _, b ->
            binding.tvDateTo.isEnabled = !b
        }
        binding.checkboxDateTo2.setOnCheckedChangeListener { _, b ->
            binding.tvDateTo2.isEnabled = !b
        }
        binding.checkboxDateTo3.setOnCheckedChangeListener { _, b ->
            binding.tvDateTo3.isEnabled = !b
        }

        binding.tvCompany.setEndIconOnClickListener {
            toSecondExp()
        }

        binding.tvCompany2.setEndIconOnClickListener {
            toThirdExp()
        }
        binding.workExpBtnNext.setOnClickListener {
            saveWithNextBtn()
        }


        val c = Calendar.getInstance()
        val datePickerDateFrom =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
        val datePickerDateFrom2 =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c2.set(Calendar.MONTH, month)
                c2.set(Calendar.YEAR, year)
                updateLableDateFrom2(c2)
            }
        val datePickerDateTo2 =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c2.set(Calendar.MONTH, month)
                c2.set(Calendar.YEAR, year)
                updateLableDateTo2(c)
            }
        val c3 = Calendar.getInstance()
        val datePickerDateFrom3 =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c3.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c3.set(Calendar.MONTH, month)
                c3.set(Calendar.YEAR, year)
                updateLableDateFrom3(c)
            }
        val datePickerDateTo3 =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                c3.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c3.set(Calendar.MONTH, month)
                c3.set(Calendar.YEAR, year)
                updateLableDateTo3(c)
            }

        binding.tvDateFrom.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDateFrom, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvDateTo.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDateTo, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvDateFrom2.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerDateFrom2,
                c2.get(Calendar.YEAR),
                c2.get(Calendar.MONTH),
                c2.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvDateTo2.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDateTo2, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
                c2.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.tvDateFrom3.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePickerDateFrom3,
                c3.get(Calendar.YEAR),
                c3.get(Calendar.MONTH),
                c3.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.tvDateTo3.editText?.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePickerDateTo3, c3.get(Calendar.YEAR), c3.get(Calendar.MONTH),
                c3.get(Calendar.DAY_OF_MONTH)
            ).show()
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

    private fun saveWithNextBtn() {
        val company = binding.etCompany.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()
        val dateFromWork = binding.etDateFrom.text.toString().trim()
        var dateToWork = binding.etDateTo.text.toString().trim()
        binding.checkboxDateTo.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tvDateTo.isEnabled = !b
                dateToWork = "till"
            }
        }
        val position = binding.etPosition.text.toString().trim()
        val responsibilities = binding.etResponsibilities.text.toString().trim()

        val experience = Experience(
            company,
            dateFromWork,
            dateToWork,
            position,
            s,
            city,
            country
        )

        if (company != "" && position != "" && country != "" && dateFromWork != ""
            && dateToWork != "" && city != ""
        ) {

            launch {
                context?.let {
                    CvDatabase(it).getExperienceDao().insertExperience(experience)
                    findNavController().navigate(R.id.linksFragment)

                }
            }
        } else {
            if (company != "" && position != "" && country != "" && dateFromWork != ""
                && dateToWork != "" && city != ""
            ) {

                launch {
                    context?.let {
                        CvDatabase(it).getExperienceDao().insertExperience(experience)
                        findNavController().navigate(R.id.linksFragment)

                    }
                }
            } else {

                findNavController().navigate(R.id.linksFragment)

            }
        }

        val company2 = binding.etCompany2.text.toString().trim()
        val city2 = binding.etCity2.text.toString().trim()
        val country2 = binding.etCountry2.text.toString().trim()
        val dateFromWork2 = binding.etDateFrom2.text.toString().trim()
        var dateToWork2 = binding.etDateTo2.text.toString().trim()
        binding.checkboxDateTo2.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tvDateTo2.isEnabled = !b
                dateToWork2 = "till"
            }
        }
        val position2 = binding.etPosition2.text.toString().trim()

        val experience2 = Experience2(
            company2,
            dateFromWork2,
            dateToWork2,
            position2,
            s2,
            city2,
            country2
        )

        if (company2 != "" && position2 != "" && country2 != "" && dateFromWork2 != ""
            && dateToWork2 != "" && city2 != ""
        ) {

            launch {
                context?.let {
                    CvDatabase(it).getExperienceDao().insertExperience2(experience2)
                    findNavController().navigate(R.id.linksFragment)

                }
            }
        } else {

            if (company2 != "") {
                binding.tvCompany2.error = null
            } else {
                binding.tvCompany2.error = R.string.txt_required.toString()
            }

            if (position2 != "") {
                binding.tvPosition2.error = null

            } else {
                binding.tvPosition2.error = resources.getString(R.string.txt_required).toString()
            }
            if (country2 != "") {
                binding.tvCountry2.error = null
            } else {

                binding.tvCountry2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (city2 != "") {
                binding.tvCity2.error = null
            } else {

                binding.tvCity2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateFromWork2 != "") {
                binding.tvDateFrom2.error = null
            } else {

                binding.tvDateFrom2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateToWork2 != "") {
                binding.tvDateTo2.error = null
            } else {

                binding.tvDateTo2.error =
                    resources.getString(R.string.txt_required).toString()
            }
        }

        val company3 = binding.etCompany3.text.toString().trim()
        val city3 = binding.etCity3.text.toString().trim()
        val country3 = binding.etCountry3.text.toString().trim()
        val dateFromWork3 = binding.etDateFrom3.text.toString().trim()
        var dateToWork3 = binding.etDateTo3.text.toString().trim()
        binding.checkboxDateTo3.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tvDateTo3.isEnabled = !b
                dateToWork3 = "till"
            }
        }
        val position3 = binding.etPosition3.text.toString().trim()

        val experience3 = Experience3(
            company3,
            dateFromWork3,
            dateToWork3,
            position3,
            s3,
            city3,
            country3
        )

        if (company3 != "" && position != "" && country != "" && dateFromWork != ""
            && dateToWork != "" && city != ""
        ) {

            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$company3 ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getExperienceDao().insertExperience3(experience3)
                    findNavController().navigate(R.id.linksFragment)
                }
            }
        } else {

            if (company3 != "") {
                binding.tvCompany3.error = null
            } else {
                binding.tvCompany3.error = R.string.txt_required.toString()
            }

            if (position3 != "") {
                binding.tvPosition3.error = null

            } else {
                binding.tvPosition3.error = resources.getString(R.string.txt_required).toString()
            }
            if (country3 != "") {
                binding.tvCountry3.error = null
            } else {

                binding.tvCountry3.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (city3 != "") {
                binding.tvCity3.error = null
            } else {

                binding.tvCity3.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateFromWork3 != "") {
                binding.tvDateFrom3.error = null
            } else {

                binding.tvDateFrom3.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateToWork3 != "") {
                binding.tvDateTo3.error = null
            } else {

                binding.tvDateTo3.error =
                    resources.getString(R.string.txt_required).toString()
            }
        }
    }

    private fun toSecondExp() {
        val company = binding.etCompany.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()
        val dateFromWork = binding.etDateFrom.text.toString().trim()
        var dateToWork = binding.etDateTo.text.toString().trim()
        binding.checkboxDateTo.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tvDateTo.isEnabled = !b
                dateToWork = "till"
            }
        }
        val position = binding.etPosition.text.toString().trim()

        val experience = Experience(
            company,
            dateFromWork,
            dateToWork,
            position,
            s,
            city,
            country
        )

        if (company != "" && position != "" && country != "" && dateFromWork != ""
            && dateToWork != "" && city != ""
        ) {

            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$company ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getExperienceDao().insertExperience(experience)
                    binding.workExpInfoContainer.visible(false)
                    binding.workExpInfoContainer2.visible(true)
                }
            }
        } else {

            if (company != "") {
                binding.tvCompany.error = null
            } else {
                binding.tvCompany.error = R.string.txt_required.toString()
            }

            if (position != "") {
                binding.tvPosition.error = null

            } else {
                binding.tvPosition.error = resources.getString(R.string.txt_required).toString()
            }
            if (country != "") {
                binding.tvCountry.error = null
            } else {

                binding.tvCountry.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (city != "") {
                binding.tvCity.error = null
            } else {

                binding.tvCity.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateFromWork != "") {
                binding.tvDateFrom.error = null
            } else {

                binding.tvDateFrom.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateToWork != "") {
                binding.tvDateTo.error = null
            } else {

                binding.tvDateTo.error =
                    resources.getString(R.string.txt_required).toString()
            }
        }
    }

    private fun toThirdExp() {
        val company2 = binding.etCompany2.text.toString().trim()
        val city2 = binding.etCity2.text.toString().trim()
        val country2 = binding.etCountry2.text.toString().trim()
        val dateFromWork2 = binding.etDateFrom2.text.toString().trim()
        var dateToWork2 = binding.etDateTo2.text.toString().trim()
        binding.checkboxDateTo2.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.tvDateTo2.isEnabled = !b
                dateToWork2 = "till"
            }
        }
        val position2 = binding.etPosition2.text.toString().trim()

        val experience = Experience2(
            company2,
            dateFromWork2,
            dateToWork2,
            position2,
            s2,
            city2,
            country2
        )

        if (company2 != "" && position2 != "" && country2 != "" && dateFromWork2 != ""
            && dateToWork2 != "" && city2 != ""
        ) {

            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$company2 ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getExperienceDao().insertExperience2(experience)
                    binding.workExpInfoContainer.visible(false)
                    binding.workExpInfoContainer2.visible(false)
                    binding.workExpInfoContainer3.visible(true)
                }
            }
        } else {

            if (company2 != "") {
                binding.tvCompany2.error = null
            } else {
                binding.tvCompany2.error = R.string.txt_required.toString()
            }

            if (position2 != "") {
                binding.tvPosition2.error = null

            } else {
                binding.tvPosition2.error = resources.getString(R.string.txt_required).toString()
            }
            if (country2 != "") {
                binding.tvCountry2.error = null
            } else {

                binding.tvCountry2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (city2 != "") {
                binding.tvCity2.error = null
            } else {

                binding.tvCity2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateFromWork2 != "") {
                binding.tvDateFrom2.error = null
            } else {

                binding.tvDateFrom2.error =
                    resources.getString(R.string.txt_required).toString()
            }
            if (dateToWork2 != "") {
                binding.tvDateTo2.error = null
            } else {

                binding.tvDateTo2.error =
                    resources.getString(R.string.txt_required).toString()
            }
        }
    }

    private fun entryChip() {
        binding.tvResponsibilities.setEndIconOnClickListener {
//                view, i, keyEvent ->
//            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
            binding.apply {
                val responsibility = etResponsibilities.text.toString()
                createChips(responsibility)
                if (s.isNotEmpty())
                    s += ",$responsibility"
                else s += responsibility
                etResponsibilities.text?.clear()
            }
//                return@setOnKeyListener true
//            }
//            false
        }
    }

    private fun setFromDB() {

        launch {
            context?.let {
                val workCompany = CvDatabase(it).getExperienceDao().getWorkCompany()
                val workDateFrom = CvDatabase(it).getExperienceDao().getWorkDateFrom()
                val workDateTo = CvDatabase(it).getExperienceDao().getWorkDateTo()
                val workPosition = CvDatabase(it).getExperienceDao().getWorkPosition()
                val workResponsibilities =
                    CvDatabase(it).getExperienceDao().getWorkResponsibilities()
                val workCity = CvDatabase(it).getExperienceDao().getWorkCity()
                val workCountry = CvDatabase(it).getExperienceDao().getWorkCountry()

                if (workCompany != "") {
                    binding.tvCompany.editText?.setText(workCompany)
                }
                if (workDateFrom != "") {
                    binding.tvDateFrom.editText?.setText(workDateFrom)
                }
                if (workDateTo != "") {
                    binding.tvDateTo.editText?.setText(workDateTo)
                }
                if (workDateTo == "till") {
                    binding.tvDateTo.enable(false)
                    binding.checkboxDateTo.isChecked = true
                }
                if (workPosition != "") {
                    binding.tvPosition.editText?.setText(workPosition)
                }
                if (workCity != "") {
                    binding.tvCity.editText?.setText(workCity)
                }
                if (workCountry != "") {
                    binding.tvCountry.editText?.setText(workCountry)
                }
            }
        }
        launch {
            context?.let {
                val workCompany2 = CvDatabase(it).getExperienceDao().getWorkCompany2()
                val workDateFrom2 = CvDatabase(it).getExperienceDao().getWorkDateFrom2()
                var workDateTo2 = CvDatabase(it).getExperienceDao().getWorkDateTo2()
                val tillNow = binding.checkboxDateTo2.isChecked
                if (tillNow) workDateTo2 = "till"
                val workPosition2 = CvDatabase(it).getExperienceDao().getWorkPosition2()
                val workResponsibilities2 =
                    CvDatabase(it).getExperienceDao().getWorkResponsibilities2()
                val workCity2 = CvDatabase(it).getExperienceDao().getWorkCity2()
                val workCountry2 = CvDatabase(it).getExperienceDao().getWorkCountry2()

                if (workCompany2 != "") {
                    binding.tvCompany2.editText?.setText(workCompany2)
                }
                if (workDateFrom2 != "") {
                    binding.tvDateFrom2.editText?.setText(workDateFrom2)
                }
                if (workDateTo2 != "") {
                    binding.tvDateTo2.editText?.setText(workDateTo2)
                }
                if (workDateTo2 == "till") {
                    binding.tvDateTo2.enable(false)
                    binding.checkboxDateTo2.isChecked = true
                }
                if (workPosition2 != "") {
                    binding.tvPosition2.editText?.setText(workPosition2)
                }
                if (workCity2 != "") {
                    binding.tvCity.editText?.setText(workCity2)
                }
                if (workCountry2 != "") {
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
                val workResponsibilities3 =
                    CvDatabase(it).getExperienceDao().getWorkResponsibilities3()
                val workCity3 = CvDatabase(it).getExperienceDao().getWorkCity3()
                val workCountry3 = CvDatabase(it).getExperienceDao().getWorkCountry3()

                if (workCompany3 != "") {
                    binding.tvCompany3.editText?.setText(workCompany3)
                }
                if (workDateFrom3 != "") {
                    binding.tvDateFrom3.editText?.setText(workDateFrom3)
                }
                if (workDateTo3 != "") {
                    binding.tvDateTo3.editText?.setText(workDateTo3)
                }
                if (workPosition3 != "") {
                    binding.tvPosition3.editText?.setText(workPosition3)
                }
                if (workCity3 != "") {
                    binding.tvCity3.editText?.setText(workCity3)
                }
                if (workCountry3 != "") {
                    binding.tvCountry3.editText?.setText(workCountry3)
                }
            }
        }
    }

    private fun createChips(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroup1.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroup1.removeView(chip as View)
            }
        }
    }

    private fun entryChip2() {
        binding.tvResponsibilities2.setEndIconOnClickListener {
            binding.apply {
                val responsibility = etResponsibilities2.text.toString()
                createChips2(responsibility)
                if (s2.isNotEmpty())
                    s2 += ",$responsibility"
                else s2 += responsibility
                etResponsibilities2.text?.clear()
            }
        }
    }

    private fun createChips2(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroup2.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroup2.removeView(chip as View)
            }
        }
    }

    private fun entryChip3() {
        binding.tvResponsibilities3.setEndIconOnClickListener {
            binding.apply {
                val responsibility = etResponsibilities3.text.toString()
                createChips3(responsibility)
                if (s3.isNotEmpty())
                    s3 += ",$responsibility"
                else s3 += responsibility
                etResponsibilities3.text?.clear()
            }
        }
    }

    private fun createChips3(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroup3.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroup3.removeView(chip as View)
            }
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
    ) = FragmentWorkExperienceBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        return CvRepository(api)
    }
}