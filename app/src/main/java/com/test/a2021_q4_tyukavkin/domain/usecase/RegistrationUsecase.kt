package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class RegistrationUsecase @Inject constructor(private val repository: Repository) { //TODO Naming

    suspend operator fun invoke(auth: Auth): User =
        repository.register(auth)
}