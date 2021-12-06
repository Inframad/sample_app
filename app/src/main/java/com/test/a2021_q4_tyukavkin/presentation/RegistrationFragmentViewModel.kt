package com.test.a2021_q4_tyukavkin.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.a2021_q4_tyukavkin.domain.entity.Auth
import com.test.a2021_q4_tyukavkin.domain.entity.User
import com.test.a2021_q4_tyukavkin.domain.usecase.RegistrationUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationFragmentViewModel
@Inject constructor(
    private val registrationUsecase: RegistrationUsecase
) : ViewModel() {

    private val _response = MutableLiveData<User>()
    val response: LiveData<User> = _response

    fun register(auth: Auth) {
        viewModelScope.launch {
            _response.value = registrationUsecase(auth)!!
            Log.i("ServerResponse", _response.value?.name.toString())
        }
    }

}