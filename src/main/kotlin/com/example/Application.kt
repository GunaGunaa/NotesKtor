package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.example.repository.NotesRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*

fun main() {
    System.setProperty("io.ktor.development", "true")
    embeddedServer(
        Netty,
        port = 8081,
        host = "127.0.0.1",
        module = Application::module,
        watchPaths = listOf("classes", "resources")
    )
        .start(wait = true)
}


fun Application.module() {

    DatabaseFactory.init()

    install(Locations)
    val db = NotesRepository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }
    install(Authentication) {

        jwt("jwt") {

            verifier(jwtService.varifier)
            realm = "Note Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }

        }

    }

    configureSecurity()
    configureSerialization()
    configureRouting(db,jwtService,hashFunction)
}
