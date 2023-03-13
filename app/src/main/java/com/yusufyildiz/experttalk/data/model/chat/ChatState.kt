package com.yusufyildiz.experttalk.data.model.chat

import com.yusufyildiz.experttalk.domain.model.Message

data class ChatState(
    val messages: ArrayList<Message> = arrayListOf(),
    val isLoading: Boolean = false
)