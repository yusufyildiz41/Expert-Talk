package com.yusufyildiz.experttalk.data.auth.user

import android.content.SharedPreferences
import com.yusufyildiz.experttalk.data.auth.AuthApi
import com.yusufyildiz.experttalk.data.auth.AuthResult
import retrofit2.HttpException


class UserRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): UserRepository {
    override suspend fun signUp(email: String,username: String, password: String): AuthResult<Unit> {
        return try {
            api.userSignUp(
                request = UserRequest(
                    email = email,
                    username = username,
                    password = password
                )
            )
            signIn(email,username,password)
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        } catch (e: Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(email: String,username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.userSignIn(
                request = UserRequest(
                    email = email,
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt",response.token)
                .apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        } catch (e: Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt",null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        } catch (e: Exception){
            AuthResult.UnknownError()
        }
    }
}