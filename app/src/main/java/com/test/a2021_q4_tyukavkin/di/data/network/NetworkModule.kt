package com.test.a2021_q4_tyukavkin.di.data.network

import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        const val BASE_URL = "http://focusstart.appspot.com/"
    }

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideScalarsConverterFactory(): ScalarsConverterFactory =
        ScalarsConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://focusstart.appspot.com/") //TODO Временно
            .client(client)
            .addConverterFactory(scalarsConverterFactory) //TODO Фабрики конфликтуют
            .addConverterFactory(moshiConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideFocusStartLoanApi( //TODO Naming
        retrofit: Retrofit,
    ): FocusStartLoanApi =
        retrofit.create(FocusStartLoanApi::class.java)
}