package com.test.a2021_q4_tyukavkin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.usecase.GetAllLoansUsecase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanHistoryFragmentViewModel
@Inject constructor(
    private val getLoanListUsecase: GetAllLoansUsecase
) : ViewModel() {

    private val _loans = MutableLiveData<List<Loan>>()
    val loans: LiveData<List<Loan>> = _loans

    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state

    init {
        _state.value = "Loading"

        viewModelScope.launch {
            val deferredLoans = async { getLoanListUsecase()!! }  //TODO Null safety
            _loans.value = deferredLoans.await()
            _state.value = "Loaded"
        }
    }
}