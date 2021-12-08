package com.test.a2021_q4_tyukavkin.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.usecase.LoginUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationFragmentViewModel
@Inject constructor(
    private val registrationUsecase: RegistrationUsecase,
    private val loginUsecase: LoginUsecase
) : ViewModel() {

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _status: MutableLiveData<String> = MutableLiveData()
    val status: LiveData<String> = _status

    init {
        _status.value = "reg" //TODO Naming
    }

    private val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is retrofit2.HttpException -> Log.e("Error http", throwable.code().toString())
        }
    }

    fun register(auth: Auth) {
        viewModelScope.launch {
            _user.value = registrationUsecase(auth)
        }
    }

    fun login(auth: Auth) {
        Log.i("MyTAG", "Before launched")
        viewModelScope.launch(loginExceptionHandler) {
            Log.i("MyTAG", "launched")
            _status.value = "Loading"
            _status.value = loginUsecase(auth)
        }
    }

}