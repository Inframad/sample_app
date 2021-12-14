package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.di.DispatchersDefault
import com.test.a2021_q4_tyukavkin.domain.entity.LoanConditions
import com.test.a2021_q4_tyukavkin.domain.entity.LoanRequest
import com.test.a2021_q4_tyukavkin.domain.usecase.CreateLoanUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanConditionsUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.LoanRegistrationFragmentState
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanRegistrationViewModel
@Inject constructor(
    private val getLoanConditionsUsecase: GetLoanConditionsUsecase,
    private val createLoanUsecase: CreateLoanUsecase,
    private val converter: Converter,
    @DispatchersDefault private val dispatchersDefault: CoroutineDispatcher
) : ViewModel() {

    private val _conditionsState: MutableLiveData<FragmentState> =
        MutableLiveData()
    val conditionsState: LiveData<FragmentState> = _conditionsState

    private val _loanRegistrationState: MutableLiveData<LoanRegistrationFragmentState> =
        MutableLiveData()
    val loanRegistrationState: LiveData<LoanRegistrationFragmentState> = _loanRegistrationState

    private val _loanConditions: MutableLiveData<LoanConditions> = MutableLiveData()
    val loanConditions: LiveData<LoanConditions> = _loanConditions

    private val _editTextError: MutableLiveData<EditTextError> = MutableLiveData()
    val editTextError: LiveData<EditTextError> = _editTextError

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
            is UnknownHostException -> _loanRegistrationState.value =
                LoanRegistrationFragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _loanRegistrationState.value =
                LoanRegistrationFragmentState.TIMEOUT
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

    fun registerLoan(inputData: Map<String, String?>) {
        viewModelScope.launch(exceptionHandlerPostRequest) {
            if (inputDataIsValid(inputData)) {
                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADING

                val deferredLoan = async { createLoanUsecase(createLoanRequest(inputData)) }
                _loan.value = converter.convertToLoanPresentation(deferredLoan.await())

                _loanRegistrationState.value = LoanRegistrationFragmentState.LOADED
            } else {
                _loanRegistrationState.value = LoanRegistrationFragmentState.INCORRECT_INPUT_DATA
            }
        }
    }

    fun checkAmountIsValid(amount: String?) {
        when (amount) {
            null -> _editTextError.value = EditTextError.AMOUNT_EMPTY
            "" -> _editTextError.value = EditTextError.AMOUNT_EMPTY
            else -> {
                if (amount.toFloat() > loanConditions.value!!.maxAmount) {
                    _editTextError.value = EditTextError.EXCEED_MAX_AMOUNT
                }
            }
        }
    }

    private suspend fun inputDataIsValid(inputData: Map<String, String?>): Boolean =
        withContext(dispatchersDefault) {
            inputData.values.map {
                if (it.isNullOrBlank()) {
                    return@withContext false
                }
            }
            if (inputData["amount"]!!.toLong() > loanConditions.value!!.maxAmount) return@withContext false
            true
        }

    private fun createLoanRequest(inputData: Map<String, String?>) =
        LoanRequest(
            amount = inputData["amount"]!!.toLong(),
            firstName = inputData["firstName"]!!,
            lastName = inputData["lastName"]!!,
            phoneNumber = inputData["phoneNumber"]!!,
            percent = loanConditions.value!!.percent,
            period = loanConditions.value!!.period
        )
}

enum class EditTextError {
    FIRST_NAME_EMPTY, LAST_NAME_EMPTY, NUMBER_EMPTY, AMOUNT_EMPTY, EXCEED_MAX_AMOUNT
}