package com.ucc.unify.data.model

data class Reply (
    val id: Int,
    val user: User,
    val comment: String,
    val date: String
)