package com.test.a2021_q4_tyukavkin.data.datasource.remote

import android.util.Log
import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import com.test.a2021_q4_tyukavkin.domain.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusStartDatasource //TODO Naming
@Inject constructor(
    private val focusStartLoanApi: FocusStartLoanApi
) {

    private lateinit var token: String

    suspend fun register(auth: Auth): User =
        withContext(Dispatchers.IO) { //TODO Провайдить dispatcher
            focusStartLoanApi.register(auth).toUser() //TODO Обработка ошибок
        }

    suspend fun login(auth: Auth): String =
        withContext(Dispatchers.IO) {
            val deferredToken = async { focusStartLoanApi.login(auth) } //TODO Потокобезопасность
            token = deferredToken.await()
            "OK"
        }


    suspend fun getLoanConditions() =
        withContext(Dispatchers.IO) {
            focusStartLoanApi.getLoanConditions(token).toLoanConditions()
        }

    suspend fun createLoan(loanRequest: LoanRequest) =
        withContext(Dispatchers.IO) {
            Log.i("ServerResponse", "Creating loan")
            focusStartLoanApi.createLoan(token, loanRequest).toLoan().also {
                Log.i("ServerResponse", it.toString())
            }
            /* Loan(
                 amount = 1F,
                 date = "123",
                 firstName = "Ivan",
                 id = 123,
                 lastName = "Ivanov",
                 percent = 20.0,
                 period = 100,
                 phoneNumber = "123",
                 state = LoanState.REGISTERED
             )*/
        }

    suspend fun getAllLoans() =
        withContext(Dispatchers.IO) {
            focusStartLoanApi.getAllLoans(token).map { it.toLoan() }
        }

}