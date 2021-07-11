package com.pharos.aalamjobs.ui.cv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pharos.aalamjobs.ui.cv.*

class CvFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = arrayListOf(
        PersonalInformationFragment(),
        ContactInformationFragment(),
        EducationFragment(),
        LanguagesFragment(),
        WorkExperienceFragment(),
        AchievementsFragment(),
        JobRequirementFragment()
    )


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}