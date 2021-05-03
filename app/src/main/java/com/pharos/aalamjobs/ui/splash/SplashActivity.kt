package com.pharos.aalamjobs.ui.splash

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.startNewActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadLocate()

        tv_splash_noaccount.setOnClickListener {
            startNewActivity(MainActivity::class.java)
        }

        btn_splash_log.setOnClickListener {
            startNewActivity(AuthActivity::class.java)
        }

        btn_splash_reg.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentAuth) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.fragmentAuth)
        }

        iv_lang.setOnClickListener {
            showChangeLang()
        }
        }

    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(langList, -1) { dialog, which ->
            if (which == 0) {
                iv_lang.setImageResource(R.drawable.lang_english_flag)
                recreate()
            } else if (which == 1) {
                iv_lang.setImageResource(R.drawable.lang_kyrgyz_flag)
                recreate()
            } else if (which == 2) {
                iv_lang.setImageResource(R.drawable.lang_russian_flag)
                recreate()
            } else if (which == 3) {
                iv_lang.setImageResource(R.drawable.lang_turkish_flag)
                recreate()
            }

            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()
    }

    @Suppress("DEPRECATION")
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }

}
