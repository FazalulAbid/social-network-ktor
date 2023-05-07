package com.fifty

import com.fifty.di.mainModule
import com.fifty.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit {
    val server = embeddedServer(Netty, port = 8001) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
            io.ktor.server.netty.EngineMain.main(args)
        }
    }
    server.start(wait = true)
}

@Suppress("unused")
fun Application.module() {
    install(Koin) {
        this.modules(mainModule)
    }
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
