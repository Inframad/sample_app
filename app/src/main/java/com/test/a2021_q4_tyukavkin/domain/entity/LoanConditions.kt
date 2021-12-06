package com.test.a2021_q4_tyukavkin.domain.entity

import java.math.BigDecimal

data class LoanConditions (
    private val maxAmount: BigDecimal, //TODO Уточнить тип number
    private val percent: Double,
    private val period: Int
)