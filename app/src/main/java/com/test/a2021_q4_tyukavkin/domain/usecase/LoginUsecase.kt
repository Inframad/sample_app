package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class LoginUsecase @Inject constructor(private val repository: Repository) {

    operator fun invoke(user: User) =
        repository.login(user)
}