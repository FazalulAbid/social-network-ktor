package com.fifty.service

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.requests.FollowUpdateRequest

class FollowService(
    private val followRepository: FollowRepository
) {

    suspend fun followUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.followUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
    }

    suspend fun unfollowUserIfExists(request: FollowUpdateRequest): Boolean {
        return followRepository.unfollowUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )
    }
}