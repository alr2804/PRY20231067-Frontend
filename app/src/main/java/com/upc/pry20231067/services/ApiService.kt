package com.upc.pry20231067.services

import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.models.LoginResponse
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.models.RegisterRequest
import com.upc.pry20231067.models.RegisterResponse
import com.upc.pry20231067.models.UserResponse
import com.upc.pry20231067.models.UserResponseUnique
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getUser(@Url url: String): Response<UserResponse>

    @GET
    suspend fun getPlaces(@Url url: String): Response<PlaceResponse>

    @GET
    suspend fun getUserByID(@Url url: String): Response<UserResponseUnique>

    @POST
    suspend fun login(@Url url: String, @Body request: LoginRequest): Response<LoginResponse>

    @POST
    suspend fun register(@Url url: String, @Body request: RegisterRequest): Response<RegisterResponse>
}