package com.fifty.routes

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.requests.FollowUpdateRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.util.ApiResponseMessages.USER_NOT_FOUND
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(followRepository: FollowRepository) {
    post("/api/following/follow") {
        val request =
            kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

        val didUserExist = followRepository.followUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
        if (didUserExist) {
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
                    message = USER_NOT_FOUND
                )
            )
        }
    }
}

fun Route.unfollowUser(followRepository: FollowRepository) {
    delete("/api/following/unfollow") {
        val request =
            kotlin.runCatching { call.receiveNullable<FollowUpdateRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

        val didUserExist = followRepository.unfollowUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
        if (didUserExist) {
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
                    message = USER_NOT_FOUND
                )
            )

        }
    }
}