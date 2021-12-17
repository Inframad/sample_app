package com.test.a2021_q4_tyukavkin.data.repository

import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.datasource.remote.FocusStartDatasource
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val focusStartDatasource: FocusStartDatasource,
    private val localDatasource: LocalDatasource,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) :
    Repository {

    private var token = localDatasource.getString("TOKEN")

    override suspend fun checkAuthorization(): Boolean =
        !token.isNullOrBlank()

    override suspend fun register(auth: Auth): User =
        focusStartDatasource.register(auth).toUser()

    override suspend fun login(auth: Auth) {
        withContext(dispatchersIO) {
            val deferredToken = async { focusStartDatasource.login(auth) }
            deferredToken.await().apply {
                token = this
                localDatasource.saveString(this) //TODO Небезопасно, использовать Android Keystore
            }
        }
    }

    override suspend fun createLoan(loanRequest: LoanRequest): Loan =
        focusStartDatasource.createLoan(loanRequest, token!!).toLoan()

    override suspend fun getLoanData(id: Long): Loan =
        focusStartDatasource.getLoanData(id, token!!).toLoan()

    override fun getAllLoans(): Flow<List<Loan>> =
        localDatasource.getAllLoans().map {
            it.map { loanDto -> loanDto.toLoan() }
        }

    override suspend fun updateLoansList(): Boolean {
        val loans = focusStartDatasource.getAllLoans(token!!)
        localDatasource.insertLoans(loans)
        return loans.isEmpty()
    }

    override suspend fun getLoanConditions(): LoanConditions =
        focusStartDatasource.getLoanConditions(token!!)
}