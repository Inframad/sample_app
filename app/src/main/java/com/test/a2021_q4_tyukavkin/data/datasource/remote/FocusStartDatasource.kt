package com.test.a2021_q4_tyukavkin.data.datasource.remote

import com.test.a2021_q4_tyukavkin.data.converter.toUser
import com.test.a2021_q4_tyukavkin.data.network.FocusStartLoanApi
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FocusStartDatasource @Inject constructor(private val focusStartLoanApi: FocusStartLoanApi) { //TODO Naming

    suspend fun register(auth: Auth): User =
        withContext(Dispatchers.IO) {
            focusStartLoanApi.register(auth).toUser() //TODO Обработка ошибок
        }

}