package com.test.a2021_q4_tyukavkin.domain.entity

data class User(
    val name: String,
    val role: UserRole
)

enum class UserRole {
    ADMIN, USER
}
