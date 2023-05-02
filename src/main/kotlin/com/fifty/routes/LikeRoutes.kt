package com.fifty.routes

import com.fifty.data.requests.LikeUpdateRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.data.util.ParentType
import com.fifty.service.ActivityService
import com.fifty.service.LikeService
import com.fifty.service.UserService
import com.fifty.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParent(
    likeService: LikeService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/like") {
            val request = kotlin.runCatching { call.receiveNullable<LikeUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            val userId = call.userId
            val likeSuccessful = likeService.likeParent(call.userId, request.parentId, request.parentType)
            if (likeSuccessful) {
                activityService.addLikeActivity(
                    byUserId = userId,
                    parentType = ParentType.fromType(request.parentType),
                    parentId = request.parentId
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unlikeParent(
    likeService: LikeService,
    userService: UserService
) {
    authenticate {
        delete("/api/unlike") {
            val request = kotlin.runCatching { call.receiveNullable<LikeUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@delete
            }
            val unlikeSuccessful = likeService.unlikeParent(call.userId, request.parentId)
            if (unlikeSuccessful) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}