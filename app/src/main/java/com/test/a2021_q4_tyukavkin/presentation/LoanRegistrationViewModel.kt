package com.test.a2021_q4_tyukavkin.presentation

import android.util.Log
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

    private val _loanConditions = MutableLiveData<LoanConditions>()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _loan = MutableLiveData<Loan>()
    val loan: LiveData<Loan> = _loan

    fun getLoanConditions() {
        viewModelScope.launch { //TODO Exception handler
            _loanConditions.value = getLoanConditionsUsecase()!! //TODO Null safety, errors handling
        }
    }

    fun registerLoan(loanRequest: LoanRequest) {
        viewModelScope.launch {
            Log.i("MyTAG", "Launched")
            _loan.value = createLoanUsecase(loanRequest)!! //TODO
        }
    }
}