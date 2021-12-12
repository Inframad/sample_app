package com.test.a2021_q4_tyukavkin.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.usecase.LoginUsecase
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import com.test.a2021_q4_tyukavkin.presentation.state.UserAuthorizationFragmentState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
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
        _state.value = UserAuthorizationFragmentState.DEFAULT
    }

    private val loginExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    404 -> _state.value = UserAuthorizationFragmentState.INVALID_CREDENTIALS
                }
            }
            is SocketTimeoutException ->
                _state.value = UserAuthorizationFragmentState.TIMEOUT_EXCEPTION
        }
    }

    private val registerExceptionHandler = CoroutineExceptionHandler {  _, throwable ->
        when (throwable) {
            is SocketTimeoutException ->
                _state.value = UserAuthorizationFragmentState.TIMEOUT_EXCEPTION
        }
    }

    fun register(auth: Auth) {
        viewModelScope.launch {
            _user.value = registrationUsecase(auth)
        }
    }

    fun login(auth: Auth) {
        viewModelScope.launch(loginExceptionHandler) {
            _state.value = UserAuthorizationFragmentState.LOADING
            val deferredLogging = async { loginUsecase(auth) }
            deferredLogging.await()
            _state.value = UserAuthorizationFragmentState.LOADED
        }
    }

    fun setNetworkState(isAvailable: Boolean) {
        viewModelScope.launch(registerExceptionHandler) {
            if (isAvailable) {
                _state.value = UserAuthorizationFragmentState.DEFAULT
            } else {
                _state.value = UserAuthorizationFragmentState.NO_INTERNET_CONNECTION
            }
        }
    }

}