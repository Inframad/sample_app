package com.test.a2021_q4_tyukavkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.a2021_q4_tyukavkin.databinding.ActivityMainBinding
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var registrationUsecase: RegistrationUsecase

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}