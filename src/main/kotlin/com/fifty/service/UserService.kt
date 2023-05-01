package com.fifty.service

import com.fifty.data.models.User
import com.fifty.data.repository.user.UserRepository
import com.fifty.data.requests.CreateAccountRequest
import com.fifty.data.responses.BasicApiResponse
import com.fifty.util.ApiResponseMessages
import io.ktor.server.application.*
import io.ktor.server.response.*

class UserService(
    private val repository: UserRepository
) {

    suspend fun doesUserWithEmailExists(email: String): Boolean {
        return repository.getUserByEmail(email) != null
    }

    suspend fun createUser(request: CreateAccountRequest) {
        repository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                gitHubUrl = null,
                instagramUrl = null,
                linkedInUrl = null
            )
        )
    }

    fun validateCreateAccountRequest(request: CreateAccountRequest): ValidationEvent {
        if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            return ValidationEvent.ErrorFieldEmpty
        }
        return ValidationEvent.SuccessEvent
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object SuccessEvent : ValidationEvent()
    }

}