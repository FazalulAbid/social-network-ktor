package com.fifty.data.responses

data class BasicApiResponse(
    val successful: Boolean,
    val message: String? = null
)