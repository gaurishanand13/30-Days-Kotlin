package com.ho.clinic.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ho.clinic.Login
import com.ho.clinic.MainActivity
import com.ho.clinic.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class splashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({
            val settings = getSharedPreferences("User_Data", 0)
            val userData = settings.getString("User_email", null)
            val token = settings.getString("User_token", null)
            if (token == null) {
                splashScreenProgressbar.setVisibility(View.GONE)
                val i = Intent(this, Login::class.java)
                startActivity(i)
                // close this activity
                finish()
            } else {
                splashScreenProgressbar.setVisibility(View.GONE)
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                // close this activity
                finish()
            }
        }, 3000)

    }
}
