package com.test.a2021_q4_tyukavkin.domain.entity

data class LoanConditions(
    val maxAmount: Float, //TODO Уточнить тип number
    val percent: Double,
    val period: Int
)