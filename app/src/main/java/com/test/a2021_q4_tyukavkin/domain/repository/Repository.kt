package com.test.a2021_q4_tyukavkin.domain.repository

import com.test.a2021_q4_tyukavkin.domain.entity.*

interface Repository {

    fun register(auth: Auth): String //TODO Возвращаемое значение token
    fun login(user: User)
    fun createLoan(loanRequest: LoanRequest): Loan
    fun getLoanData(id: Long): Loan
    fun getAllLoans(): List<Loan>
    fun getLoanConditions(): LoanConditions
}