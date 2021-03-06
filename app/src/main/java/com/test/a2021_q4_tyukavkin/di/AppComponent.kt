package com.test.a2021_q4_tyukavkin.di

import android.content.Context
import com.test.a2021_q4_tyukavkin.di.data.datasource.DataModule
import com.test.a2021_q4_tyukavkin.di.data.network.NetworkModule
import com.test.a2021_q4_tyukavkin.di.data.network.UrlModule
import com.test.a2021_q4_tyukavkin.di.domain.DomainModule
import com.test.a2021_q4_tyukavkin.di.ui.UIModule
import com.test.a2021_q4_tyukavkin.ui.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        UrlModule::class,
        DomainModule::class,
        UIModule::class,
        DataModule::class
    ]
)
interface AppComponent {

    @Singleton
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: UserAuthorizationFragment)
    fun inject(fragment: LoanDetailsFragment)
    fun inject(fragment: LoanSuccessfullyCreatedFragment)
    fun inject(fragment: LoanConditionsFragment)
    fun inject(fragment: LoanRegistrationFragment)
    fun inject(fragment: LoansHistoryFragment)
}