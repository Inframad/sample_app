package com.test.a2021_q4_tyukavkin.domain.entity

data class LoanConditions (
    private val maxAmount: Float, //TODO Уточнить тип number
    private val percent: Double,
    private val period: Int
)