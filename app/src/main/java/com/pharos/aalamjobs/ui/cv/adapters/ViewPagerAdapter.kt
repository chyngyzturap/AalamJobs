package com.pharos.aalamjobs.ui.cv.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
    }


//     return   when(position){
//            0->{
//                PersonalInformationFragment()
//            }
//            1->{
//                ContactInformationFragment()
//            }
//            2->{
//                EducationFragment()
//            }
//         3-> {
//             LanguagesFragment()
//         }
//         4-> {
//             WorkExperienceFragment()
//         }
//         5-> {
//             JobRequirementFragment()
//         }
//
//            else->{
//                Fragment()
//            }
//
//        }

}