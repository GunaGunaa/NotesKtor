package com.example.repository

import com.example.model.Notes
import com.example.model.User
import com.example.repository.DatabaseFactory.dbQuery
import com.example.table.NotesTable
import com.example.table.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class NotesRepository {
    suspend fun addNotes(notes: Notes, email: String) {
        dbQuery {
            NotesTable.insert { table ->
                table[position_id] = notes.position_id
                table[email_id] =email
                table[archived] = notes.archived
                table[title] = notes.title
                table[body] = notes.body
                table[created_time] = notes.created_time
                table[image] = notes.image
                table[expiry_time] = notes.expiry_time
            }
        }
    }

    suspend fun getAllNotes(email: String): List<Notes> {
        return dbQuery {
            NotesTable.select {
                NotesTable.email_id.eq(email)
            }.mapNotNull { rowToNote(it) }
        }
    }

    private fun rowToNote(row: ResultRow?): Notes? {
        if (row == null) {
            return null
        }
        return Notes(
            position_id = row[NotesTable.position_id],
            id = row[NotesTable.email_id],
            archived = row[NotesTable.archived],
            title = row[NotesTable.title],
            body = row[NotesTable.body],
            created_time = row[NotesTable.created_time],
            image = row[NotesTable.image],
            expiry_time = row[NotesTable.expiry_time]
        )

    }

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.userName] = user.userName
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.userName]
        )
    }
}