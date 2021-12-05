package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository

class GetLoanDataUsecase(private val repository: Repository) {

    operator fun invoke(id: Long) =
        repository.getLoanData(id)
}