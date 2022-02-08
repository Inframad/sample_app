package com.test.a2021_q4_tyukavkin.data.converter

import com.test.a2021_q4_tyukavkin.data.model.LoanConditionsDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.*
import java.time.OffsetDateTime

fun UserDTO.toUser() =
    User(
        name = username,
        role = UserRole.USER
    )

fun LoanConditionsDTO.toLoanConditions() =
    LoanConditions(
        maxAmount = maxAmount,
        percent = percent,
        period = period
    )

fun LoanDTO.toLoan(): Loan {
    return Loan(
        amount = amount,
        offsetDateTime = OffsetDateTime.parse(date),
        firstName = firstName,
        id = id,
        lastName = lastName,
        percent = percent,
        period = period,
        phoneNumber = phoneNumber,
        state = when (state) {
            "APPROVED" -> LoanState.APPROVED
            "REGISTERED" -> LoanState.REGISTERED
            "REJECTED" -> LoanState.REJECTED
            else -> throw IllegalArgumentException("Loan's state can't be $state")
        }
    )
}