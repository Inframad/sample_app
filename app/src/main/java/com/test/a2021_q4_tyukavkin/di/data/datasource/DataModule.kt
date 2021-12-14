package com.test.a2021_q4_tyukavkin.di.data.datasource

import android.content.Context
import androidx.room.Room
import com.test.a2021_q4_tyukavkin.data.datasource.local.AppDatabase
import com.test.a2021_q4_tyukavkin.data.datasource.local.LoanDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLoanDao(database: AppDatabase): LoanDao =
        database.loanDao()

}
