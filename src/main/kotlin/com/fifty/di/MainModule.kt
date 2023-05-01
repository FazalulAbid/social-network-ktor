package com.fifty.di

import com.fifty.data.repository.follow.FollowRepository
import com.fifty.data.repository.follow.FollowRepositoryImpl
import com.fifty.data.repository.post.PostRepository
import com.fifty.data.repository.post.PostRepositoryImpl
import com.fifty.data.repository.user.UserRepository
import com.fifty.data.repository.user.UserRepostiroyImpl
import com.fifty.service.FollowService
import com.fifty.service.PostService
import com.fifty.service.UserService
import com.fifty.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }
    single<UserRepository> {
        UserRepostiroyImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single { UserService(get()) }
    single { FollowService(get()) }
    single { PostService(get()) }
}

