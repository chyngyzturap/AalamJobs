package com.pharos.aalamjobs.ui.cv.jobapplicationform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.Achievements
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentAchievementsBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import com.pharos.aalamjobs.ui.cv.CvViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AchievementsFragment : BaseFragment<CvViewModel, FragmentAchievementsBinding, CvRepository>() {
    private var s = ""
    private var s2 = ""
    private var s3 = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entryChipAch()
        entryChipLinks()
        entryChipSkills()

        binding.linksBtnNext.setOnClickListener{
            val achievementsAll = Achievements(s3, s2, s)

            launch {
                context?.let {
                    CvDatabase(it).getExperienceDao().insertLink(achievementsAll)
                }
            }
            findNavController().navigate(R.id.action_linksFragment_to_jobRequirementFragment)
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

    private fun entryChipSkills() {
        binding.tvSkills.setEndIconOnClickListener {
                binding.apply {
                    val responsibility = binding.etSkills.text.toString()
                    createChipsSkills(responsibility)
                    if (s.isNotEmpty())
                        s += ",$responsibility"
                    else  s+= responsibility
                    binding.etSkills.text?.clear()
                }
        }
    }

    private fun createChipsSkills(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background)
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroupSkills.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroupSkills.removeView(chip as View)
            }
        }
    }
    private fun entryChipAch() {
        binding.tvAchievements.setEndIconOnClickListener {
                binding.apply {
                    val responsibility = etAchievements.text.toString()
                    createChipsAch(responsibility)
                    if (s2.isNotEmpty())
                        s2 += ",$responsibility"
                    else s2+= responsibility
                    etAchievements.text?.clear()
                }
        }
    }

    private fun createChipsAch(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background)
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroupAchievements.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroupAchievements.removeView(chip as View)
            }
        }
    }
    private fun entryChipLinks() {
        binding.tvLinks.setEndIconOnClickListener {
                binding.apply {
                    val responsibility = etLinks.text.toString()
                    createChipsLinks(responsibility)
                    if (s3.isNotEmpty())
                        s3 += ",$responsibility"
                    else s3+= responsibility
                    etLinks.text?.clear()
                }
        }
    }

    private fun createChipsLinks(responsibility: String) {
        val chip = Chip(requireContext())
        chip.apply {
            text = responsibility
            chipIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_launcher_background)
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            binding.chipGroupLinks.addView(chip as View)
            setOnCloseIconClickListener {
                binding.chipGroupLinks.removeView(chip as View)
            }
        }
    }

    override fun getViewModel()= CvViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAchievementsBinding.inflate(inflater,container, false)

    override fun getFragmentRepository() : CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }

        val api = remoteDataSource.buildApi(CvApi::class.java, token)
        return CvRepository(api)
    }
}