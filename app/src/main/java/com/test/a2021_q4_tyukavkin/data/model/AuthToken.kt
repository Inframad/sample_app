package com.test.a2021_q4_tyukavkin.data.model

import com.squareup.moshi.Json

data class AuthToken(
    @field:Json(name="auth_token")
    val value: String
)
