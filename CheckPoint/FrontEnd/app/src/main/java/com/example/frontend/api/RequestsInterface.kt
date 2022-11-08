package com.example.frontend.api

import com.example.frontend.models.LocationDTO
import com.example.frontend.models.PostDTO
import com.example.frontend.models.RegisterDTO
import com.example.frontend.models.Tokens
import retrofit2.Call
import retrofit2.http.*

interface RequestsInterface {
    @POST("api/register")
    fun register(
        @Body appUser:RegisterDTO
    ):Call<String>

    @FormUrlEncoded
    @POST("api/login")
    fun login(@Field("username") username:String, @Field("password") password:String):Call<Tokens>

    @Headers("Content-Type: application/json")
    @GET("location/keyword/{name}")
    fun searchLocation(@Header("Authorization") token : String, @Path("name") name:String):Call<List<LocationDTO>>

    @Headers("Content-Type: application/json")
    @GET("location/all")
    fun getAll(@Header("Authorization") token : String):Call<List<LocationDTO>>

    @Headers("Content-Type: application/json")
    @GET("post/location/{locationId}")
    fun getPostsFromLocation(@Header("Authorization") token : String,@Path("locationId") locationId:Long):Call<List<PostDTO>>
}