package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("content")
    var content: String,
    @SerializedName("rating")
    var rating: Float,
    @SerializedName("_user")
    var _user: String,
    @SerializedName("_place")
    var _place: String,
)
