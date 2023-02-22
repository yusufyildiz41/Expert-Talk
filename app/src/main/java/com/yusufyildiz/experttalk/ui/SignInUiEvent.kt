package com.yusufyildiz.experttalk.ui

sealed class SignInUiEvent {
    data class SignInEmailChanged(val value: String): SignInUiEvent()
    data class SignInUsernameChanged(val value: String): SignInUiEvent()
    data class SignInPasswordChanged(val value: String): SignInUiEvent()
    object SignIn: SignInUiEvent()
}