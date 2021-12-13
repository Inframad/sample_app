package com.test.a2021_q4_tyukavkin.data.repository

import com.test.a2021_q4_tyukavkin.data.converter.toLoan
import com.test.a2021_q4_tyukavkin.data.datasource.local.LoanDao
import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import com.test.a2021_q4_tyukavkin.data.datasource.remote.FocusStartDatasource
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val focusStartDatasource: FocusStartDatasource,
    private val localDatasource: LocalDatasource,
    private val loanDao: LoanDao
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

    override suspend fun getLoanData(id: Long): Loan =
        focusStartDatasource.getLoanData(id)

    override fun getAllLoans(): Flow<List<Loan>> =
            loanDao.getAll().map {
                it.map { loanDto -> loanDto.toLoan() }
            }

    override suspend fun updateLoansList(): Boolean =
        withContext(Dispatchers.IO) {
            val loans = focusStartDatasource.getAllLoans()
            loanDao.insertAll(loans)
            loans.isEmpty()
        }

    override suspend fun getLoanConditions(): LoanConditions =
        focusStartDatasource.getLoanConditions()
}