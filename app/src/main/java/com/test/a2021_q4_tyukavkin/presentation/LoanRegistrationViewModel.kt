package com.test.a2021_q4_tyukavkin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanRegistrationViewModel //TODO Naming
@Inject constructor(
    private val getLoanConditionsUsecase: GetLoanConditionsUsecase,
    private val createLoanUsecase: CreateLoanUsecase
) : ViewModel() {

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _loan: MutableLiveData<Loan> = MutableLiveData()
    val loan: LiveData<Loan> = _loan

    fun getLoanConditions() {
        viewModelScope.launch { //TODO Exception handler
            _loanConditions.value = getLoanConditionsUsecase()
        }
    }

    fun registerLoan(loanRequest: LoanRequest) {
        viewModelScope.launch {
            _loan.value = createLoanUsecase(loanRequest)
        }
    }
}