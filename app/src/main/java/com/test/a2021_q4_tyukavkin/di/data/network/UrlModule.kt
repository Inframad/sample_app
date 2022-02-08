package com.test.a2021_q4_tyukavkin.di.data.network

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UrlModule {

    companion object {
        //const val BASE_URL = "https://focusstart.appspot.com/"
        const val BASE_URL = "https://mybackapi.herokuapp.com/api/v1/cashloans/"
    }

    @BaseUrl
    @Provides
    @Singleton
    fun getBaseUrl(): String = BASE_URL
}