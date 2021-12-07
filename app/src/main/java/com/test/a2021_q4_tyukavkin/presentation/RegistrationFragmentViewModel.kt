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
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationFragmentViewModel
@Inject constructor(
    private val registrationUsecase: RegistrationUsecase,
    private val loginUsecase: LoginUsecase
) : ViewModel() {

    private val _response = MutableLiveData<User>()
    val response: LiveData<User> = _response

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    init {
        _status.value = "reg" //TODO Naming
    }

    fun register(auth: Auth) {
        viewModelScope.launch {
            _response.value = registrationUsecase(auth)!! //TODO
            Log.i("ServerResponse", _response.value?.name.toString())
        }
    }

    fun login(auth: Auth) {
        viewModelScope.launch {
            _status.value = "Loading"
            _status.value = loginUsecase(auth)!! //TODO
        }
    }

}