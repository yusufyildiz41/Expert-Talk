package com.yusufyildiz.experttalk.data.auth.expert

import android.content.SharedPreferences
import com.yusufyildiz.experttalk.data.auth.AuthApi
import com.yusufyildiz.experttalk.data.auth.AuthResult
import retrofit2.HttpException

class ExpertRepositoryImpl(
    private val api: AuthApi,
    private val pref: SharedPreferences
): ExpertRepository {
    override suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): AuthResult<Unit> {
        return try {
            api.expertSignUp(
                request = ExpertRequest(
                    email = email,
                    username = username,
                    password = password
                )
            )
            signIn(email,username,password)
        }catch (e: HttpException){
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

    override suspend fun signIn(
        email: String,
        username: String,
        password: String
    ): AuthResult<Unit> {
        return try{
            val response = api.expertSignIn(
                request = ExpertRequest(
                    email = email,
                    username = username,
                    password = password
                )
            )
            pref.edit()
                .putString("jwt",response.token)
                .apply()
            AuthResult.Authorized()
        }catch (e: HttpException){
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        }catch (e: Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = pref.getString("jwt",null) ?: return AuthResult.Unauthorized()
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