package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRegistrationInputData
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanRegistrationInputDataErrorsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.EditTextError
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
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

    private val _loanRegistrationState: MutableLiveData<LoanRegistrationFragmentState> =
        MutableLiveData()
    val loanRegistrationState: LiveData<LoanRegistrationFragmentState> = _loanRegistrationState

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _editTextError: SingleLiveEvent<EditTextError> = SingleLiveEvent()
    val editTextError: LiveData<EditTextError> = _editTextError

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
    }

    private val exceptionHandlerPostRequest = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _loanRegistrationState.value =
                LoanRegistrationFragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _loanRegistrationState.value =
                LoanRegistrationFragmentState.TIMEOUT
            is NumberFormatException -> _loanRegistrationState.value =
                LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
            else -> {
                LoanRegistrationFragmentState.UNKNOWN_ERROR
                Log.e("LoanRegistrationVM", "exceptionHandlerPostRequest", throwable)
            }
        }
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

    fun registerLoan(inputData: LoanRegistrationInputData) {
        viewModelScope.launch(exceptionHandlerPostRequest) {
            val inputDataErrors =
                getLoanRegistrationInputDataErrorsUsecase(inputData, loanConditions.value!!)
            if (inputDataErrors.isEmpty()) {
                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADING

                val deferredLoan = async {
                    createLoanUsecase(
                        LoanRequest(
                            amount = inputData.amount.toString().toLong(),
                            firstName = inputData.firstName.toString(),
                            lastName = inputData.lastName.toString(),
                            percent = loanConditions.value!!.percent,
                            period = loanConditions.value!!.period,
                            phoneNumber = inputData.phoneNumber.toString()
                        )
                    )
                }
                _loan.value = converter.convertToLoanPresentation(deferredLoan.await())
                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADED
            } else {
                for (inputDataError in inputDataErrors) {
                    _editTextError.value = inputDataError
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
                _editTextError.value = inputDataError
            }
        }
    }

}