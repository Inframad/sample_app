package com.test.a2021_q4_tyukavkin.data.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource
@Inject constructor(
    context: Context,
    private val loanDao: LoanDao,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) {

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
    }

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val sharedPrefsFile: String = "TOKEN"
    private val sharedPref: SharedPreferences = EncryptedSharedPreferences.create(
        sharedPrefsFile,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveToken(value: String?) {
        withContext(Dispatchers.IO) {
            with(sharedPref.edit()) {
                putString(TOKEN_KEY, value)
                apply()
            }
        }
    }

    fun getToken(): String? =
        sharedPref.getString(TOKEN_KEY, null)

    fun deleteToken() {
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, null)
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