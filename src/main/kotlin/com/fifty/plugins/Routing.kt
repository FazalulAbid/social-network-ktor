package com.fifty.plugins

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.repository.user.UserRepository
import com.fifty.routes.*
import com.fifty.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val userService: UserService by inject()
    val followRepository: FollowRepository by inject()
    val postRepository: PostRepository by inject()

    routing {
        // User routes
        createUserRoute(userService)
        loginUser(userRepository)

        // Following routes
        followUser(followRepository)
        unfollowUser(followRepository)

        // Post routes
        createPostRoute(postRepository)
    }
}
