package com.yusufyildiz.experttalk.data.auth.expert

import com.yusufyildiz.experttalk.data.auth.AuthResult

interface ExpertRepository {
    suspend fun signUp(email: String,username: String,password: String): AuthResult<Unit>
    suspend fun signIn(email: String,username: String,password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}