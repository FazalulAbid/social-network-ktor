package com.fifty.plugins

import com.fifty.routes.*
import com.fifty.service.FollowService
import com.fifty.service.LikeService
import com.fifty.service.PostService
import com.fifty.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPost(postService, userService)
        getPostsForFollows(postService, userService)
        deletePost(postService, userService)

        // Like routing
        likeParent(likeService, userService)
        unlikeParent(likeService, userService)
    }
}
