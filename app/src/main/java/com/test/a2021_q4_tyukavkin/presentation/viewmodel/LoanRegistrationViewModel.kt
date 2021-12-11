package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.LoanConditionsFragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.LoanDetailsState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanRegistrationViewModel //TODO Naming shared viewModel
@Inject constructor(
    private val getLoanConditionsUsecase: GetLoanConditionsUsecase,
    private val createLoanUsecase: CreateLoanUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _conditionsState: MutableLiveData<LoanConditionsFragmentState> =
        MutableLiveData() //TODO Naming
    val conditionsState: LiveData<LoanConditionsFragmentState> = _conditionsState

    private val _detailState: MutableLiveData<LoanDetailsState> =
        MutableLiveData() //TODO Naming
    val detailState: LiveData<LoanDetailsState> = _detailState

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _loan: MutableLiveData<LoanPresentaion> = MutableLiveData()
    val loan: LiveData<LoanPresentaion> = _loan

    init {
        _conditionsState.value = LoanConditionsFragmentState.LOADING
        getLoanConditions()
    }

    private fun getLoanConditions() {
        viewModelScope.launch { //TODO Exception handler
            val deferredLoanConditions = async { getLoanConditionsUsecase() }
            _loanConditions.value = deferredLoanConditions.await()
            _conditionsState.value = LoanConditionsFragmentState.LOADED
        }
    }

    fun registerLoan(loanRequest: LoanRequest) {
        _detailState.value = LoanDetailsState.LOADING
        viewModelScope.launch {
            val deferredLoan = async { createLoanUsecase(loanRequest) }
            _loan.value = converter.convertToLoanPresentation(deferredLoan.await())
            _detailState.value = LoanDetailsState.LOADED
        }
    }
}