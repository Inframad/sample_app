package com.test.a2021_q4_tyukavkin

import com.test.a2021_q4_tyukavkin.di.DaggerMockAppComponent
import com.test.a2021_q4_tyukavkin.di.MockAppComponent

class TestApp: App() {

    override fun initializeComponent(): MockAppComponent {
        return DaggerMockAppComponent.factory().create(applicationContext)
    }
}