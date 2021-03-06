package com.test.a2021_q4_tyukavkin.presentation.model

import com.test.a2021_q4_tyukavkin.domain.entity.LoanState

data class LoanPresentaion (
    val amount: Float, //TODO
    val date: String,
    val time: String,
    val firstName: String,
    val id: Long,
    val lastName: String,
    val percent: String,
    val period: Int,
    val phoneNumber: String,
    val state: LoanState
)