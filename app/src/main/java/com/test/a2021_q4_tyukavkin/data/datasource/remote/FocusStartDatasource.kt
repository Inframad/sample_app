package com.test.a2021_q4_tyukavkin.data.datasource.remote

import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusStartDatasource
@Inject constructor(
    private val focusStartLoanApi: FocusStartLoanApi,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) {
    suspend fun register(auth: Auth): UserDTO =
        withContext(dispatchersIO) {
            focusStartLoanApi.register(auth)
        }

    suspend fun login(auth: Auth): String =
        withContext(dispatchersIO) {
            focusStartLoanApi.login(auth)
        }

    suspend fun getLoanConditions(token: String) =
        withContext(dispatchersIO) {
            focusStartLoanApi.getLoanConditions(token).toLoanConditions() //TODO
        }

    suspend fun createLoan(loanRequest: LoanRequest, token: String): LoanDTO =
        withContext(dispatchersIO) {
            focusStartLoanApi.createLoan(token, loanRequest)
        }

    suspend fun getAllLoans(token: String) =
        withContext(dispatchersIO) {
            focusStartLoanApi.getAllLoans(token)
        }

    suspend fun getLoanData(id: Long, token: String): LoanDTO =
        focusStartLoanApi.getLoanData(token, id)

}