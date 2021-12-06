package com.test.a2021_q4_tyukavkin.data.model

import java.math.BigDecimal

data class LoanConditionsDTO (
    val maxAmount: BigDecimal, //TODO Уточнить тип number
    val percent: Double,
    val period: Int
)