package com.pharos.aalamjobs.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pharos.aalamjobs.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        iv_backpressed.setOnClickListener {
            onBackPressed()
        }
    }

}