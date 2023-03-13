package com.yusufyildiz.experttalk.ui.signup.user

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
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val resultChannel = Channel<UserAuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    fun signUp(
        email: String,
        username: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            val result = repository.signUp(
                email = email,
                username = username,
                password = password,
                phone = phone
            )
            resultChannel.send(result)
        }
    }


}