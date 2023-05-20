package com.fifty.data.repository.activity

import com.fifty.data.models.Activity
import com.fifty.data.responses.ActivityResponse
import com.fifty.util.Constants

interface ActivityRepository {

    suspend fun getActivitiesForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE
    ): List<ActivityResponse>

    suspend fun createActivity(activity: Activity)

    suspend fun deleteActivity(activityId: String): Boolean
}