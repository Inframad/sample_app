package com.test.a2021_q4_tyukavkin.data.network

import com.test.a2021_q4_tyukavkin.data.model.AuthToken
import com.test.a2021_q4_tyukavkin.data.model.LoanConditionsDTO
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerApi {

    //@POST("registration")
    @POST("registration/users/")
    suspend fun register(@Body auth: Auth): UserDTO

    //@POST("login")
    @POST("auth_token/token/login/")
    suspend fun login(@Body auth: Auth): AuthToken

    @POST("loans/")
    suspend fun createLoan(
        @Body loanRequest: LoanRequest
    ): LoanDTO

    @GET("loans/conditions/")
    suspend fun getLoanConditions(): LoanConditionsDTO

    @GET("loans/all/")
    suspend fun getAllLoans(): List<LoanDTO>

    @GET("loans/{id}/")
    suspend fun getLoanData(
        @Path("id") id: Long
    ): LoanDTO
}