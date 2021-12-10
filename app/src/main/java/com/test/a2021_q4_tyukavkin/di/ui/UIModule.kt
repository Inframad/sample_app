package com.test.a2021_q4_tyukavkin.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.presentation.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface UIModule {

    @Binds
    @Singleton
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton //TODO Отдельные аннотации
    @ViewModelKey(UserAuthorizationFragmentViewModel::class)
    fun bindUserRegistrationViewModel(viewModel: UserAuthorizationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LoanRegistrationViewModel::class)
    fun bindLoanRegistrationFragmentViewModel(viewModel: LoanRegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LoanHistoryFragmentViewModel::class)
    fun bindLoanHistoryFragmentViewModel(viewModel: LoanHistoryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanDetailsFragmentViewModel::class)
    fun bindLoanDetailsFragmentViewModel(viewModel: LoanDetailsFragmentViewModel): ViewModel
}