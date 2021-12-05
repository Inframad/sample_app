package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository

class GetLoanConditionsUsecase(private val repository: Repository) {

    operator fun invoke() =
        repository.getLoanConditions()
}