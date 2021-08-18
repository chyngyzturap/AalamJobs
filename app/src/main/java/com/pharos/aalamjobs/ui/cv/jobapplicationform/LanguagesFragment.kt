package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.MotherLanguage
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages2
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages3
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentLanguagesBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LanguagesFragment : BaseFragment<CvViewModel, FragmentLanguagesBinding, CvRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

  setFromDb()

        binding.tvOther.setEndIconOnClickListener {
            toSecondOthLang()
        }

        binding.tvOther2.setEndIconOnClickListener {
            toThirdOthLang()
        }

        binding.langBtnNext.setOnClickListener {
            saveWithNextBtn()
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
        val otherLang = binding.etOther.text.toString().trim()
        val otherLangTalking = binding.langSpTalking.selectedItem.toString()
        val otherLangListening = binding.langSpListening.selectedItem.toString()
        val otherLangReading = binding.langSpReading.selectedItem.toString()
        val otherLangWriting = binding.langSpWriting.selectedItem.toString()
        val otherLanguage = OtherLanguages(otherLang, otherLangListening, otherLangReading,
            otherLangWriting, otherLangTalking)

        val otherLang2 = binding.etOther2.text.toString().trim()
        val otherLangTalking2 = binding.langSpTalking2.selectedItem.toString()
        val otherLangListening2 = binding.langSpListening2.selectedItem.toString()
        val otherLangReading2 = binding.langSpReading2.selectedItem.toString()
        val otherLangWriting2 = binding.langSpWriting2.selectedItem.toString()
        val otherLanguage2 = OtherLanguages2(otherLang2, otherLangListening2, otherLangReading2,
            otherLangWriting2, otherLangTalking2)

        val nativeLang = binding.etNative.text.toString().trim()
        val otherLang3 = binding.etOther3.text.toString().trim()
        val otherLangTalking3 = binding.langSpTalking3.selectedItem.toString()
        val otherLangListening3 = binding.langSpListening3.selectedItem.toString()
        val otherLangReading3 = binding.langSpReading3.selectedItem.toString()
        val otherLangWriting3 = binding.langSpWriting3.selectedItem.toString()

        val otherLanguage3 = OtherLanguages3(otherLang3, otherLangListening3, otherLangReading3,
            otherLangWriting3, otherLangTalking3
        )

        val motherLang = MotherLanguage(nativeLang)
        if (nativeLang != "") {

            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$nativeLang ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getLangDao().insertMotherLang(motherLang)
                    findNavController().navigate(R.id.workExperienceFragment)

                }
            }
        } else {

            if (nativeLang != "") {
                binding.tvNative.error = null
            } else {
                binding.tvNative.error = R.string.txt_required.toString()
            }
        }
        if (otherLang != "") {
            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$nativeLang, $otherLang ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getLangDao().insertOtherLang(otherLanguage)
                    findNavController().navigate(R.id.workExperienceFragment)
                }
            }
        }
        if (otherLang2 != "") {
            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$nativeLang, $otherLang, $otherLang2 ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getLangDao().insertOtherLang2(otherLanguage2)
                    findNavController().navigate(R.id.workExperienceFragment)
                }
            }
        }
        if (otherLang3 != "") {
            launch {
                context?.let {
                    Toast.makeText(requireContext(), "$nativeLang, $otherLang, $otherLang3 ✔", Toast.LENGTH_SHORT).show()
                    CvDatabase(it).getLangDao().insertOtherLang3(otherLanguage3)
                    findNavController().navigate(R.id.languagesFragment)
                }
            }
        }
    }

    private fun toThirdOthLang() {
        val otherLang2 = binding.etOther2.text.toString().trim()
        val otherLangTalking2 = binding.langSpTalking2.selectedItem.toString()
        val otherLangListening2 = binding.langSpListening2.selectedItem.toString()
        val otherLangReading2 = binding.langSpReading2.selectedItem.toString()
        val otherLangWriting2 = binding.langSpWriting2.selectedItem.toString()

        val otherLanguage2 = OtherLanguages2(otherLang2, otherLangListening2, otherLangReading2,
            otherLangWriting2, otherLangTalking2)

        launch {
            context?.let {
                Toast.makeText(requireContext(), "$otherLang2 ✔", Toast.LENGTH_SHORT).show()
                CvDatabase(it).getLangDao().insertOtherLang2(otherLanguage2)
            }
        }
        binding.otherLangContainer.visible(false)
        binding.otherLangContainer2.visible(false)
        binding.otherLangContainer3.visible(true)
    }

    private fun toSecondOthLang() {
        val otherLang = binding.etOther.text.toString().trim()
        val otherLangTalking = binding.langSpTalking.selectedItem.toString()
        val otherLangListening = binding.langSpListening.selectedItem.toString()
        val otherLangReading = binding.langSpReading.selectedItem.toString()
        val otherLangWriting = binding.langSpWriting.selectedItem.toString()

        val otherLanguage = OtherLanguages(otherLang, otherLangListening, otherLangReading,
            otherLangWriting, otherLangTalking)

        launch {
            context?.let {
                Toast.makeText(requireContext(), "$otherLang ✔", Toast.LENGTH_SHORT).show()
                CvDatabase(it).getLangDao().insertOtherLang(otherLanguage)

            }
        }
        binding.otherLangContainer.visible(false)
        binding.otherLangContainer2.visible(true)
    }

    private fun setFromDb(){
        launch {
            context?.let {
                val motherLanguage = CvDatabase(it).getLangDao().getMotherLanguage()
                if (motherLanguage!= ""){
                    binding.tvNative.editText?.setText(motherLanguage)
                }
            }
        }
    }

    override fun getViewModel() = CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLanguagesBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): CvRepository {
        val token = runBlocking {userPreferences.tokenAccess.first() }
        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        return CvRepository(api)
    }

}

