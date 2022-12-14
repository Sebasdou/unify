package com.ucc.unify.data.model

data class User(
    val name: String,
    val age: Int,
    val sex: String,
    val interests: ArrayList<String>,
    val major: String,
    val hasProfileData: Boolean
)
