package com.example.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val email = varchar("email", length = 255).uniqueIndex()
    val hashPassword = varchar("hash_password", length = 255)
    val userName = varchar("user_name", length = 255)

    override val primaryKey = PrimaryKey(email)
}