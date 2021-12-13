package com.test.a2021_q4_tyukavkin.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO

@Database(entities = [LoanDTO::class], version = 1) //TODO version
abstract class AppDatabase : RoomDatabase() {

    abstract fun loanDao(): LoanDao
}