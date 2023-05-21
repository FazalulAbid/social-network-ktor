package com.fifty.data.websocket

import com.fifty.data.models.Message

data class WsClientMessage(
    val toId: String,
    val text: String,
    val chatId: String?
) {
    fun toMessage(fromId: String): Message {
        return Message(
            fromId = fromId,
            text = text,
            timestamp = System.currentTimeMillis(),
            chatId = chatId,
            toId = toId
        )
    }
}
