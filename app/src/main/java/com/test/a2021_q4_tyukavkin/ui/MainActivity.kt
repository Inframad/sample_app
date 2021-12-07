package com.test.a2021_q4_tyukavkin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, RegistrationFragment())
            .commit()

        //TODO Internet connection checking
    }
}