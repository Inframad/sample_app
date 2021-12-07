package com.test.a2021_q4_tyukavkin.data.repository

import android.util.Log
import com.test.a2021_q4_tyukavkin.data.datasource.remote.FocusStartDatasource
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl
@Inject constructor(
    private val focusStartDatasource: FocusStartDatasource
    ) :
    Repository {

    override suspend fun register(auth: Auth): User =
        focusStartDatasource.register(auth)

    override suspend fun login(auth: Auth) =
        focusStartDatasource.login(auth)

    override suspend fun createLoan(loanRequest: LoanRequest): Loan {
        Log.i("MyTAG", "Repository create loan")
        return focusStartDatasource.createLoan(loanRequest)
    }


    override fun getLoanData(id: Long): Loan {
        TODO("Not yet implemented")
    }

    override fun getAllLoans(): List<Loan> {
        TODO("Not yet implemented")
    }

    override suspend fun getLoanConditions(): LoanConditions =
        focusStartDatasource.getLoanConditions()
}