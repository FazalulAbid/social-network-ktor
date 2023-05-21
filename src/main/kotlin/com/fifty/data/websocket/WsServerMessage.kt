package com.fifty.data.websocket

import com.fifty.data.models.Message

data class WsServerMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            text = text,
            timestamp = timestamp,
            chatId = chatId,
            toId = toId
        )
    }
}
