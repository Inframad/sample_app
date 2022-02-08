package com.test.a2021_q4_tyukavkin.data.repository

import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.datasource.remote.RemoteDatasource
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) :
    Repository {

    override fun checkAuthorization(): Boolean =
        !localDatasource.getToken().isNullOrBlank()

    override suspend fun register(auth: Auth): User =
        remoteDatasource.register(auth).toUser()

    override suspend fun login(auth: Auth) {
        withContext(dispatchersIO) {
            val token = remoteDatasource.login(auth)
            localDatasource.saveToken(token.value)
        }
    }

    override suspend fun logout() {
        localDatasource.deleteAllLoans()
        localDatasource.deleteToken()
    }

    override suspend fun createLoan(loanRequest: LoanRequest): Loan =
        remoteDatasource.createLoan(loanRequest).toLoan()

    override suspend fun getLoanData(id: Long): Loan =
        remoteDatasource.getLoanData(id).toLoan()

    override fun getAllLoans(): Flow<List<Loan>> =
        localDatasource.getAllLoans().map {
            it.map { loanDto -> loanDto.toLoan() }
        }

    override suspend fun updateLoansList(): Boolean {
        val loans = remoteDatasource.getAllLoans()
        localDatasource.insertLoans(loans)
        return loans.isEmpty()
    }

    override suspend fun getLoanConditions(): LoanConditions =
        remoteDatasource.getLoanConditions().toLoanConditions()
}