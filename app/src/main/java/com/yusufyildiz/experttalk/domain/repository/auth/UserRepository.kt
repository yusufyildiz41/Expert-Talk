package com.yusufyildiz.experttalk.domain.repository.auth

import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.data.model.user.UserRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signUp(
        email: String,
        username: String,
        password: String,
        phone: String
    ): UserAuthResult<Unit>
    suspend fun signIn(
        email: String,
        password: String
    ): UserAuthResult<Unit>
    suspend fun authenticate(): UserAuthResult<Unit>
    suspend fun getUserData(email: String,password: String): Flow<UserRequest>

    suspend fun signOut(): UserAuthResult<Unit>
}