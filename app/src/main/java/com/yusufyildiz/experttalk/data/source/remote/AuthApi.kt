package com.yusufyildiz.experttalk.data.source.remote

import com.yusufyildiz.experttalk.data.model.appointment.AppointmentRequest
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.data.model.expert.TransformedExpertRequest
import com.yusufyildiz.experttalk.data.model.token.TokenResponse
import com.yusufyildiz.experttalk.data.model.user.TransformedUserRequest
import com.yusufyildiz.experttalk.data.model.user.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("expert/signup")
    suspend fun expertSignUp(
        @Body request: ExpertRequest
    )
    @POST("expert/signin")
    suspend fun expertSignIn(
        @Body request: TransformedExpertRequest
    ): TokenResponse

    @POST("user/signup")
    suspend fun userSignUp(
        @Body request: UserRequest
    )

    @POST("user/signin")
    suspend fun userSignIn(
        @Body request: TransformedUserRequest
    ) : TokenResponse

    @POST("expert/appointment/add")
    suspend fun addAppointment(
        @Body request: ArrayList<AppointmentRequest>
    )

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @GET("user/data")
    suspend fun getUserData(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserRequest

    @GET("expert/data")
    suspend fun getExpertData(
        @Query("email") email: String,
        @Query("password") password: String
    ): ExpertRequest

    @GET("expert/data/detail")
    suspend fun getExpertDataWithUsername(
        @Query("username") username: String
    ): ExpertRequest

    @GET("expert/data/all")
    suspend fun getAllExpertData(): ArrayList<ExpertRequest>

}