package com.example.model

data class Notes(
    val position_id:Int,
    val id: String,
    val archived: Boolean,
    val title: String,
    val body: String,
    val created_time: Long,
    val image: String? ,
    val expiry_time: Long?
)