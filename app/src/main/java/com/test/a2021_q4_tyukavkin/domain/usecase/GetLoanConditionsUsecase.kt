package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class GetLoanConditionsUsecase @Inject constructor(private val repository: Repository) {

    operator fun invoke() =
        repository.getLoanConditions()
}