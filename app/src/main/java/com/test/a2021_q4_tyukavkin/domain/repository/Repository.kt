package com.test.a2021_q4_tyukavkin.domain.repository

import com.test.a2021_q4_tyukavkin.domain.entity.*

interface Repository {

    suspend fun register(auth: Auth): User
    fun login(user: User) //TODO Возвращаемое значение token
    fun createLoan(loanRequest: LoanRequest): Loan
    fun getLoanData(id: Long): Loan
    fun getAllLoans(): List<Loan>
    fun getLoanConditions(): LoanConditions
}