package com.fifty.data.repository.post

import com.fifty.data.models.Post
import com.fifty.data.responses.PostResponse
import com.fifty.util.Constants

interface PostRepository {

    suspend fun createPost(post: Post): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostByFollows(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPostsForProfile(
        ownUserId:String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<PostResponse>

    suspend fun getPost(postId: String): Post?

    suspend fun getPostDetails(userId:String, postId:String): PostResponse?
}