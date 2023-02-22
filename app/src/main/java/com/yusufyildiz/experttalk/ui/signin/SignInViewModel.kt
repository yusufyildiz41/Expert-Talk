package com.yusufyildiz.experttalk.ui.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.data.auth.AuthResult
import com.yusufyildiz.experttalk.data.auth.user.UserRepository
import com.yusufyildiz.experttalk.ui.AuthState
import com.yusufyildiz.experttalk.ui.SignInUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    var state by mutableStateOf(AuthState())
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: SignInUiEvent){
        when(event){
            is SignInUiEvent.SignInEmailChanged -> {
                state = state.copy(signInEmail = event.value)
            }
            is SignInUiEvent.SignInUsernameChanged -> {
                state = state.copy(signInUsername = event.value)
            }
            is SignInUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is SignInUiEvent.SignIn ->{
                signIn()
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(
                email = state.signInEmail,
                username = state.signInUsername,
                password = state.signInPassword
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.authenticate()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

}