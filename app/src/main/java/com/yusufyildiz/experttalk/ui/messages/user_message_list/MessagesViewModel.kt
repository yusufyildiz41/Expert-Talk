package com.yusufyildiz.experttalk.ui.messages.user_message_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import com.yusufyildiz.experttalk.data.model.expert.ExpertRequest
import com.yusufyildiz.experttalk.domain.source.message.ChatSocketService
import com.yusufyildiz.experttalk.domain.source.message.MessageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val expertRepository: ExpertRepository,
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var dataLoading = MutableLiveData<Boolean>()

    private val _expertsStateFlow = MutableStateFlow(ArrayList<ExpertRequest>())
    val expertStateFlow = _expertsStateFlow.asStateFlow()

    fun getExperts(){
        viewModelScope.launch {
            dataLoading.value = true
            expertRepository.getAllExpertData().collect{ experts->
                _expertsStateFlow.value = experts
            }
            dataLoading.value = false
        }
    }
}