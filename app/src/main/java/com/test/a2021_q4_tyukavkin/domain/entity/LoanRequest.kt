package com.test.a2021_q4_tyukavkin.domain.entity

data class LoanRequest(
    private val amount: Long,
    private val firstName: String,
    private val lastName: String,
    private val percent: Double,
    private val period: Int,
    private val phoneNumber: String
)
