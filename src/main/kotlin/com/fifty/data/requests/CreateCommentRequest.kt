package com.fifty.data.requests

import com.fifty.data.models.Comment

data class CreateCommentRequest(
    val comment: String,
    val postId: String,
    val userId: String
)
