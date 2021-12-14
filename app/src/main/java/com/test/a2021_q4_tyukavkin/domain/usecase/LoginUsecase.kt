package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class LoginUsecase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(auth: Auth) {
        repository.login(auth)
    }

}