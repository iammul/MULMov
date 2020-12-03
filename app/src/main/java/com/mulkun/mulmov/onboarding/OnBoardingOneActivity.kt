package com.mulkun.mulmov.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mulkun.mulmov.R
import com.mulkun.mulmov.sign.signin.SignInActivity
import com.mulkun.mulmov.utils.Preferences
import kotlinx.android.synthetic.main.activity_on_boarding_one.*

class OnBoardingOneActivity : AppCompatActivity() {

    lateinit var preference:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_one)

        preference = Preferences(this)

        if (preference.getValues("onboarding").equals("1")){
            finishAffinity()
            var intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        btn_home.setOnClickListener{
            var intent = Intent(this@OnBoardingOneActivity, OnBoardingTwoActivity::class.java)
            startActivity(intent)
        }

        btn_daftar.setOnClickListener{

            preference.setValues("onboarding", "1")

            finishAffinity()
            var intent = Intent(this@OnBoardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}