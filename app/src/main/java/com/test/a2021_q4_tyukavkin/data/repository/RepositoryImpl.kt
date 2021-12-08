package com.test.a2021_q4_tyukavkin.data.repository

import android.util.Log
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.datasource.remote.FocusStartDatasource
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl
@Inject constructor(
    private val focusStartDatasource: FocusStartDatasource,
    private val localDatasource: LocalDatasource
) :
    Repository {

    override suspend fun checkAuthorization(): Boolean =
        !localDatasource.getString("TOKEN").isNullOrBlank()

    override suspend fun register(auth: Auth): User =
        focusStartDatasource.register(auth)

    override suspend fun login(auth: Auth): String =
        focusStartDatasource.login(auth)

    override suspend fun createLoan(loanRequest: LoanRequest): Loan =
        focusStartDatasource.createLoan(loanRequest)

    override fun getLoanData(id: Long): Loan {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLoans(): List<Loan> =
        focusStartDatasource.getAllLoans()

    override suspend fun getLoanConditions(): LoanConditions =
        focusStartDatasource.getLoanConditions()
}