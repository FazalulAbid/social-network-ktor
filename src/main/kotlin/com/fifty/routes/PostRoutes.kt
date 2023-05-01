package com.fifty.routes

import com.fifty.data.models.Post
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.requests.CreatePostRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.service.PostService
import com.fifty.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(postService: PostService) {
    post("/api/post/create") {
        val request =
            kotlin.runCatching { call.receiveNullable<CreatePostRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

        val didUserExist = postService.createPostIfUserExists(request)
        if (!didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true
                )
            )
        }
    }
}