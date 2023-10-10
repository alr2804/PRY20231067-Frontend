package com.upc.pry20231067.ui.foro.dao

import com.google.gson.annotations.SerializedName
import com.upc.pry20231067.ui.foro.models.PostForo

data class PostForoResponse(
    @SerializedName("message")
    var message: String,

    @SerializedName("data")
    var data: List<PostForo>
)
