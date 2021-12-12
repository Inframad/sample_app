package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanRegistrationViewModel //TODO Naming shared viewModel
@Inject constructor(
    private val getLoanConditionsUsecase: GetLoanConditionsUsecase,
    private val createLoanUsecase: CreateLoanUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _conditionsState: MutableLiveData<FragmentState> =
        MutableLiveData()
    val conditionsState: LiveData<FragmentState> = _conditionsState

    private val _loanRegistrationState: MutableLiveData<FragmentState> =
        MutableLiveData()
    val loanRegistrationState: LiveData<FragmentState> = _loanRegistrationState

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _loan: MutableLiveData<LoanPresentaion> = MutableLiveData()
    val loan: LiveData<LoanPresentaion> = _loan

    private val exceptionHandlerGetRequest = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _conditionsState.value = FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _conditionsState.value = FragmentState.TIMEOUT
        }
        Log.e("ExceptionHandler", throwable.javaClass.toString(), throwable)
    }

    private val exceptionHandlerPostRequest = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _loanRegistrationState.value = FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _loanRegistrationState.value = FragmentState.TIMEOUT
        }
        Log.e("ExceptionHandler", throwable.javaClass.toString(), throwable)
    }

    init {
        getLoanConditions()
    }

    fun getLoanConditions() {
        _conditionsState.value = FragmentState.LOADING
        viewModelScope.launch(exceptionHandlerGetRequest) {
            val deferredLoanConditions = async { getLoanConditionsUsecase() }
            _loanConditions.value = deferredLoanConditions.await()
            _conditionsState.value = FragmentState.LOADED
        }
    }

    fun registerLoan(loanRequest: LoanRequest) {
        _loanRegistrationState.value = FragmentState.LOADING
        viewModelScope.launch(exceptionHandlerPostRequest) {
            val deferredLoan = async { createLoanUsecase(loanRequest) }
            _loan.value = converter.convertToLoanPresentation(deferredLoan.await())
            _loanRegistrationState.value = FragmentState.LOADED
        }
    }
}