package com.upc.pry20231067.data.Review

import com.upc.pry20231067.models.User

data class Review (
    val _id: String,
    val _user: User,
    val _place: String,
    val content: String,
    val rating: Float
)