package com.fifty.routes

import com.fifty.data.repository.user.UserRepository
import com.fifty.data.models.User
import com.fifty.data.requests.CreateAccountRequest
import com.fifty.data.requests.LoginRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.service.UserService
import com.fifty.util.ApiResponseMessages
import com.fifty.util.ApiResponseMessages.ERROR_INVALID_CREDENTIALS
import com.fifty.util.ApiResponseMessages.FIELDS_BLANK
import com.fifty.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoute(
    userService: UserService
) {
    post("/api/user/create") {
        val request =
            kotlin.runCatching { call.receiveNullable<CreateAccountRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        if (userService.doesUserWithEmailExists(request.email)) {
            call.respond(
                BasicApiResponse(
                    successful = false,
                    message = USER_ALREADY_EXISTS
                )
            )
            return@post
        }
        when (userService.validateCreateAccountRequest(request)) {
            is UserService.ValidationEvent.ErrorFieldEmpty -> {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
            }

            is UserService.ValidationEvent.SuccessEvent -> {
                userService.createUser(request)
                call.respond(
                    BasicApiResponse(successful = true)
                )
            }
        }
    }
}

fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request =
            kotlin.runCatching { call.receiveNullable<LoginRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userRepository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
        )
        if (isCorrectPassword) {
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
                    message = ERROR_INVALID_CREDENTIALS
                )
            )
        }
    }
}