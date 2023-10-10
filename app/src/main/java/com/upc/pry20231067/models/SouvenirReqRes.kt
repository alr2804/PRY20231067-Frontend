package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName
data class CreateSouvenirRequest(
    @SerializedName("_user")
    var _user: String,

    @SerializedName("_object3D")
    var _object3D: String,
)

data class SouvenirResponse(
    @SerializedName("data")
    var data: Souvenir,
)

data class Souvenir(
    @SerializedName("_id")
    var _id: String,
    @SerializedName("_object3D")
    var _object3D: String,
    @SerializedName("_user")
    var _user: String,
)