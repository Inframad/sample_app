package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.usecase.CheckAuthorizationUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel
@Inject constructor(
    private val checkAuthorizationUsecase: CheckAuthorizationUsecase
) : ViewModel() {

    private val _isAuthorized: MutableLiveData<Boolean> = MutableLiveData()
    val isAuthorized: LiveData<Boolean> = _isAuthorized

    init {
        viewModelScope.launch {
            _isAuthorized.value = checkAuthorizationUsecase()
            Log.i("MyTAG", "Auth ${isAuthorized.value}")
        }
    }

}