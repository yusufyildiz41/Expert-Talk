package com.yusufyildiz.experttalk.data.auth.user

import com.yusufyildiz.experttalk.data.auth.AuthResult

interface UserRepository {
    suspend fun signUp(email: String,username: String, password: String): AuthResult<Unit>
    suspend fun signIn(email: String,username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}