package com.fifty.data.util

sealed class ActivityType(val type: Int) {
    object LikedPost : ActivityType(0)
    object LikedComment : ActivityType(1)
    object CommentedOnPost : ActivityType(2)
    object FollowedUser : ActivityType(3)
}

class Solution {
    fun duplicateZeros(arr: IntArray): Unit {
        for (i in arr.indices) {

        }
    }
}