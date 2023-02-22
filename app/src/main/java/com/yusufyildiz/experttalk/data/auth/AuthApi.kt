package com.yusufyildiz.experttalk.data.auth

import com.yusufyildiz.experttalk.data.auth.expert.ExpertRequest
import com.yusufyildiz.experttalk.data.auth.user.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("expert/signup")
    suspend fun expertSignUp(
        @Body request: ExpertRequest
    )

    @POST("expert/signin")
    suspend fun expertSignIn(
        @Body request: ExpertRequest
    ): TokenResponse

    @POST("user/signup")
    suspend fun userSignUp(
        @Body request: UserRequest
    )

    @POST("user/signin")
    suspend fun userSignIn(
        @Body request: UserRequest
    ) : TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

}