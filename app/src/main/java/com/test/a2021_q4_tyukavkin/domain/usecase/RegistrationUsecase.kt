package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.repository.Repository

class RegistrationUsecase(private val repository: Repository) { //TODO Naming

    operator fun invoke(auth: Auth) =
        repository.register(auth)
}