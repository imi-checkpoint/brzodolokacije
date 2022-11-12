package com.example.frontend.data.remote

import com.example.frontend.data.remote.dto.LocationDTO
import com.example.frontend.data.remote.dto.LoginDTO
import com.example.frontend.data.remote.dto.PostDTO
import com.example.frontend.domain.model.RegisterUser
import retrofit2.http.*

interface CheckpointApi {
    @POST("api/register")
    suspend fun register(
        @Body appUser:RegisterUser
    ): String

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("username") username:String,
        @Field("password") password:String
    ) : LoginDTO

    @Headers("Content-Type: application/json")
    @GET("location/all")
    suspend fun getAll(
        @Header("Authorization") token : String
    ): List<LocationDTO>

    @Headers("Content-Type: application/json")
    @GET("location/keyword/{name}")
    suspend fun searchLocation(
        @Header("Authorization") token : String,
        @Path("name") name:String
    ): List<LocationDTO>

    @Headers("Content-Type: application/json")
    @GET("post/location/{locationId}")
    suspend fun getPostsFromLocation(
        @Header("Authorization") token : String,
        @Path("locationId") locationId:Long
    ): List<PostDTO>
}