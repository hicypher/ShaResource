package com.hacktech19

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.AnimationUtils
import android.widget.*

class MainActivity_splash_screen : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val imageViewSplash = findViewById(R.id.logo) as ImageView;
        val txtAppName = findViewById(R.id.name) as LinearLayout;


        val bounce_interpolator = AnimationUtils.loadAnimation(this, R.anim.bounce_interpolator)
        val bounce_interpolator1 = AnimationUtils.loadAnimation(this, R.anim.translate)
        imageViewSplash.startAnimation(bounce_interpolator)

        txtAppName.startAnimation(bounce_interpolator1)

        var usernamea=sPref.getString("UserID","")



        object : CountDownTimer(3000,1000) {
            override fun onFinish() {
                if(usernamea=="") {
                     intent = Intent(applicationContext, login::class.java)

                     startActivity(intent)
                     finish()
                 }
                else{
                    intent = Intent(applicationContext, activity_addnewitem::class.java)

                    startActivity(intent)
                    finish()
                }

            }

            override fun onTick(p0: Long) {
                 //To change body of created functions use File | Settings | File Templates.
            }


        }.start()



        }
    }

