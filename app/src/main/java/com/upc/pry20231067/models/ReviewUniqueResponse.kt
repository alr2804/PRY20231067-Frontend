package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName
import com.upc.pry20231067.data.Review.Review

data class ReviewUniqueResponse(
    @SerializedName("data")
    var data: Review
)