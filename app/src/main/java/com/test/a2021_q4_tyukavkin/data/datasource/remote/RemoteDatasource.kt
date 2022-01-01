package com.test.a2021_q4_tyukavkin.data.datasource.remote

import com.test.a2021_q4_tyukavkin.data.converter.toLoanConditions
import com.test.a2021_q4_tyukavkin.data.model.LoanDTO
import com.test.a2021_q4_tyukavkin.data.model.UserDTO
import com.test.a2021_q4_tyukavkin.data.network.ServerApi
import com.test.a2021_q4_tyukavkin.di.DispatchersIO
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
            } catch (httpException: retrofit2.HttpException) {
                when (httpException.code()) {
                    400 -> throw IllegalArgumentException("Busy login")
                    500 -> throw IllegalAccessError() //TODO Обертки для ошибок
                    else -> throw UnknownError(httpException.message)
                }
            }
        }

    suspend fun login(auth: Auth): String =
        withContext(dispatchersIO) {
            return@withContext try {
                serverApi.login(auth)
            } catch (httpException: retrofit2.HttpException) {
                when (httpException.code()) {
                    404 -> throw IllegalAccessException("Invalid credentials")
                    500 -> throw IllegalAccessError() //TODO Обертки для ошибок
                    else -> throw UnknownError(httpException.message)
                }
            }
        }


    suspend fun getLoanConditions() =
        withContext(dispatchersIO) {
            serverApi.getLoanConditions().toLoanConditions() //TODO
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