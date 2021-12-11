package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.usecase.GetAllLoansUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.LoanHistoryState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanHistoryFragmentViewModel
@Inject constructor(
    private val getLoanListUsecase: GetAllLoansUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _loans = MutableLiveData<List<LoanPresentaion>>()
    val loans: LiveData<List<LoanPresentaion>> = _loans

    private val _state = MutableLiveData<LoanHistoryState>()
    val state: LiveData<LoanHistoryState> = _state

    fun getLoans() {
        _state.value = LoanHistoryState.LOADING

        viewModelScope.launch {
            val deferredLoans = async {
                getLoanListUsecase().map {
                    converter.convertToLoanPresentation(it)
                }
            }
            _loans.value = deferredLoans.await()
            _state.value = LoanHistoryState.LOADED
        }
    }
}