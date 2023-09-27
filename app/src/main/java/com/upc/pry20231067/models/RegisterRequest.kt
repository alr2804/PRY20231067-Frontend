package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("firstname")
    var firstname: String,
    @SerializedName("lastname")
    var lastname: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
)
