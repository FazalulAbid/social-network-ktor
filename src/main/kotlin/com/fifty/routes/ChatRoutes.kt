package com.fifty.routes

import com.fifty.data.websocket.WsClientMessage
import com.fifty.service.chat.ChatController
import com.fifty.service.chat.ChatService
import com.fifty.util.Constants
import com.fifty.util.QueryParams
import com.fifty.util.WebSocketObject
import com.fifty.util.fromJsonOrNull
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.java.KoinJavaComponent.inject

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
            call.respond(HttpStatusCode.OK, messages)
        }
    }
}

fun Route.getChatsForUser(chatService: ChatService) {
    authenticate {
        get("/api/chats") {
            val chats = chatService.getChatsForUser(call.userId)
            print("RespondChatDto $chats")
            call.respond(HttpStatusCode.OK, chats)
        }
    }
}

fun Route.chatWebSocket(chatController: ChatController) {
    authenticate {
        webSocket("/api/chat/websocket") {
            chatController.onJoin(call.userId, this)
            try {
                print("Worked")
                incoming.consumeEach { frame ->
                    print("Worked inside ")
                    kotlin.run {
                        print("Worked inside 2 ")
                        when (frame) {
                            is Frame.Text -> {
                                val frameText = frame.readText()
                                val delimiterIndex = frameText.indexOf("#")
                                if (delimiterIndex == -1) {
                                    println("No delimiter found")
                                    return@run
                                }
                                val type = frameText.substring(0, delimiterIndex).toIntOrNull()
                                if (type == null) {
                                    println("Invalid format")
                                    return@run
                                }
                                val json = frameText.substring(delimiterIndex + 1, frameText.length)
                                handleWebSocket(call.userId, chatController, type, frameText, json)
                            }

                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                print("This is it ${e.message}")
                e.printStackTrace()
            } finally {
                println("Disconnecting ${call.userId}")
                chatController.onDisconnect(call.userId)
            }
        }
    }
}

suspend fun handleWebSocket(
    ownUserId: String,
    chatController: ChatController,
    type: Int,
    frameText: String,
    json: String
) {
    val gson by inject<Gson>(Gson::class.java)
    when (type) {
        WebSocketObject.MESSAGE.ordinal -> {
            val message = gson.fromJsonOrNull(json, WsClientMessage::class.java) ?: return
            chatController.sendMessage(ownUserId, gson, message)
        }
    }
}