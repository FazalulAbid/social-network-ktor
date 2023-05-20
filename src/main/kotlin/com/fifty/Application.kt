package com.fifty

import com.fifty.di.mainModule
import com.fifty.plugins.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
            EngineMain.main(args)
        }
    }
    server.start(wait = true)
}

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        this.modules(mainModule)
    }
    configureSecurity()
    configureSockets()
    configureSessions()
    configureRouting()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}