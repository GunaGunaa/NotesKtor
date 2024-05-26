package com.example.repository

import com.example.model.User
import com.example.table.NotesTable
import com.example.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(NotesTable)
//            SchemaUtils.drop(NotesTable)
            SchemaUtils.create(UserTable)
        }
    }

    fun hikari(): HikariDataSource {
        return try {
            val config = HikariConfig().apply {
                driverClassName = "org.postgresql.Driver"
                jdbcUrl = "jdbc:postgresql://localhost:8080/note_db?user=postgres&password=Thamilan@413003"
                maximumPoolSize = 3
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }

            println("Driver: ${config.driverClassName}")
            println("JDBC URL: ${config.jdbcUrl}")

            HikariDataSource(config)
        } catch (e: Exception) {
            println("Error creating HikariDataSource: ${e.message}")
            throw e
        }


    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}