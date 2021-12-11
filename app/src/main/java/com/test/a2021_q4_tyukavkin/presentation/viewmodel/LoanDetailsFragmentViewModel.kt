package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanDataUsecase
import com.test.a2021_q4_tyukavkin.presentation.converter.Converter
import com.test.a2021_q4_tyukavkin.presentation.model.LoanPresentaion
import com.test.a2021_q4_tyukavkin.presentation.state.LoanDetailsState
import com.test.a2021_q4_tyukavkin.presentation.toLoanPresentation
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanDetailsFragmentViewModel
@Inject constructor(
    private val getLoanDataUsecase: GetLoanDataUsecase,
    private val converter: Converter
) : ViewModel() {

    private val _state: MutableLiveData<LoanDetailsState> = MutableLiveData()
    val state: LiveData<LoanDetailsState> = _state

    private val _loan: MutableLiveData<LoanPresentaion> = MutableLiveData()
    val loan: LiveData<LoanPresentaion> = _loan

    init {
        _state.value = LoanDetailsState.LOADING
    }

    fun getLoanData(
        id: Long
    ) {
        viewModelScope.launch {
            val loanDeferred = async { getLoanDataUsecase(id) }
            _loan.value = converter.convertToLoanPresentation(loanDeferred.await())

            _state.value = LoanDetailsState.LOADED
        }
    }
}