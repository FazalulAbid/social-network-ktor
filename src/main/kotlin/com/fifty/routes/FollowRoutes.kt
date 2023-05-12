package com.fifty.routes

import com.fifty.data.models.Activity
import com.fifty.data.requests.FollowUpdateRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.data.util.ActivityType
import com.fifty.service.ActivityService
import com.fifty.service.FollowService
import com.fifty.util.ApiResponseMessages.USER_NOT_FOUND
import com.fifty.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/following/follow") {
            val request =
                kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

            if (followService.followUserIfExists(request, call.userId)) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId = call.userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unfollowUser(followService: FollowService) {
    authenticate {
        delete("/api/following/unfollow") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            if (followService.unfollowUserIfExists(followedUserId = userId, call.userId)) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
            }
        }
    }
}