package com.upc.pry20231067.ui.foro.models

import com.upc.pry20231067.models.User

data class PostForo (
    val _id: String,
    val _user: User,
    val content: String,
)