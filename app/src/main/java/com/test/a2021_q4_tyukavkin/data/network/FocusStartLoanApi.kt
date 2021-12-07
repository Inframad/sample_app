package com.test.a2021_q4_tyukavkin.data.network

import com.test.a2021_q4_tyukavkin.data.model.LoanConditionsDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.entity.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface FocusStartLoanApi { //TODO Naming

    @POST("registration")
    suspend fun register(@Body auth: Auth): UserDTO

    @POST("login")
    suspend fun login(@Body auth: Auth): String

    @POST("loans")
    suspend fun createLoan(
        @Header("Authorization") token: String,
        @Body loanRequest: LoanRequest
    ): LoanDTO

    @GET("loans/conditions")
    suspend fun getLoanConditions(@Header("Authorization") token: String): LoanConditionsDTO


}