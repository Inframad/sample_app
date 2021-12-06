package com.test.a2021_q4_tyukavkin.data.repository

import com.test.a2021_q4_tyukavkin.data.datasource.remote.FocusStartDatasource
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val focusStartDatasource: FocusStartDatasource): Repository {

    override suspend fun register(auth: Auth): User =
        focusStartDatasource.register(auth)

    override suspend fun login(auth: Auth) =
        focusStartDatasource.login(auth)

    override fun createLoan(loanRequest: LoanRequest): Loan {
        TODO("Not yet implemented")
    }

    override fun getLoanData(id: Long): Loan {
        TODO("Not yet implemented")
    }

    override fun getAllLoans(): List<Loan> {
        TODO("Not yet implemented")
    }

    override fun getLoanConditions(): LoanConditions {
        TODO("Not yet implemented")
    }
}