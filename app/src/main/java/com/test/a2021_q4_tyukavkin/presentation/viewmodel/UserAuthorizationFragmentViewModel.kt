package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.RequestError
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.entity.exception.AuthException
import com.test.a2021_q4_tyukavkin.domain.entity.exception.LoginError
import com.test.a2021_q4_tyukavkin.domain.entity.exception.PasswordError
import com.test.a2021_q4_tyukavkin.domain.usecase.LoginUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState.*
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val _loginError: MutableLiveData<List<LoginError>> = MutableLiveData()
    val loginError: LiveData<List<LoginError>> = _loginError

    private val _passwordError: MutableLiveData<List<PasswordError>> = MutableLiveData()
    val passwordError: LiveData<List<PasswordError>> = _passwordError

    init {
        _state.value = DEFAULT
    }

    private val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = when (throwable) {
            is RequestError -> {
                when (throwable.code) {
                    404 -> INVALID_CREDENTIALS
                    500 -> SERVER_ERROR
                    else -> UNKNOWN_ERROR
                }
            }
            is UnknownHostException -> NO_INTERNET_CONNECTION
            is SocketTimeoutException -> TIMEOUT_EXCEPTION
            else -> UNKNOWN_ERROR
        }
        Log.e("UserAuthorization", "loginExceptionHandler", throwable)
    }

    private val registerExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = when (throwable) {
            is RequestError -> {
                when (throwable.code) {
                    500 -> SERVER_ERROR
                    else -> UNKNOWN_ERROR
                }
            }
            is SocketTimeoutException -> TIMEOUT_EXCEPTION
            is UnknownHostException -> NO_INTERNET_CONNECTION
            is AuthException -> INVALID_CREDENTIALS
            else -> UNKNOWN_ERROR
        }

        if (throwable is AuthException) {
            throwable.value?.username?.let {
                _loginError.value = it
            }

            throwable.value?.password?.let {
                _passwordError.value = it
            }
        }

        Log.e("LoanRegistrationVM", "registerExceptionHandler", throwable)
    }

    fun register(auth: Auth) {
        viewModelScope.launch(registerExceptionHandler) {
            _state.value = LOADING
            _user.value = registrationUsecase(auth)
            _state.value = DEFAULT
        }
    }

    fun login(auth: Auth) {
        viewModelScope.launch(loginExceptionHandler) {
            _state.value = LOADING
            loginUsecase(auth)
            _state.value = LOADED
        }
    }

    fun setNetworkState(isAvailable: Boolean) {
        viewModelScope.launch(registerExceptionHandler) {
            _state.value = if (isAvailable) DEFAULT else NO_INTERNET_CONNECTION
        }
    }

}