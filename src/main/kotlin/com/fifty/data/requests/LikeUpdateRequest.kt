package com.fifty.data.requests

import com.fifty.data.util.ParentType

data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)
