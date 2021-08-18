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

        iv_backpressed.setOnClickListener {
            onBackPressed()
        }
        auth_toolbar_title.setOnClickListener{
           startNewActivity(MainActivity::class.java)
        }
    }
}