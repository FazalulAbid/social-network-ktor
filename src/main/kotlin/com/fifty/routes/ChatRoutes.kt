package com.fifty.routes

import com.fifty.service.ChatService
import com.fifty.util.Constants
import com.fifty.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.getMessagesForChat(chatService: ChatService) {
    authenticate {
        get("/api/chat/messages") {
            val chatId = call.parameters[QueryParams.PARAM_CHAT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

            if (!chatService.doesChatBelongToUser(chatId, call.userId)) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            val messages = chatService.getMessagesForChat(chatId, page, pageSize)
            call.respond(
                HttpStatusCode.OK,
                messages
            )
        }
    }
}

fun Route.getChatsForUser(chatService: ChatService) {
    authenticate {
        get("/api/chats") {
            val chats = chatService.getChatForUser(call.userId)
            call.respond(
                HttpStatusCode.OK,
                chats
            )
        }
    }
}

fun Route.chatWebSocket(chatService: ChatService) {
    authenticate {
        webSocket("/api/chat/websocket") {
            incoming.consumeEach { frame ->
                when (frame) {
                    is Frame.Text -> {
                        if (frame.readText() == "Hello World") {
                            send(Frame.Text("Yo, what's up"))
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}