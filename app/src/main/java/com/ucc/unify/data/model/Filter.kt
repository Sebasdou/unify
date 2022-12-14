package com.ucc.unify.data.model

data class Filter(
    val ageFrom: Long,
    val ageTo: Long,
    val sex: String,
    val interests: ArrayList<String>,
    val major: String
    )
