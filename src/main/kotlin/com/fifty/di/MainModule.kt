package com.fifty.di

import com.fifty.repository.user.UserRepository
import com.fifty.repository.user.UserRepostiroyImpl
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
}

