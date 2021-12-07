package com.test.a2021_q4_tyukavkin.data.model

data class LoanConditionsDTO (
    val maxAmount: Float, //TODO Уточнить тип number
    val percent: Double,
    val period: Int
)