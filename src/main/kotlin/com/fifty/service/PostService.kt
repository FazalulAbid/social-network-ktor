package com.fifty.service

import com.fifty.data.models.Post
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.requests.CreatePostRequest

class PostService(
    private val repository: PostRepository
) {

    suspend fun createPostIfUserExists(request: CreatePostRequest): Boolean {
        return repository.createPostIfUserExists(
            Post(
                imageUrl = "",
                userId = request.userId,
                timestamp = System.currentTimeMillis(),
                description = request.description
            )
        )
    }
}