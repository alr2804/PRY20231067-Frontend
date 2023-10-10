package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName
import com.upc.pry20231067.data.News.New

data class UserObjectResponse(
    @SerializedName("data")
    var data: UserObject,
)

data class CreateUserObjectRequest(
    @SerializedName("_user")
    var _user: String,

    @SerializedName("_object3d")
    var _object3d: String,
)

data class UserObject(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("_user")
    var _user: String,
    @SerializedName("_object3d")
    var _object3d: String,
    @SerializedName("hasVisited")
    var hasVisited: Boolean,

)