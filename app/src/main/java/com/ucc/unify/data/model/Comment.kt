package com.ucc.unify.data.model

data class Comment (
    val id: Int,
    val user: User,
    val comment: String,
    val date: String
)