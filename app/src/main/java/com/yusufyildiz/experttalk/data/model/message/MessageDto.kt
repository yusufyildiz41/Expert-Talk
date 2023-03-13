package com.yusufyildiz.experttalk.data.model.message

import com.yusufyildiz.experttalk.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDto(
    val text: String?=null,
    val senderEmail: String?=null,
    val receiverEmail: String?=null,
    val timeStamp: Long?=null,
    val id: String?=null
) {
    fun toMessage(): Message {

        val date = timeStamp?.let { Date(it) }
        val formattedDate = date?.let {
            DateFormat
                .getDateInstance(DateFormat.DEFAULT)
                .format(it)
        }
        return Message(
            text = text,
            formattedTime = formattedDate,
            senderEmail = senderEmail,
            receiverEmail = receiverEmail
        )
    }
}