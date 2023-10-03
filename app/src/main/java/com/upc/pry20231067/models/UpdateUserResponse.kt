package com.upc.pry20231067.models

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(
    @SerializedName("message")
    var message: String,

    @SerializedName("data")
    var data: User
){
    // Constructor sin argumentos necesario para Gson
    constructor() : this("", User("", "", "", "", "", "", "", ""))
}
