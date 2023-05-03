package com.fifty.service

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.repository.likes.LikeRepository
import com.fifty.data.repository.user.UserRepository
import com.fifty.data.responses.UserResponseItem

class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun likeParent(userId: String, parentId: String, parentType: Int): Boolean {
        return likeRepository.likeParent(userId, parentId, parentType)
    }

    suspend fun unlikeParent(userId: String, parentId: String): Boolean {
        return likeRepository.unlikeParent(userId, parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikeForParent(parentId)
    }

    suspend fun getUsersWhoLikedForParent(parentId: String, userId: String): List<UserResponseItem> {
        val userIds = likeRepository.getLikesForParent(parentId).map { it.userId }
        val users = userRepository.getUsers(userIds)
        val followsByUser = followRepository.getFollowsByUser(userId)
        return users.map { user ->
            val isFollowing = followsByUser.find { it.followedUserId == user.id } != null
            UserResponseItem(
                username = user.username,
                profilePictureUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }
}