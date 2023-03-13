package com.yusufyildiz.experttalk.data.repository.auth.expert

import android.content.SharedPreferences
import com.yusufyildiz.experttalk.data.source.remote.AuthApi
import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.data.model.expert.TransformedExpertRequest
import com.yusufyildiz.experttalk.data.model.appointment.AppointmentRequest
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ExpertRepositoryImpl(
    private val api: AuthApi,
    private val pref: SharedPreferences
): ExpertRepository {
    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
        phone: String,
        expertCategory: String,
        expertId: String,
        expertTime: String,
        expertPrice: String,
        about: String,
        longAbout: String,
        expertCategoryDetail: ArrayList<CategoryItems>
    ): ExpertAuthResult<Unit> {
        return try {
            api.expertSignUp(
                request = ExpertRequest(
                    email = email,
                    username = username,
                    password = password,
                    phone = phone,
                    expertCategory = expertCategory,
                    expertId = expertId,
                    expertTime = expertTime,
                    expertPrice = expertPrice,
                    about = about,
                    longAbout = longAbout,
                    expertCategoryDetail = expertCategoryDetail,
                    _Expert_id = null
                )
            )
            signIn(email,password)
        }catch (e: HttpException){
            if(e.code() == 401){
                ExpertAuthResult.ExpertUnAuthorized()
            }
            else{
                ExpertAuthResult.ExpertUnknownError()
            }
        } catch (e: Exception){
            ExpertAuthResult.ExpertUnknownError()
        }

    }
    override suspend fun signIn(
        email: String,
        password: String
    ): ExpertAuthResult<Unit> {
        return try{
            val response = api.expertSignIn(
                request = TransformedExpertRequest(
                    email = email,
                    password = password
                )
            )
            pref.edit()
                .putString("jwt",response.token)
                .apply()
            ExpertAuthResult.ExpertAuthorized()
        }catch (e: HttpException){
            if(e.code() == 401){
                ExpertAuthResult.ExpertUnAuthorized()
            }
            else{
                ExpertAuthResult.ExpertUnknownError()
            }
        }catch (e: Exception){
            ExpertAuthResult.ExpertUnknownError()
        }
    }
    override suspend fun authenticate(): ExpertAuthResult<Unit> {
        return try {
            val token = pref.getString("jwt",null) ?: return ExpertAuthResult.ExpertUnAuthorized()
            api.authenticate("Bearer $token")
            ExpertAuthResult.ExpertAuthorized()
        } catch (e: HttpException) {
            if(e.code() == 401){
                ExpertAuthResult.ExpertUnAuthorized()
            }
            else{
                ExpertAuthResult.ExpertUnknownError()
            }
        } catch (e: Exception){
            ExpertAuthResult.ExpertUnknownError()
        }
    }
    override suspend fun getExpertData(email: String,password: String) = flow {
        emit(api.getExpertData(email,password))
    }
    override suspend fun signOut(): ExpertAuthResult<Unit> {
        return try{
            pref.edit().clear().apply()
            ExpertAuthResult.ExpertUnAuthorized()
        }catch (e: Exception){
            ExpertAuthResult.ExpertUnknownError()
        }
    }
    override suspend fun getAllExpertData() = flow {
        emit(api.getAllExpertData())
    }
    override suspend fun getExpertDataWithUsername(username: String) = flow {
        emit(api.getExpertDataWithUsername(username))
    }

    override suspend fun addAppointment(
        appointments: ArrayList<AppointmentRequest>
    ) {
        api.addAppointment(
            request = appointments
        )
    }
}