package com.yusufyildiz.experttalk.ui.profile.expert

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
class ExpertProfileViewModel @Inject constructor(
    private val expertRepository: ExpertRepository
): ViewModel() {

    private val _expertStateFlow = MutableStateFlow(ExpertRequest())
    var expertStateFlow = _expertStateFlow.asStateFlow()

    fun getExpertData(email: String,password: String){
        viewModelScope.launch {
            expertRepository.getExpertData(email,password).collect{
                _expertStateFlow.value = it
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            expertRepository.signOut()
        }
    }
}