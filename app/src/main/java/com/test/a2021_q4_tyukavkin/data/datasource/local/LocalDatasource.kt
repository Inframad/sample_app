package com.test.a2021_q4_tyukavkin.data.datasource.local

import android.content.Context
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource
@Inject constructor(
    context: Context,
    private val loanDao: LoanDao,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) {

    companion object {
        private const val DATA = "DATA"
    }

    private val sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String?) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String? =
        sharedPref.getString(key, null)

    fun deleteString(key: String) {
        with(sharedPref.edit()) {
            putString(key, null)
            apply()
        }
    }

    suspend fun insertLoans(loans: List<LoanDTO>) =
        withContext(dispatchersIO) {
            loanDao.insertAll(loans)
        }

    fun getAllLoans() =
        loanDao.getAll()

    suspend fun deleteAllLoans() {
        withContext(dispatchersIO) {
            loanDao.deleteAll()
        }
    }

}