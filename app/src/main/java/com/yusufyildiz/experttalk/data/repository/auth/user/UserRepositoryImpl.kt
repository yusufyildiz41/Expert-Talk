package com.yusufyildiz.experttalk.data.repository.auth.user

import android.content.SharedPreferences
import com.yusufyildiz.experttalk.data.source.remote.AuthApi
import com.yusufyildiz.experttalk.common.UserAuthResult
import com.yusufyildiz.experttalk.data.model.user.TransformedUserRequest
import com.yusufyildiz.experttalk.data.model.user.UserRequest
import com.yusufyildiz.experttalk.domain.repository.auth.UserRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException


class UserRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
): UserRepository {
    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
        phone: String
    ): UserAuthResult<Unit> {
        return try {
            api.userSignUp(
                request = UserRequest(
                    email = email,
                    username = username,
                    password = password,
                    phone = phone,
                    _id = null
                )
            )
            signIn(email,password)
        } catch (e: HttpException) {
            if(e.code() == 401){
                UserAuthResult.UserUnAuthorized()
            }
            else{
                UserAuthResult.UserUnknownError()
            }
        } catch (e: Exception){
            UserAuthResult.UserUnknownError()
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): UserAuthResult<Unit> {
        return try {
            val response = api.userSignIn(
                request = TransformedUserRequest(
                    email = email,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt",response.token)
                .apply()
            UserAuthResult.UserAuthorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                UserAuthResult.UserUnAuthorized()
            }
            else{
                UserAuthResult.UserUnknownError()
            }
        } catch (e: Exception){
            UserAuthResult.UserUnknownError()
        }
    }

    override suspend fun authenticate(): UserAuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt",null) ?: return UserAuthResult.UserUnAuthorized()
            api.authenticate("Bearer $token")
            UserAuthResult.UserAuthorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                UserAuthResult.UserUnAuthorized()
            }
            else{
                UserAuthResult.UserUnknownError()
            }
        } catch (e: Exception){
            UserAuthResult.UserUnknownError()
        }
    }

    override suspend fun getUserData(email: String,password: String) = flow {
        emit(api.getUserData(email,password))
    }

    override suspend fun signOut(): UserAuthResult<Unit> {
        return try{
            prefs.edit().clear().apply()
            UserAuthResult.UserUnAuthorized()
        }catch (e: Exception){
            UserAuthResult.UserUnknownError()
        }
    }
}