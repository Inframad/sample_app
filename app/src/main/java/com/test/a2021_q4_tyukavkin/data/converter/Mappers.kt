package com.test.a2021_q4_tyukavkin.data.converter

import com.test.a2021_q4_tyukavkin.data.model.LoanConditionsDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanRequestDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.*

fun UserDTO.toUser() =
    User(
        name = name,
        role = when (role) { //TODO Улучшить
            "ADMIN" -> UserRole.ADMIN
            "USER" -> UserRole.USER
            else -> throw IllegalArgumentException("User's role can't be $role")
        }
    )

fun LoanConditionsDTO.toLoanConditions() =
    LoanConditions(
        maxAmount = maxAmount,
        percent = percent,
        period = period
    )

fun LoanDTO.toLoan() =
    Loan(
        amount = amount,
        date = date,
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

fun LoanRequestDTO.toLoanRequest() =
    LoanRequest(
        amount = amount,
        firstName = firstName,
        lastName = lastName,
        percent = percent,
        period = period,
        phoneNumber = phoneNumber
    )
