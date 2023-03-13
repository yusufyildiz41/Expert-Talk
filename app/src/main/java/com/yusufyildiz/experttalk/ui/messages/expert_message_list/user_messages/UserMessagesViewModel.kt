package com.yusufyildiz.experttalk.ui.messages.expert_message_list.user_messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusufyildiz.experttalk.common.Resource
import com.yusufyildiz.experttalk.data.model.chat.ChatState
import com.yusufyildiz.experttalk.domain.model.Message
import com.yusufyildiz.experttalk.domain.source.message.ChatSocketService
import com.yusufyildiz.experttalk.domain.source.message.MessageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMessagesViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService
) : ViewModel() {

    val dataLoading = MutableSharedFlow<Boolean>()
    private val _state = MutableStateFlow<ChatState>(ChatState())
    val state = _state.asStateFlow()
    private val _messageStateFlow = MutableStateFlow(Message())
    var messageStateFlow = _messageStateFlow.asStateFlow()
    private val _messagesStateFlow = MutableStateFlow<ArrayList<Message>>(arrayListOf())
    var messagesStateFlow = _messagesStateFlow.asStateFlow()

    init {
        getAllMessages()
    }

    fun getAllMessages(){
        viewModelScope.launch {
            dataLoading.emit(true)
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _state.value = state.value.copy(
                messages = result,
                isLoading = false
            )
        }
    }



    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun addSession(senderEmail: String, receiverEmail: String){
        viewModelScope.launch {
            val result = chatSocketService.initSession(senderEmail,receiverEmail)
            when(result){
                is Resource.Success -> {
                    println("The session operation was successful")
                }
                is Resource.Error -> {
                    println("error : ${result.message}")
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            chatSocketService.sendMessage(message)
        }
    }

    fun observeMessages(){
        chatSocketService.observeMessages()
            .onEach { message ->
                val newList = state.value.messages.toMutableList().apply {
                    add(0,message)
                }
                _state.value = state.value.copy(
                    messages = ArrayList(newList)
                )
            }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}