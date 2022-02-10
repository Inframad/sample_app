package com.test.a2021_q4_tyukavkin.data.datasource.remote

import com.test.a2021_q4_tyukavkin.data.converter.convertErrorBody
import com.test.a2021_q4_tyukavkin.data.converter.toAuthErrorResponse
import com.test.a2021_q4_tyukavkin.data.model.AuthErrorResponseDTO
import com.test.a2021_q4_tyukavkin.data.model.AuthToken
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.data.network.ServerApi
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.entity.RequestError
import com.test.a2021_q4_tyukavkin.domain.entity.exception.AuthException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatasource
@Inject constructor(
    private val serverApi: ServerApi,
    @DispatchersIO private val dispatchersIO: CoroutineDispatcher
) {
    suspend fun register(auth: Auth): UserDTO =
        withContext(dispatchersIO) {
            return@withContext try {
                serverApi.register(auth)
            } catch (httpException: HttpException) {
                when (httpException.code()) {
                    400 -> throw AuthException(
                        httpException.convertErrorBody(AuthErrorResponseDTO::class.java)
                            ?.toAuthErrorResponse()
                    )
                    500 -> throw RequestError(500)
                    else -> throw UnknownError(httpException.message)
                }
            }
        }

    suspend fun login(auth: Auth): AuthToken =
        withContext(dispatchersIO) {
            return@withContext try {
                serverApi.login(auth)
            } catch (httpException: HttpException) {
                when (httpException.code()) {
                    404 -> throw RequestError(404)
                    500 -> throw RequestError(500)
                    else -> throw UnknownError(httpException.message)
                }
            }
        }


    suspend fun getLoanConditions() =
        withContext(dispatchersIO) {
            serverApi.getLoanConditions()
        }

    suspend fun createLoan(loanRequest: LoanRequest): LoanDTO =
        withContext(dispatchersIO) {
            serverApi.createLoan(loanRequest)
        }

    suspend fun getAllLoans() =
        withContext(dispatchersIO) {
            serverApi.getAllLoans()
        }

    suspend fun getLoanData(id: Long): LoanDTO =
        serverApi.getLoanData(id)

}