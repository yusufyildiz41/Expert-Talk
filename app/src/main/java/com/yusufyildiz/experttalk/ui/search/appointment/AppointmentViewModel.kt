package com.yusufyildiz.experttalk.ui.search.appointment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.data.model.appointment.AppointmentRequest
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val expertRepository: ExpertRepository
): ViewModel() {

    val dataLoading = MutableLiveData<Boolean>()
    private val _expertDetailStateFlow = MutableStateFlow(ExpertRequest())
    val expertDetailStateFlow = _expertDetailStateFlow.asStateFlow()

    fun getExpertData(username: String){
        viewModelScope.launch {
            dataLoading.value = true
            expertRepository.getExpertDataWithUsername(username).collect{ expertRequest ->
                _expertDetailStateFlow.value = expertRequest
            }
            dataLoading.value = false
        }
    }

    fun addAppointment(appointmentRequestList: ArrayList<AppointmentRequest>){
        viewModelScope.launch {
            dataLoading.value = true
            expertRepository.addAppointment(appointmentRequestList)
            dataLoading.value = false
        }
    }

}