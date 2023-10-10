package com.upc.pry20231067.ui.foro.dao

import com.google.gson.annotations.SerializedName

data class PostForoRequest(
    @SerializedName("content")
    var content: String,

    @SerializedName("_id")
    var _id: String
)

data class CreatePostForoRequest(
    @SerializedName("content")
    var content: String,

    @SerializedName("_user")
    var _user: String
)

data class UpdatePostForoRequest(
    @SerializedName("content")
    var content: String,

)