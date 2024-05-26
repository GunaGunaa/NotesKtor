package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.NotesRepository
import com.example.routes.NoteRoutes
import com.example.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(db: NotesRepository, jwtService: JwtService, hashFunction: (String) -> String) {
    routing {
        UserRoutes(db,jwtService,hashFunction)
        NoteRoutes(db, hashFunction)
    }
}
