package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLoansUsecase @Inject constructor(private val repository: Repository) {

    operator fun invoke(): Flow<List<Loan>> =
        repository.getAllLoans()
}