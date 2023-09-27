package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("token")
    var token: String,

    @SerializedName("data")
    var data: RegisterResponseData,

    @SerializedName("message")
    var message: String?
)

data class RegisterResponseData(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("firstname")
    var firstname: String,
    @SerializedName("lastname")
    var lastname: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("urlImageProfile")
    var urlImageProfile: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("updatedAt")
    var updatedAt: String,

    )
