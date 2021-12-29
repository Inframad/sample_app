package com.test.a2021_q4_tyukavkin.di.data.network

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockUrlModule {

    companion object {
        const val BASE_URL = "http://127.0.0.1:8080"
    }

    @BaseUrl
    @Provides
    @Singleton
    fun getBaseUrl(): String = BASE_URL
}