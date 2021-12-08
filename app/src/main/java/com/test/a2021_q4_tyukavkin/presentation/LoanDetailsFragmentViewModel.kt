package com.test.a2021_q4_tyukavkin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Loan
import com.test.a2021_q4_tyukavkin.domain.usecase.GetLoanDataUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoanDetailsFragmentViewModel
@Inject constructor(
    private val getLoanDataUsecase: GetLoanDataUsecase
) : ViewModel() {

    private val _loan: MutableLiveData<Loan> = MutableLiveData()
    val loan: LiveData<Loan> = _loan

    fun getLoanData(id: Long) {
        viewModelScope.launch {
            _loan.value = getLoanDataUsecase(id)
        }
    }
}