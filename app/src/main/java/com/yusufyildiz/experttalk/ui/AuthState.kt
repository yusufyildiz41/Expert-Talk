package com.yusufyildiz.experttalk.ui

data class AuthState(
    val isLoading: Boolean = false,
    var signInEmail: String = "",
    var signUpEmail:String = "",
    var signUpUsername: String = "",
    var signUpPassword: String = "",
    var signInUsername: String = "",
    var signInPassword: String = ""
)