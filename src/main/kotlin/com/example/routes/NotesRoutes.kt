package com.example.routes

import com.example.model.Notes
import com.example.model.SimpleResponse
import com.example.model.User
import com.example.repository.NotesRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.text.get

fun Route.NoteRoutes(
    db:NotesRepository,
    hashFunction: (String)->String
) {

    authenticate("jwt") {

        post("v1/notes/create") {

            val note = try {
                call.receive<Notes>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try {

                val email = call.principal<User>()!!.email
                db.addNotes(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Added Successfully!"))

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
            }

        }


        get("v1/notes/getAll") {

            try {
                val email = call.principal<User>()!!.email
                val notes = db.getAllNotes(email)
                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, emptyList<Notes>())
            }
        }



//        post<NoteUpdateRoute> {
//
//            val note = try {
//                call.receive<Notes>()
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
//                return@post
//            }
//
//            try {
//
//                val email = call.principal<User>()!!.email
//                db.updateNote(note, email)
//                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully!"))
//
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
//            }
//
//        }


//        delete<NoteDeleteRoute> {
//
//            val noteId = try {
//                call.request.queryParameters["id"]!!
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "QueryParameter:id is not present"))
//                return@delete
//            }
//
//
//            try {
//
//                val email = call.principal<User>()!!.email
//                db.deleteNote(noteId, email)
//                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully!"))
//
//            } catch (e: Exception) {
//                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem Occurred!"))
//            }
//
//        }
    }
}