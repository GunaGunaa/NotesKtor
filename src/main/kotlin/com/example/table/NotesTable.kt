package com.example.table

import org.jetbrains.exposed.sql.Table

object NotesTable : Table() {
    val position_id = integer("position_id").autoIncrement()
    val email_id = varchar("email_id", length = 50).references(UserTable.email)
    val archived = bool("archived")
    val title = varchar("title", length = 255)
    val body = text("body")
    val created_time = long("created_time")
    val image = varchar("image", length = 255).nullable()
    val expiry_time = long("expiry_time").nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(position_id)
}