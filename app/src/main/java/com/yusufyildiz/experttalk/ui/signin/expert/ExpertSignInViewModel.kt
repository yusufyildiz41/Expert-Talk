package com.yusufyildiz.experttalk.ui.signin.expert

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.common.ExpertAuthResult
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpertSignInViewModel @Inject constructor(
  private val expertRepository: ExpertRepository
) : ViewModel() {

    val resultLoading = MutableLiveData<Boolean>()
    private val resultChannel = Channel<ExpertAuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun signIn(
        email: String,
        password: String
    ){
        viewModelScope.launch {
            val result = expertRepository.signIn(
                email = email,
                password = password
            )
            resultChannel.send(result)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            resultLoading.value = true
            val authenticateResult =  expertRepository.authenticate()
            resultChannel.send(authenticateResult)
            resultLoading.value = false
        }
    }
}