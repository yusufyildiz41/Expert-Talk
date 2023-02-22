package com.yusufyildiz.experttalk.data.auth.expert

data class ExpertRequest(
    val email: String,
    val username: String,
    val password: String
)