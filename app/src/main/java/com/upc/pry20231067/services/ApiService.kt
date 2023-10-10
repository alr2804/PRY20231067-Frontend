package com.upc.pry20231067.services

import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.models.LoginResponse
import com.upc.pry20231067.models.NewsResponse
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.models.PostForoResponse
import com.upc.pry20231067.models.RegisterRequest
import com.upc.pry20231067.models.RegisterResponse
import com.upc.pry20231067.models.ReviewRequest
import com.upc.pry20231067.models.ReviewResponse
import com.upc.pry20231067.models.ReviewResponseUnique
import com.upc.pry20231067.models.ReviewUniqueResponse
import com.upc.pry20231067.models.UpdateUserRequest
import com.upc.pry20231067.models.UpdateUserResponse
import com.upc.pry20231067.models.UserResponse
import com.upc.pry20231067.models.UserResponseUnique
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET
    suspend fun getUser(@Url url: String): Response<UserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: String): Call<UserResponseUnique>

    @GET("places")
    fun getPlaces(): Call<PlaceResponse>

    @GET("news")
    fun getNews(): Call<NewsResponse>

    @GET("reviews/getReviewByPlaceID/{id}")
    fun getReviewByPlaceID(@Path("id") idPlace: String): Call<ReviewResponse>

    @POST("reviews")
    fun createReview(@Body request: ReviewRequest): Call<ReviewUniqueResponse>



    @GET("users/{id}")
    fun getUserByID(@Path("id") userId: String): Call<UserResponseUnique>

    @POST
    fun login(@Url url: String, @Body request: LoginRequest): Call<LoginResponse>

    @POST
    suspend fun register(@Url url: String, @Body request: RegisterRequest): Response<RegisterResponse>

    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: String, @Body request: UpdateUserRequest): Call<UpdateUserResponse>

    @GET("postsForo")
    fun getPostsForo(): Call<PostForoResponse>
}