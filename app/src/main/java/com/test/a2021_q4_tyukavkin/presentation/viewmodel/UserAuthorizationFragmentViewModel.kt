package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.usecase.LoginUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class UserAuthorizationFragmentViewModel
@Inject constructor(
    private val registrationUsecase: RegistrationUsecase,
    private val loginUsecase: LoginUsecase
) : ViewModel() {

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _state: MutableLiveData<UserAuthorizationFragmentState> = MutableLiveData()
    val state: LiveData<UserAuthorizationFragmentState> = _state

    init {
        _state.value = DEFAULT
    }

    private val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = when (throwable) {
            is IllegalAccessException -> INVALID_CREDENTIALS
            is IllegalAccessError -> BAD_RESPONSE
            is UnknownHostException -> NO_INTERNET_CONNECTION
            is SocketTimeoutException -> TIMEOUT_EXCEPTION
            else -> UNKNOWN_ERROR
        }
        Log.e("MyTAG", "ERROR", throwable)
    }

    private val registerExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = when (throwable) {
            is SocketTimeoutException -> TIMEOUT_EXCEPTION
            is IllegalAccessError -> BAD_RESPONSE
            is UnknownHostException -> NO_INTERNET_CONNECTION
            is IllegalArgumentException -> BUSY_LOGIN
            else -> UNKNOWN_ERROR
        }
    }

    fun register(auth: Auth) {
        viewModelScope.launch(registerExceptionHandler) {
            _state.value = LOADING
            val deferredRegister = async { registrationUsecase(auth) }
            _user.value = deferredRegister.await()
            _state.value = DEFAULT
        }
    }

    fun login(auth: Auth) {
        viewModelScope.launch(loginExceptionHandler) {
            _state.value = LOADING
            val deferredLogging = async { loginUsecase(auth) }
            deferredLogging.await()
            _state.value = LOADED
        }
    }

    fun setNetworkState(isAvailable: Boolean) {
        viewModelScope.launch(registerExceptionHandler) {
            _state.value = if (isAvailable) DEFAULT else NO_INTERNET_CONNECTION
        }
    }

}