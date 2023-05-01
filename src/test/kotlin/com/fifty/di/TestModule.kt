package com.fifty.di

import com.fifty.repository.user.FakeUserRepository
import org.koin.dsl.module

internal val testModule = module {
    single { FakeUserRepository() }
}