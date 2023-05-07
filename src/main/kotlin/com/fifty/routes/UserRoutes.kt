package com.fifty.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fifty.data.models.User
import com.fifty.data.requests.CreateAccountRequest
import com.fifty.data.requests.LoginRequest
import com.fifty.data.requests.UpdateProfileRequest
import com.fifty.data.responses.AuthResponse
import com.fifty.data.responses.BasicApiResponse
import com.fifty.service.PostService
import com.fifty.service.UserService
import com.fifty.util.ApiResponseMessages
import com.fifty.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.fifty.util.ApiResponseMessages.FIELDS_BLANK
import com.fifty.util.ApiResponseMessages.USER_ALREADY_EXISTS
import com.fifty.util.Constants
import com.fifty.util.Constants.BASE_URL
import com.fifty.util.Constants.PROFILE_PICTURE_PATH
import com.fifty.util.QueryParams
import com.fifty.util.save
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*


fun Route.searchUser(userService: UserService) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<User>()
                )
                return@get
            }
            val searchResults = userService.searchForUsers(query, call.userId)
            call.respond(
                HttpStatusCode.OK,
                searchResults
            )
        }
    }
}

fun Route.getPostsForProfile(
    postService: PostService,
) {
    authenticate {
        get("/api/user/posts") {
            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize =
                call.parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostsForProfile(
                userId = call.userId, page = page, pageSize = pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.getUserProfile(userService: UserService) {
    authenticate {
        get("/api/user/profile") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            if (userId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val profileResponse = userService.getUserProfile(userId, call.userId)
            if (profileResponse == null) {
                call.respond(
                    HttpStatusCode.OK, BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
                return@get
            }
            call.respond(
                HttpStatusCode.OK,
                profileResponse
            )
        }
    }
}

fun Route.updateUserProfile(userService: UserService) {
    val gson: Gson by inject()
    authenticate {
        put("/api/user/update") {
            val multipart = call.receiveMultipart()
            var updateProfileRequest: UpdateProfileRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "update_profile_data") {
                            updateProfileRequest = gson.fromJson(
                                partData.value,
                                UpdateProfileRequest::class.java
                            )
                        }
                    }

                    is PartData.FileItem -> {
                        fileName = partData.save(PROFILE_PICTURE_PATH)
                    }

                    is PartData.BinaryItem -> Unit

                    is PartData.BinaryChannelItem -> Unit
                }
            }
            if (updateProfileRequest == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val profilePictureUrl = "${BASE_URL}profile_pictures/$fileName"

            updateProfileRequest?.let { request ->
                val updateAcknowledged = userService.updateUser(
                    userId = call.userId,
                    profileImageUrl = profilePictureUrl,
                    updateProfileRequest = request
                )
                if (updateAcknowledged) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true
                        )
                    )
                } else {
                    File("${PROFILE_PICTURE_PATH}/$fileName").deleteOnExit()
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
        }
    }
}

