package com.test.a2021_q4_tyukavkin.domain.repository

import com.test.a2021_q4_tyukavkin.domain.entity.*

interface Repository {

    suspend fun register(auth: Auth): User
    suspend fun login(auth: Auth): String //TODO Возвращаемое значение token
    suspend fun createLoan(loanRequest: LoanRequest): Loan
    fun getLoanData(id: Long): Loan
    fun getAllLoans(): List<Loan>
    suspend fun getLoanConditions(): LoanConditions
}