package com.yusufyildiz.experttalk.domain.source.message

import com.yusufyildiz.experttalk.common.Utils.BASE_URL
import com.yusufyildiz.experttalk.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageService {

    suspend fun getAllMessages(): ArrayList<Message>//*

    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }

}