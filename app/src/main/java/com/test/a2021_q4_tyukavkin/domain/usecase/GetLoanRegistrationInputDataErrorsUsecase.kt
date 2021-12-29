package com.test.a2021_q4_tyukavkin.domain.usecase

import com.test.a2021_q4_tyukavkin.domain.entity.InputDataError
import com.test.a2021_q4_tyukavkin.domain.entity.InputDataError.*
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRegistrationInputData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLoanRegistrationInputDataErrorsUsecase @Inject constructor() {

    suspend operator fun invoke(
        loanRegistrationInputData: LoanRegistrationInputData,
        loanConditions: LoanConditions
    ): List<InputDataError> =
        withContext(Dispatchers.Default) {
            val inputErrors: MutableList<InputDataError> = mutableListOf()
            loanRegistrationInputData.apply {

                if (firstName.isNullOrBlank()) inputErrors.add(FIRST_NAME_EMPTY)
                if (lastName.isNullOrBlank()) inputErrors.add(LAST_NAME_EMPTY)
                if (phoneNumber.isNullOrBlank()) inputErrors.add(NUMBER_EMPTY)
                if (amount.isNullOrBlank()) {
                    inputErrors.add(AMOUNT_EMPTY)
                } else {
                    if (amount.toLong() !in 1L..loanConditions.maxAmount.toLong())
                        inputErrors.add(EXCEED_MAX_AMOUNT)
                }
            }
            inputErrors
        }

}