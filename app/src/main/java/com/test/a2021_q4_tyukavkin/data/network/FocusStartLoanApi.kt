package com.test.a2021_q4_tyukavkin.data.network

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FocusStartLoanApi { //TODO Naming

    @POST("registration")
    suspend fun register(@Body auth: Auth): UserDTO

    @POST("login")
    suspend fun login(@Body auth: Auth): String
}