package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName

data class UserResponseUnique (
    @SerializedName("data")
    var data: User
)
