package com.fifty.data.repository.likes

interface LikeRepository {

    suspend fun likeParent(userId: String, parentId: String): Boolean

    suspend fun unlikeParent(userId: String, parentId: String): Boolean

    suspend fun deleteLikeForParent(parentId: String)
}