package com.test.a2021_q4_tyukavkin.di

import android.content.Context
import com.test.a2021_q4_tyukavkin.di.data.datasource.DataModule
import com.test.a2021_q4_tyukavkin.di.data.network.MockUrlModule
import com.test.a2021_q4_tyukavkin.di.data.network.NetworkModule
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
        MockUrlModule::class,
        DomainModule::class,
        UIModule::class,
        DataModule::class
    ]
)
interface MockAppComponent: AppComponent {

    @Singleton
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MockAppComponent
    }

    override fun inject(activity: MainActivity)
    override fun inject(fragment: UserAuthorizationFragment)
    override fun inject(fragment: LoanDetailsFragment)
    override fun inject(fragment: LoanSuccessfullyCreatedFragment)
    override fun inject(fragment: LoanConditionsFragment)
    override fun inject(fragment: LoanRegistrationFragment)
    override fun inject(fragment: LoansHistoryFragment)
}