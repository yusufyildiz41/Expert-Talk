package com.yusufyildiz.experttalk.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expertRepository: ExpertRepository
): ViewModel(){

    val dataLoading = MutableLiveData<Boolean>()
    private val _expertsStateFlow = MutableStateFlow<ArrayList<ExpertRequest>>(arrayListOf())
    var expertsStateFlow = _expertsStateFlow.asStateFlow()

    fun getAllExpertsData(){
        viewModelScope.launch {
            dataLoading.value = true
            expertRepository.getAllExpertData().collect{ experts ->
                _expertsStateFlow.value = experts
                dataLoading.value = false
            }
        }
    }

}