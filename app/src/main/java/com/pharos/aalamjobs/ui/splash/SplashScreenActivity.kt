package com.pharos.aalamjobs.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        iv_splash_upvector.alpha = 0f
        iv_splash_upvector.animate().setDuration(1500).alpha(1f).withEndAction{
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        iv_splash_downvector.alpha = 0f
        iv_splash_downvector.animate().setDuration(1500).alpha(1f).withEndAction{
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        iv_splash_logo.alpha = 0f
        iv_splash_logo.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this, SplashActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}