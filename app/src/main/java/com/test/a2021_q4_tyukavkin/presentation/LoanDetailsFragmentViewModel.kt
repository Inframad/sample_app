package com.test.a2021_q4_tyukavkin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanDataUsecase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanDetailsFragmentViewModel
@Inject constructor(
    private val getLoanDataUsecase: GetLoanDataUsecase
) : ViewModel() {

    private val _state: MutableLiveData<LoanDetailsState> = MutableLiveData()
    val state: LiveData<LoanDetailsState> = _state

    private val _loan: MutableLiveData<Loan> = MutableLiveData()
    val loan: LiveData<Loan> = _loan

    init {
        _state.value = LoanDetailsState.LOADING
    }

    fun getLoanData(id: Long) {
        viewModelScope.launch {
            val loanDeferred = async {getLoanDataUsecase(id)}
            _loan.value = loanDeferred.await()
            _state.value = LoanDetailsState.LOADED
        }
    }
}