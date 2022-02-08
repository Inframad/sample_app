package com.test.a2021_q4_tyukavkin.data.network

import com.test.a2021_q4_tyukavkin.data.datasource.local.LocalDatasource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject constructor(
    private val localDatasource: LocalDatasource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        localDatasource.getToken()?.let {
            val authRequest = request.newBuilder()
                .header("Authorization", "Token $it").build()
            return chain.proceed(authRequest)
        }
        return chain.proceed(request)
    }
}