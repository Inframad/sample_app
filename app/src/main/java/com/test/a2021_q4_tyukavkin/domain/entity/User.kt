package com.test.a2021_q4_tyukavkin.domain.entity

data class User(
    private val name: String,
    private val role: UserRole
)

enum class UserRole {
    ADMIN, USER
}
