package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class GetAllLoansUsecase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(): List<Loan> =
        repository.getAllLoans()
}