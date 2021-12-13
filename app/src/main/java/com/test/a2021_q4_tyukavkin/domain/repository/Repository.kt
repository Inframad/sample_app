package com.test.a2021_q4_tyukavkin.domain.repository

import com.test.a2021_q4_tyukavkin.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun checkAuthorization(): Boolean
    suspend fun register(auth: Auth): User
    suspend fun login(auth: Auth): String
    suspend fun createLoan(loanRequest: LoanRequest): Loan
    suspend fun getLoanData(id: Long): Loan
    suspend fun getLoanConditions(): LoanConditions
    suspend fun updateLoansList()
    fun getAllLoans(): Flow<List<Loan>>
}