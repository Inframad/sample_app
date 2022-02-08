package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.LoanState
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanDataUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.FragmentState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoanDetailsFragmentViewModel
@Inject constructor(
    private val getLoanDataUsecase: GetLoanDataUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _state: MutableLiveData<FragmentState> = MutableLiveData()
    val state: LiveData<FragmentState> = _state

    private val _loanPresentation: MutableLiveData<LoanPresentaion> = MutableLiveData()
    val loanPresentation: LiveData<LoanPresentaion> = _loanPresentation

    private val _isApproved: MutableLiveData<Boolean> = MutableLiveData()
    val isApproved: LiveData<Boolean> = _isApproved

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = when (throwable) {
            is UnknownHostException -> FragmentState.UNKNOWN_HOST
            is SocketTimeoutException -> FragmentState.TIMEOUT
            else -> FragmentState.UNKNOWN_ERROR
        }
        Log.e("ExceptionHandler", throwable.javaClass.toString(), throwable)
    }

    fun getLoanData(id: Long) {
        _state.value = FragmentState.LOADING
        viewModelScope.launch(exceptionHandler) {
            val loan = getLoanDataUsecase(id)
            loan.apply {
                if (this.state == LoanState.APPROVED) _isApproved.value = true
                _loanPresentation.value = converter.convertToLoanPresentation(this)
            }
            _state.value = FragmentState.LOADED
        }
    }
}