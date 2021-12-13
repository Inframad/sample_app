package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.usecase.GetAllLoansUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.UpdateLoansListUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanHistoryFragmentViewModel
@Inject constructor(
    private val getLoanListUsecase: GetAllLoansUsecase,
    private val updateLoansListUsecase: UpdateLoansListUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _state = MutableLiveData<FragmentState>()
    val state: LiveData<FragmentState> = _state

    fun getLoans() = getLoanListUsecase().map {
        it.map { loan -> converter.convertToLoanPresentation(loan) }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is UnknownHostException -> _state.value = FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> _state.value = FragmentState.TIMEOUT
        }
        Log.e("ExceptionHandler", throwable.javaClass.toString(), throwable)
    }

    init {
        updateLoans()
    }

    fun updateLoans() {
        _state.value = FragmentState.LOADING
        viewModelScope.launch(exceptionHandler) {
            val deferredUpdate = async { updateLoansListUsecase() }
            deferredUpdate.await()
            _state.value = FragmentState.LOADED
        }
    }
}
