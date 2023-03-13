package com.yusufyildiz.experttalk.ui.search.expert_detail

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
class ExpertDetailViewModel @Inject constructor(
    private val expertRepository: ExpertRepository
): ViewModel() {

    var dataLoading = MutableLiveData<Boolean>()
    private val _expertDetailStateFlow = MutableStateFlow(ExpertRequest())
    val expertDetailStateFlow = _expertDetailStateFlow.asStateFlow()

    fun getExpertDetail(username: String){
        dataLoading.value = true
        viewModelScope.launch {
            expertRepository.getExpertDataWithUsername(username).collect{ expertRequest ->
                _expertDetailStateFlow.value = expertRequest
            }
        }
        dataLoading.value = false
    }
}