package com.yusufyildiz.experttalk.ui.profile.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.domain.repository.auth.UserRepository
import com.yusufyildiz.experttalk.data.model.user.UserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userStateFlow = MutableStateFlow(UserRequest())
    var userStateFlow = _userStateFlow.asStateFlow()

    fun getUserData(email: String,password: String){
        viewModelScope.launch {
            userRepository.getUserData(email,password).collect {
                _userStateFlow.value = it
            }
        }
    }

    fun signOut(){
        viewModelScope.launch {
            userRepository.signOut()
        }
    }

}