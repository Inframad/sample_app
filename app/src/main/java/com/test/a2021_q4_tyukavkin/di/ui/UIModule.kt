package com.test.a2021_q4_tyukavkin.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.a2021_q4_tyukavkin.presentation.RegistrationFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface UIModule {

    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(RegistrationFragmentViewModel::class)
    fun bindViewModel(viewModel: RegistrationFragmentViewModel): ViewModel
}