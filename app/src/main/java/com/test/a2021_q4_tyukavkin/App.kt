package com.test.a2021_q4_tyukavkin

import android.app.Application
import com.test.a2021_q4_tyukavkin.di.AppComponent
import com.test.a2021_q4_tyukavkin.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}