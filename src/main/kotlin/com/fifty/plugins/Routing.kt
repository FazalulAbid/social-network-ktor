package com.fifty.plugins

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.repository.user.UserRepository
import com.fifty.routes.createUserRoute
import com.fifty.routes.followUser
import com.fifty.routes.loginUser
import com.fifty.routes.unfollowUser
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()

    routing {
        // User routes
        createUserRoute(userRepository)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)
    }
}
