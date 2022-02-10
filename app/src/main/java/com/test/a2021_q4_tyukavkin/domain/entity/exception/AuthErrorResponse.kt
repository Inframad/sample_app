package com.test.a2021_q4_tyukavkin.domain.entity.exception

data class AuthErrorResponse(
    val username: List<LoginError>?,
    val password: List<PasswordError>?,
)

enum class LoginError {
    INCORRECT, BLANK_FIELD, BUSY
}

enum class PasswordError {
    INCORRECT, TOO_SHORT, TOO_COMMON, ENTIRELY_NUMERIC, BLANK_FIELD, TOO_SIMILAR_TO_LOGIN
}