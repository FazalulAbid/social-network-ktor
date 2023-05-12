package com.fifty.data.repository.activity

import com.fifty.data.models.Activity
import com.fifty.data.models.Following
import com.fifty.data.models.Post
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class ActivityRepositoryImpl(
    db: CoroutineDatabase
) : ActivityRepository {

    private val activities = db.getCollection<Activity>()

    override suspend fun getActivitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<Activity> {
        return activities.find(Activity::toUserId `in` userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Activity::timestamp)
            .toList()
    } 

    override suspend fun createActivity(activity: Activity) {
        activities.insertOne(activity)
    }

    override suspend fun deleteActivity(activityId: String): Boolean {
        return activities.deleteOneById(activityId).wasAcknowledged()
    }
}