package com.test.a2021_q4_tyukavkin.domain.entity

import java.math.BigDecimal

data class Loan(
    val amount: Float, //TODO
    val date: String, //TODO Date
    val firstName: String,
    val id: Long,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: LoanState
)

enum class LoanState {
    APPROVED, REGISTERED, REJECTED
}
