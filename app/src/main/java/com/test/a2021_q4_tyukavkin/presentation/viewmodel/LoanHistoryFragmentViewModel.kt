package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.usecase.GetAllLoansUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanHistoryFragmentViewModel
@Inject constructor(
    private val getLoanListUsecase: GetAllLoansUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _loans = MutableLiveData<List<LoanPresentaion>>()
    val loans: LiveData<List<LoanPresentaion>> = _loans

    private val _state = MutableLiveData<FragmentState>()
    val state: LiveData<FragmentState> = _state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _state.value = FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _state.value = FragmentState.TIMEOUT
        }
        Log.e("ExceptionHandler", throwable.javaClass.toString(), throwable)
    }

    init {
        _state.value = FragmentState.LOADING
        getLoans()
    }

    fun getLoans() {
        viewModelScope.launch(exceptionHandler) {
            val deferredLoans = async {
                getLoanListUsecase().map {
                    converter.convertToLoanPresentation(it)
                }
            }
            _loans.value = deferredLoans.await()
            _state.value = FragmentState.LOADED
        }
    }
}