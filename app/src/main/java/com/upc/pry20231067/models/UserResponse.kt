package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("message")
    var message: String,

    @SerializedName("data")
    var data: List<User>
)
