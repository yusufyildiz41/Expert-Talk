package com.yusufyildiz.experttalk.domain.source.message

import com.yusufyildiz.experttalk.common.Resource
import com.yusufyildiz.experttalk.common.Utils
import com.yusufyildiz.experttalk.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(
        senderEmail: String,
        receiverEmail: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    sealed class Endpoints(val url: String){
        object ChatSocket: Endpoints("${Utils.WEBSOCKET_URL}/chat-socket")
    }

}