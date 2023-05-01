package com.fifty.plugins

import com.fifty.repository.user.UserRepository
import com.fifty.routes.createUserRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    routing {
        createUserRoute(userRepository)
    }
}
