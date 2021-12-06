package com.test.a2021_q4_tyukavkin.data.model

data class LoanRequestDTO(
    val amount: Long,
    val firstName: String,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String
)
