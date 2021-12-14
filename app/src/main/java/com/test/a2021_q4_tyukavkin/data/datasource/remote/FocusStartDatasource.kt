package com.test.a2021_q4_tyukavkin.data.datasource.remote

import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusStartDatasource
@Inject constructor(
    private val focusStartLoanApi: FocusStartLoanApi,
    private val localDatasource: LocalDatasource,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) {

    private var token: String? = localDatasource.getString("TOKEN") //TODO Const

    suspend fun register(auth: Auth): User =
        withContext(dispatchersIO) {
            focusStartLoanApi.register(auth).toUser()
        }

    suspend fun login(auth: Auth) {
        withContext(dispatchersIO) {
            val deferredToken = async { focusStartLoanApi.login(auth) }
            deferredToken.await().apply {
                token = this  //TODO Потокобезопасность
                localDatasource.saveString(token) //TODO Небезопасно, использовать Android Keystore
            }
        }
    }


    suspend fun getLoanConditions() =
        withContext(dispatchersIO) {
            focusStartLoanApi.getLoanConditions(token!!).toLoanConditions() //TODO
        }

    suspend fun createLoan(loanRequest: LoanRequest): Loan =
        withContext(dispatchersIO) {
            focusStartLoanApi.createLoan(token!!, loanRequest).toLoan()
        }

    suspend fun getAllLoans() =
        withContext(dispatchersIO) {
            focusStartLoanApi.getAllLoans(token!!)
        }

    suspend fun getLoanData(id: Long): Loan =
        focusStartLoanApi.getLoanData(token!!, id).toLoan()

}