package com.upc.pry20231067.services

import com.upc.pry20231067.models.CreateSouvenirRequest
import com.upc.pry20231067.models.CreateUserObjectRequest
import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.models.LoginResponse
import com.upc.pry20231067.models.NewsResponse
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.ui.foro.dao.PostForoRequest
import com.upc.pry20231067.ui.foro.dao.PostForoResponse
import com.upc.pry20231067.models.RegisterRequest
import com.upc.pry20231067.models.RegisterResponse
import com.upc.pry20231067.models.ReviewRequest
import com.upc.pry20231067.models.ReviewResponse
import com.upc.pry20231067.models.ReviewUniqueResponse
import com.upc.pry20231067.models.SouvenirResponse
import com.upc.pry20231067.models.UpdateUserRequest
import com.upc.pry20231067.models.UpdateUserResponse
import com.upc.pry20231067.models.UserObjectResponse
import com.upc.pry20231067.models.UserResponse
import com.upc.pry20231067.models.UserResponseUnique
import com.upc.pry20231067.ui.foro.dao.CreatePostForoRequest
import com.upc.pry20231067.ui.foro.dao.UpdatePostForoRequest
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

    // USER ENDPOINTS
    @GET
    suspend fun getUser(@Url url: String): Response<UserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: String): Call<UserResponseUnique>

    @GET("users/{id}")
    fun getUserByID(@Path("id") userId: String): Call<UserResponseUnique>

    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: String, @Body request: UpdateUserRequest): Call<UpdateUserResponse>

    // PLACES ENDPOINTS

    @GET("places")
    fun getPlaces(): Call<PlaceResponse>


    // NEWS ENDPOINTS
    @GET("news")
    fun getNews(): Call<NewsResponse>

    // REVIEWS ENDPOINTS

    @GET("reviews/getReviewByPlaceID/{id}")
    fun getReviewByPlaceID(@Path("id") idPlace: String): Call<ReviewResponse>

    @POST("reviews")
    fun createReview(@Body request: ReviewRequest): Call<ReviewUniqueResponse>


    // AUTH ENDPOINTS

    @POST
    fun login(@Url url: String, @Body request: LoginRequest): Call<LoginResponse>

    @POST
    suspend fun register(@Url url: String, @Body request: RegisterRequest): Response<RegisterResponse>


    // POST FORO ENDPOINT
    @GET("postsForo")
    fun getPostsForo(): Call<PostForoResponse>

    @GET("postsForo/getPostForoByuserID/{id}")
    fun getPostForoByuserID(@Path("id") userId: String): Call<PostForoResponse>

    @POST("postsForo")
    fun createPostForo(@Body request: CreatePostForoRequest): Call<PostForoRequest>

    @PUT("postsForo/{id}")
    fun updatePostForo(@Path("id") postId: String, @Body request: UpdatePostForoRequest): Call<PostForoRequest>

    @DELETE("postsForo/{id}")
    fun deletePostForo(@Path("id") postId: String): Call<PostForoRequest>

    // SOUVENIR ENDPOINTS

    @GET("souvenirs")
    fun getSouvenirs(): Call<SouvenirResponse>

    @POST("souvenirs")
    fun createSouvenir(@Body request: CreateSouvenirRequest): Call<SouvenirResponse>


    // USER OBJECT ENDPOINTS
    @GET("users-object3d/{idUser}/{idObject3D}")
    fun getUserObject3D(@Path("idUser") idUser: String, @Path("idObject3D") idObject3D: String) : Call<UserObjectResponse>

    @POST("users-object3d")
    fun getUserObject3D(@Body request: CreateUserObjectRequest) : Call<UserObjectResponse>

}