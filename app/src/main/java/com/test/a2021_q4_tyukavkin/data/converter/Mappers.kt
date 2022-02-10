package com.test.a2021_q4_tyukavkin.data.converter

import com.test.a2021_q4_tyukavkin.data.model.AuthErrorResponseDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanConditionsDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.*
import com.test.a2021_q4_tyukavkin.domain.entity.exception.AuthErrorResponse
import com.test.a2021_q4_tyukavkin.domain.entity.exception.LoginError
import com.test.a2021_q4_tyukavkin.domain.entity.exception.PasswordError
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

private const val TOO_SHORT_PASSWORD =
    "This password is too short. It must contain at least 8 characters."
private const val TOO_COMMON_PASSWORD = "This password is too common."
private const val NUMERIC_PASSWORD = "This password is entirely numeric."
private const val BLANK_FIELD = "This field may not be blank."
private const val LOGIN_BUSY = "A user with that username already exists."
private const val PASSWORD_TOO_SIMILAR_TO_LOGIN = "The password is too similar to the username."

fun AuthErrorResponseDTO.toAuthErrorResponse(): AuthErrorResponse =
    AuthErrorResponse(
        username = username?.map {
            when (it) {
                BLANK_FIELD -> LoginError.BLANK_FIELD
                LOGIN_BUSY -> LoginError.BUSY
                else -> LoginError.INCORRECT
            }
        },
        password = password?.map {
            when (it) {
                TOO_SHORT_PASSWORD -> PasswordError.TOO_SHORT
                TOO_COMMON_PASSWORD -> PasswordError.TOO_COMMON
                NUMERIC_PASSWORD -> PasswordError.ENTIRELY_NUMERIC
                BLANK_FIELD -> PasswordError.BLANK_FIELD
                PASSWORD_TOO_SIMILAR_TO_LOGIN -> PasswordError.TOO_SIMILAR_TO_LOGIN
                else -> PasswordError.INCORRECT
            }
        }
    )