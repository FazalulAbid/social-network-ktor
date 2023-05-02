package com.fifty.service

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.followUserIfExists(
            followingUserId = followingUserId,
            followedUserId = request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists(request: FollowUpdateRequest, followingUserId: String): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId = followingUserId,
            followedUserId = request.followedUserId
        )
    }
}