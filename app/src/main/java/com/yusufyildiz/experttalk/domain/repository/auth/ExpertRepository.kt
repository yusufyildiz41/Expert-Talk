package com.yusufyildiz.experttalk.domain.repository.auth

import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.data.model.appointment.AppointmentRequest
import com.yusufyildiz.experttalk.data.model.category_model.CategoryItems
import kotlinx.coroutines.flow.Flow

interface ExpertRepository {
    suspend fun signUp(
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
    ): ExpertAuthResult<Unit>
    suspend fun signIn(
        email: String,
        password: String
    ): ExpertAuthResult<Unit>
    suspend fun authenticate(): ExpertAuthResult<Unit>
    suspend fun getExpertData(email: String,password: String): Flow<ExpertRequest>
    suspend fun signOut(): ExpertAuthResult<Unit>
    suspend fun getAllExpertData(): Flow<ArrayList<ExpertRequest>>
    suspend fun getExpertDataWithUsername(username: String): Flow<ExpertRequest>
    suspend fun addAppointment(appointments: ArrayList<AppointmentRequest>)
}