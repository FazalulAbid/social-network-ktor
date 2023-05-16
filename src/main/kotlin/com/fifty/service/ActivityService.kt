package com.fifty.service

import com.fifty.data.models.Activity
import com.fifty.data.repository.activity.ActivityRepository
import com.fifty.data.repository.comment.CommentRepository
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.responses.ActivityResponse
import com.fifty.data.util.ActivityType
import com.fifty.data.util.ParentType
import com.fifty.util.Constants

class ActivityService(
    private val activityRepository: ActivityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<ActivityResponse> {
        return activityRepository.getActivitiesForUser(userId, page, pageSize)
    }

    suspend fun addCommentActivity(
        byUserId: String,
        postId: String
    ): Boolean {
        val userIdOfPost = postRepository.getPost(postId)?.userId ?: return false
        if (byUserId == userIdOfPost) return false
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = userIdOfPost,
                type = ActivityType.CommentedOnPost.type,
                parentId = postId
            )
        )
        return true
    }

    suspend fun addLikeActivity(
        byUserId: String,
        parentType: ParentType,
        parentId: String
    ): Boolean {
        val toUserId = when (parentType) {
            ParentType.Post -> {
                postRepository.getPost(parentId)?.userId
            }

            ParentType.Comment -> {
                commentRepository.getComment(parentId)?.userId
            }

            ParentType.None -> return false
        } ?: return false
        if (byUserId == toUserId) return false
        activityRepository.createActivity(
            Activity(
                timestamp = System.currentTimeMillis(),
                byUserId = byUserId,
                toUserId = toUserId,
                type = when (parentType) {
                    is ParentType.Post -> ActivityType.LikedPost.type
                    is ParentType.Comment -> ActivityType.LikedComment.type
                    else -> ActivityType.LikedPost.type
                },
                parentId = parentId
            )
        )
        return true
    }

    suspend fun createActivity(activity: Activity) {
        activityRepository.createActivity(activity)
    }

    suspend fun deleteActivity(activityId: String): Boolean {
        return activityRepository.deleteActivity(activityId)
    }
}