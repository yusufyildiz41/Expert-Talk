package com.yusufyildiz.experttalk.data.source.remote

import com.yusufyildiz.experttalk.common.Resource
import com.yusufyildiz.experttalk.data.model.message.MessageDto
import com.yusufyildiz.experttalk.domain.model.Message
import com.yusufyildiz.experttalk.domain.source.message.ChatSocketService
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(
  private val client: HttpClient
) : ChatSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(senderEmail: String, receiverEmail: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.Endpoints.ChatSocket.url}?userEmail=${senderEmail}&expertEmail=${receiverEmail}")
            }
            if(socket?.isActive == true){
                Resource.Success(Unit)
            } else{
                Resource.Error("Couldn't establish a connection")
            }
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<MessageDto>(json)
                    messageDto.toMessage()
                } ?: flow {
                    println("data cannot received")
            }
        }catch (e: Exception){
            e.printStackTrace()
            flow {
                println("error ${e.localizedMessage}")
            }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

}