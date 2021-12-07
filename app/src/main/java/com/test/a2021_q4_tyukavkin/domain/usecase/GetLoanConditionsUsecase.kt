package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class GetLoanConditionsUsecase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): LoanConditions =
        repository.getLoanConditions()
}