package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName
import com.upc.pry20231067.data.Place.Place

data class PlaceResponse (
    @SerializedName("message")
    var message: String,

    @SerializedName("data")
    var data: List<Place>
)