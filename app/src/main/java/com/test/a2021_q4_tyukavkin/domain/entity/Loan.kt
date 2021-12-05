package com.test.a2021_q4_tyukavkin.domain.entity

import java.math.BigDecimal

data class Loan(
    private val amount: BigDecimal,
    private val date: String, //TODO Date
    private val firstName: String,
    private val id: Long,
    private val lastName: String,
    private val percent: Double,
    private val period: Int,
    private val phoneNumber: String,
    private val state: LoanState
)

enum class LoanState {
    APPROVED, REGISTERED, REJECTED
}
