package com.yusufyildiz.experttalk.ui

sealed class SignUpUiEvent {
    data class SignUpEmailChanged(val value: String): SignUpUiEvent()
    data class SignUpUsernameChanged(val value: String): SignUpUiEvent()
    data class SignUpPasswordChanged(val value: String): SignUpUiEvent()
    object SignUp: SignUpUiEvent()
}