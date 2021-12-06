package com.test.a2021_q4_tyukavkin.data.model

import java.math.BigDecimal

data class LoanDTO(
    val amount: BigDecimal,
    val date: String, //TODO Date
    val firstName: String,
    val id: Long,
    val lastName: String,
    val percent: Double,
    val period: Int,
    val phoneNumber: String,
    val state: String
)

