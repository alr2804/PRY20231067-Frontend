package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName
import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.data.Review.Review

data class ReviewResponseUnique(
    @SerializedName("data")
    var data: Review
)
