package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.repository.Repository

class LoginUsecase(private val repository: Repository) {

    operator fun invoke(user: User) =
        repository.login(user)
}