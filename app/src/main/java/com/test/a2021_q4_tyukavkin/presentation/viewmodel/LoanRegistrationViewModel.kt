package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.InputDataError
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRegistrationInputData
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanRegistrationInputDataErrorsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanRegistrationViewModel
@Inject constructor(
    private val getLoanConditionsUsecase: GetLoanConditionsUsecase,
    private val createLoanUsecase: CreateLoanUsecase,
    private val getLoanRegistrationInputDataErrorsUsecase: GetLoanRegistrationInputDataErrorsUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _conditionsState: MutableLiveData<FragmentState> =
        MutableLiveData()
    val conditionsState: LiveData<FragmentState> = _conditionsState

    private val _loanRegistrationState: SingleLiveEvent<LoanRegistrationFragmentState> =
        SingleLiveEvent()
    val loanRegistrationState: LiveData<LoanRegistrationFragmentState> = _loanRegistrationState

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _inputDataError: SingleLiveEvent<InputDataError> = SingleLiveEvent()
    val inputDataError: LiveData<InputDataError> = _inputDataError

    private val _loan: MutableLiveData<LoanPresentaion> = MutableLiveData()
    val loan: LiveData<LoanPresentaion> = _loan

    private val exceptionHandlerGetRequest = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _conditionsState.value = FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _conditionsState.value = FragmentState.TIMEOUT
            else -> {
                _conditionsState.value = FragmentState.UNKNOWN_ERROR
                Log.e("LoanRegistrationVM", "exceptionHandlerGetRequest", throwable)
            }
        }
        Log.e("LoanRegistrationVM", "exceptionHandlerGetRequest", throwable)
    }

    private val exceptionHandlerPostRequest = CoroutineExceptionHandler { _, throwable ->
        _loanRegistrationState.value = when (throwable) {
            is UnknownHostException ->
                LoanRegistrationFragmentState.UNKNOWN_HOST
            is SocketTimeoutException ->
                LoanRegistrationFragmentState.TIMEOUT
            is NumberFormatException ->
                LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
            else ->
                LoanRegistrationFragmentState.UNKNOWN_ERROR
        }
        Log.e("LoanRegistrationVM", "exceptionHandlerPostRequest", throwable)
    }

    init {
        getLoanConditions()
    }

    fun getLoanConditions() {
        viewModelScope.launch(exceptionHandlerGetRequest) {
            _conditionsState.value = FragmentState.LOADING

            _loanConditions.value = getLoanConditionsUsecase()

            _conditionsState.value = FragmentState.LOADED
        }
    }

    fun registerLoan(inputData: LoanRegistrationInputData) {
        viewModelScope.launch(exceptionHandlerPostRequest) {
            val inputDataErrors =
                getLoanRegistrationInputDataErrorsUsecase(inputData, loanConditions.value!!)

            if (inputDataErrors.isEmpty()) {
                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADING

                val loan = createLoanUsecase(
                    LoanRequest(
                        amount = inputData.amount.toString().toLong(),
                        firstName = inputData.firstName.toString(),
                        lastName = inputData.lastName.toString(),
                        percent = loanConditions.value!!.percent,
                        period = loanConditions.value!!.period,
                        phoneNumber = inputData.phoneNumber.toString()
                    )
                )
                _loan.value = converter.convertToLoanPresentation(loan)

                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADED
            } else {
                for (inputDataError in inputDataErrors) {
                    _inputDataError.value = inputDataError
                }
                _loanRegistrationState.value = LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
            }
        }
    }

    fun checkInputData(inputData: LoanRegistrationInputData) {
        viewModelScope.launch(exceptionHandlerPostRequest) {
            val inputDataErrors =
                getLoanRegistrationInputDataErrorsUsecase(inputData, loanConditions.value!!)
            for (inputDataError in inputDataErrors) {
                _inputDataError.value = inputDataError
            }
        }
    }

}