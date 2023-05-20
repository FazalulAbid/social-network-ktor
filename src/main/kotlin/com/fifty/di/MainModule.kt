package com.fifty.di

import com.fifty.data.models.Skill
import com.fifty.data.repository.activity.ActivityRepository
import com.fifty.data.repository.activity.ActivityRepositoryImpl
import com.fifty.data.repository.chat.ChatRepository
import com.fifty.data.repository.chat.ChatRepositoryImpl
import com.fifty.data.repository.comment.CommentRepository
import com.fifty.data.repository.comment.CommentRepositoryImpl
import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.repository.follow.FollowRepositoryImpl
import com.fifty.data.repository.likes.LikeRepository
import com.fifty.data.repository.likes.LikeRepositoryImpl
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.repository.post.PostRepositoryImpl
import com.fifty.data.repository.skill.SkillRepository
import com.fifty.data.repository.skill.SkillRepositoryImpl
import com.fifty.data.repository.user.UserRepository
import com.fifty.data.repository.user.UserRepositoryImpl
import com.fifty.service.*
import com.fifty.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<SkillRepository> {
        SkillRepositoryImpl(get())
    }
    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }
    single { UserService(get(), get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
    single { LikeService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { ActivityService(get(), get(), get()) }
    single { SkillService(get()) }
    single { ChatService(get()) }

    single { Gson() }
}

