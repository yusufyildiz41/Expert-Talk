package com.yusufyildiz.experttalk.ui.signin.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.domain.repository.auth.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    val resultLoading = MutableLiveData<Boolean>()
    private val resultChannel = Channel<UserAuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            val result = repository.signIn(
                email = email,
                password = password
            )
            resultChannel.send(result)
        }
    }

    fun authenticate(){
        viewModelScope.launch {
            resultLoading.value = true
            val result = repository.authenticate()
            resultChannel.send(result)
            resultLoading.value = false
        }
    }
}