package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class CreateLoanUsecase @Inject constructor(private val repository: Repository) {

    operator fun invoke(loanRequest: LoanRequest) =
        repository.createLoan(loanRequest)
}