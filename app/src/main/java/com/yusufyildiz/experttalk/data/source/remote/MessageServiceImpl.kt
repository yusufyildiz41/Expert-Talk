package com.yusufyildiz.experttalk.data.source.remote

import com.yusufyildiz.experttalk.data.model.message.MessageDto
import com.yusufyildiz.experttalk.domain.model.Message
import com.yusufyildiz.experttalk.domain.source.message.MessageService
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MessageServiceImpl(
    private val client: HttpClient
) : MessageService {

    override suspend fun getAllMessages(): ArrayList<Message> {
        return try{
            ArrayList(client.get<List<MessageDto>>(
                MessageService.Endpoints.GetAllMessages.url
            ).map { it.toMessage() })
        }
        catch (e: Exception){
            arrayListOf()
        }
    }
//* // map -> {messagesDto -> messages}

}