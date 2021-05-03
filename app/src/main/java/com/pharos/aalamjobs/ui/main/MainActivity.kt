package com.pharos.aalamjobs.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pharos.aalamjobs.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        nav_bottom.setupWithNavController(navController)

        when (nav_bottom.id) {
            R.id.nav_jobs -> (R.drawable.ic_menu_jobs_selected)
            R.id.nav_favorites -> (R.drawable.ic_menu_favorites_selected)
            R.id.nav_applied -> (R.drawable.ic_menu_applied_selected)
            R.id.nav_resume -> (R.drawable.ic_menu_resume_selected)
            R.id.nav_profile -> (R.drawable.ic_menu_profile_selected)
            }
        }
    }
