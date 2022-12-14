package com.ucc.unify.data.model

data class Post(
    val id: Int,
    val user: User,
    val publication: String,
    val replies: List<Reply>,
    val date: String
)
