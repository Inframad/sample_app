package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class GetLoanDataUsecase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(id: Long) =
        repository.getLoanData(id)
}