package com.test.a2021_q4_tyukavkin.di

import android.content.Context
import com.test.a2021_q4_tyukavkin.MainActivity
import com.test.a2021_q4_tyukavkin.di.data.network.NetworkModule
import com.test.a2021_q4_tyukavkin.di.domain.DomainModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        DomainModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}