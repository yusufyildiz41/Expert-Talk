package com.yusufyildiz.experttalk.domain.model

data class Message(
    val text:String?=null,
    val senderEmail: String?=null,
    val receiverEmail: String?=null,
    val formattedTime: String?=null
)