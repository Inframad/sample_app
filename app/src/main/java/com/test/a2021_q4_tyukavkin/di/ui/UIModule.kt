package com.test.a2021_q4_tyukavkin.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface UIModule {

    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UserAuthorizationFragmentViewModel::class)
    fun bindUserRegistrationViewModel(viewModel: UserAuthorizationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanRegistrationViewModel::class)
    fun bindLoanRegistrationFragmentViewModel(viewModel: LoanRegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanHistoryFragmentViewModel::class)
    fun bindLoanHistoryFragmentViewModel(viewModel: LoanHistoryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoanDetailsFragmentViewModel::class)
    fun bindLoanDetailsFragmentViewModel(viewModel: LoanDetailsFragmentViewModel): ViewModel
}