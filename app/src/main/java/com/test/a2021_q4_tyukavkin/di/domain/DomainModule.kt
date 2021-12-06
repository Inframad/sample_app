package com.test.a2021_q4_tyukavkin.di.domain

import com.test.a2021_q4_tyukavkin.data.repository.RepositoryImpl
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun provideRepository(repositoryImpl: RepositoryImpl): Repository
}