package com.pharos.aalamjobs.ui.cv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.startNewActivity
import kotlinx.android.synthetic.main.activity_cv.*


class CvActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
        val navController: NavController = navHostFragment.navController

//        setupTabLayout()

        val arrayOfNav = arrayOf(
    R.id.personalInformationFragment,
    R.id.contactInformationFragment,
    R.id.educationFragment,
    R.id.languagesFragment,
    R.id.workExperienceFragment,
    R.id.jobRequirementFragment
)


//        var viewPager2: ViewPager2 = findViewById(R.id.view_pager)
//        var tabLayout: TabLayout = findViewById(R.id.tab_layout)


//        btn_personal.setOnClickListener {
//            navController.navigate(R.id.personalInformationFragment)
//        }
//
//        btn_contacts.setOnClickListener {
//            navController.navigate(R.id.contactInformationFragment)
//        }
//
//        btn_education.setOnClickListener {
//            navController.navigate(R.id.educationFragment)
//        }
//
//        btn_language.setOnClickListener {
//            navController.navigate(R.id.languagesFragment)
//        }
//
//        btn_experience.setOnClickListener {
//            navController.navigate(R.id.workExperienceFragment)
//        }
//
//        btn_requirements.setOnClickListener {
//            navController.navigate(R.id.jobRequirementFragment)
//        }



//        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)
//
//        viewPager2.adapter=adapter
//
//        TabLayoutMediator(tabLayout,viewPager2){tab,position->
//            when(position){
//                0->{
//                    tab.text=  resources.getString(R.string.tab_personals)
//                    navController.navigate(R.id.personalInformationFragment)
//                }
//                1->{
//                    tab.text=  resources.getString(R.string.tab_contacts)
//                    navController.navigate(R.id.contactInformationFragment)
//
//                }
//                2->{
//                    tab.text=  resources.getString(R.string.tab_education)
//                    navController.navigate(R.id.educationFragment)
//                }
//                3-> {tab.text =   resources.getString(R.string.tab_language)
//                    navController.navigate(R.id.languagesFragment)
//                }
//                4->{tab.text =   resources.getString(R.string.tab_experience)
//                    navController.navigate(R.id.workExperienceFragment)
//                }
//                5->{ tab.text =  resources.getString(R.string.tab_job_requirements)
//                    navController.navigate(R.id.jobRequirementFragment)
//                }
//            }
//        }.attach()
//
//        initViewPager()

        iv_backpressed.setOnClickListener {
            onBackPressed()
        }
        auth_toolbar_title.setOnClickListener{
           startNewActivity(MainActivity::class.java)
        }



    }

//    @Suppress("DEPRECATION")
//    private fun setupTabLayout() {
//        val mTabLayout = findViewById<View>(R.id.tabs) as TabLayout
////        val mTabs = tabs.getTabAt(0)
////        mTabs?.select()
//
//        mTabLayout.setOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                onTabTapped(tab.position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {
//                onTabTapped(tab.position)
//            }
//        })
//    }

    private fun onTabTapped(position: Int) {
        when (position) {
            0->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.personalInformationFragment)
            }
            1->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.contactInformationFragment)

            }
            2->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.educationFragment)
            }
            3-> {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.languagesFragment)
            }
            4->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.workExperienceFragment)
            }
            5->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.linksFragment)
            }
            6->{
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentCV) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(R.id.jobRequirementFragment)
            }
        }
    }

//    private fun initViewPager() {
//        var viewPager2: ViewPager2 = findViewById(R.id.view_pager)
//        var adapter = CvFragmentAdapter(supportFragmentManager, lifecycle)
//        viewPager2.adapter = adapter
//
//        // If you want to scroll the ViewPager Vertical uncomment the next line:
//        // viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
//
//        var tablayout: TabLayout = findViewById(R.id.tab_layout)
//        var names:Array<String> = arrayOf(
//            "Personal",
//            "Contacts",
//            "Education",
//            "Languages",
//            "Experience",
//            "Requirements"
//        )
//        TabLayoutMediator(tablayout, viewPager2){ tab, position ->
//            tab.text = names[position]
//        }.attach()
//    }
}