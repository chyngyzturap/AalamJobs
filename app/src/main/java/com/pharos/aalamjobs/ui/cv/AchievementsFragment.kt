package com.pharos.aalamjobs.ui.cv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.dao.PersonalInfoDao
import com.pharos.aalamjobs.data.local.db.cv.database.CvDatabase
import com.pharos.aalamjobs.data.local.db.cv.entities.Achievements
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.FragmentAchievementsBinding
import com.pharos.aalamjobs.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AchievementsFragment : BaseFragment<CvViewModel, FragmentAchievementsBinding, CvRepository>() {

    private val dao: PersonalInfoDao? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linksBtnNext.setOnClickListener{

            val link = binding.etLinks.text.toString().trim()
            val achievements = binding.etAchievements.text.toString().trim()
            val skill = binding.etSkills.text.toString().trim()

            val achievementsAll = Achievements(link, achievements, skill)



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