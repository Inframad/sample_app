package com.test.a2021_q4_tyukavkin.data.datasource.remote

import android.util.Log
import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import com.test.a2021_q4_tyukavkin.domain.entity.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton
import javax.security.auth.login.LoginException

@Singleton
class FocusStartDatasource //TODO Naming
@Inject constructor(
    private val focusStartLoanApi: FocusStartLoanApi,
    private val localDatasource: LocalDatasource
) {

    private var token: String? = localDatasource.getString("TOKEN") //TODO Const

    suspend fun register(auth: Auth): User =
        withContext(Dispatchers.IO) { //TODO Провайдить dispatcher
            focusStartLoanApi.register(auth).toUser() //TODO Обработка ошибок
        }

    suspend fun login(auth: Auth): String =
        withContext(Dispatchers.IO) {
            val deferredToken = async { focusStartLoanApi.login(auth) } //TODO Потокобезопасность
            deferredToken.await().apply {
                Log.i("MyToken", localDatasource.getString("TOKEN") ?: "Пусто :(")
                token = this
                localDatasource.saveString(token) //TODO Небезопасно, использовать Android Keystore
            }

            "OK"
        }


    suspend fun getLoanConditions() =
        withContext(Dispatchers.IO) {
            focusStartLoanApi.getLoanConditions(token!!).toLoanConditions()
        }

    suspend fun createLoan(loanRequest: LoanRequest) =
        withContext(Dispatchers.IO) {
            Log.i("ServerResponse", "Creating loan")
            focusStartLoanApi.createLoan(token!!, loanRequest).toLoan().also {
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
            focusStartLoanApi.getAllLoans(token!!).map { it.toLoan() }
        }

}