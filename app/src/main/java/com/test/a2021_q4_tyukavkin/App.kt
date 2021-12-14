package com.test.a2021_q4_tyukavkin

import android.app.Application
import android.content.Context
import com.test.a2021_q4_tyukavkin.di.AppComponent
import com.test.a2021_q4_tyukavkin.di.DaggerAppComponent
import com.test.a2021_q4_tyukavkin.presentation.LocaleChanger

class App: Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleChanger.wrapContext(base))
    }
}