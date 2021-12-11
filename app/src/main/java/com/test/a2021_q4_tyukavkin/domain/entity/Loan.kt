package com.test.a2021_q4_tyukavkin.domain.entity

import java.time.OffsetDateTime

data class Loan(
    val amount: Float, //TODO
    val offsetDateTime: OffsetDateTime,
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
