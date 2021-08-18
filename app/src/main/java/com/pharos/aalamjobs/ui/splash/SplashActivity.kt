package com.pharos.aalamjobs.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.network.AuthApi
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.databinding.ActivitySplashBinding
import com.pharos.aalamjobs.ui.auth.AuthActivity
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.base.BaseActivity
import com.pharos.aalamjobs.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SplashActivity : BaseActivity<AuthViewModel, ActivitySplashBinding, AuthRepository>() {
    private lateinit var preferences: UserPreferences
    private lateinit var userPrefsViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showCurrent()

        tv_splash_noaccount.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_splash_log.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        lang_change.setOnClickListener {
            showChangeLang()
        }
    }

    private fun showCurrent() {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        if (token.isNullOrEmpty()) {
            setContentView(R.layout.activity_splash)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showChangeLang() {
        val langList = arrayOf("English", "Кыргызча", "Русский", "Türkçe")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(getString(R.string.choose_lang))
        mBuilder.setSingleChoiceItems(langList, -1) { dialog, which ->
            when (which) {
                0 -> {
                    lifecycleScope.launch {
                        setLocale("en")
                    }
                    recreate()
                }
                1 -> {
                    lifecycleScope.launch {
                        setLocale("ky")
                    }
                    recreate()
                }
                2 -> {
                    lifecycleScope.launch {
                        setLocale("ru")
                    }
                    recreate()
                }
                3 -> {
                    lifecycleScope.launch {
                        setLocale("tr")
                    }
                    recreate()
                }
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()

        mDialog.show()
    }

    @Suppress("DEPRECATION")
    private suspend fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
        lifecycleScope.launch {
            viewModel.saveLang(lang)
            Log.d("ololo", lang)
        }
        recreate()
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun getActivityRepository(): AuthRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(AuthApi::class.java, token)
        val api = remoteDataSource.buildApi(AuthApi::class.java, token)
        if (token.isNullOrEmpty()) {
            return AuthRepository(apiNoToken, userPreferences)
        } else {
            return AuthRepository(api, userPreferences)
        }
    }
}




