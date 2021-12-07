package com.test.a2021_q4_tyukavkin.domain.usecase

import android.util.Log
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import javax.inject.Inject

class CreateLoanUsecase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(loanRequest: LoanRequest): Loan {
        Log.i("MyTAG", "CreateLoanUsecase")
        return repository.createLoan(loanRequest)
    }

}