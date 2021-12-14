package com.test.a2021_q4_tyukavkin.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class AppModule {

    @Provides
    @DispatchersIO
    fun provideDispatchersIO(): CoroutineDispatcher =
        Dispatchers.IO

    @Provides
    @DispatchersDefault
    fun provideDispatchersDefault(): CoroutineDispatcher =
        Dispatchers.Default
}