package com.ucc.unify.data.model

data class Publication(
    val id: Int,
    val user: User,
    val publication: String,
    val comments: List<Comment>,
    val date: String
)
